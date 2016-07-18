package com.willchen.weather.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.willchen.weather.R;
import com.willchen.weather.adapter.CityAdapter;
import com.willchen.weather.config.Constant;
import com.willchen.weather.db.biz.SQLiteHandle;
import com.willchen.weather.https.HttpCity;
import com.willchen.weather.utils.CityJsonUtil;
import com.willchen.weather.utils.LocationUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/31.
 */
public class CitySearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private int mMultiMode = Constant.MODE_SINGLE;
    private int mCityMode = 0;
    private Toolbar mToolbar;
    private ListView lvCitySearch;
    private Toolbar tbCitySearch;
    private EditText etCitySearch;
    private TextView tvCitySearchClear, tvCitySearchId, tvCitySearchAll, tvCityGps;
    private ImageButton btnCitySearchDel;
    private LinearLayout lySingleMode, lyMultiMode;
    private HttpCity httpSend;
    private ArrayAdapter<String> mAdapter;
    private CityAdapter adapter;
    private SQLiteHandle sqliteHandle;
    private CityJsonUtil jsonUtil;
    private List<String> listHistory;
    private List<String> listCity;
    private List<String> listHttpCity;
    private Set<Integer> selectPosition;
    private String searchCity;
    private String gpsLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.city_search);
        init();
    }

    private void init() {

        lvCitySearch = (ListView) findViewById(R.id.lv_city_search);
        etCitySearch = (EditText) findViewById(R.id.et_citysearch_edit);
        tvCitySearchClear = (TextView) findViewById(R.id.tv_citysearch_clear);
        tvCityGps = (TextView) findViewById(R.id.tv_citysearch_gps);

        mToolbar = (Toolbar) findViewById(R.id.tb_city_search);
        lySingleMode = (LinearLayout) findViewById(R.id.ly_citysearch_singlemode);

        lyMultiMode = (LinearLayout) findViewById(R.id.ly_citysearch_multimode);

        initEvent();

    }

    private void initEvent() {
        httpSend = new HttpCity();
        listHistory = new ArrayList<String>();
        listCity = new ArrayList<String>();
        listHttpCity = new ArrayList<String>();
        sqliteHandle = new SQLiteHandle(this);
        jsonUtil = new CityJsonUtil();
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        getGpsLocation();

        queryHistory();
        adapter = new CityAdapter(this, listHistory);
        lvCitySearch.setAdapter(adapter);


        tvCityGps.setOnClickListener(this);
        tvCitySearchClear.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(this);
        lvCitySearch.setOnItemClickListener(this);
        etCitySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchCity = etCitySearch.getText().toString().trim();
                if (!searchCity.equals("")) {
                    searchCityName(searchCity);
                } else if (etCitySearch.length() > 0 && searchCity.equals("")) {
                    Toast.makeText(CitySearchActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etCitySearch.length() == 0) {
                    queryHistory();
                }
            }
        });
        lvCitySearch.setOnItemLongClickListener(this);
    }


    private void getGpsLocation() {
        LocationUtil locationUtil = new LocationUtil(getApplicationContext(), handler);
    }


    private void queryHistory() {
        listHistory = sqliteHandle.getCityHistory();
        adapter = new CityAdapter(this, listHistory);
        lvCitySearch.setAdapter(adapter);
    }

    private void searchCityName(String searchCity) {
        try {
            if (!searchCity.equals("")) {
                httpSend.sendHttpRequest(sqliteHandle, Constant.SEARCH_CITY_URL + URLEncoder.encode(searchCity, "utf-8"), handler, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * 如果数据库存储的与网络的不相同，优先选择网络
    * */
    private void chooseList(String searchCity) {
        if (sqliteHandle.queryCity(searchCity)) {
            listCity = sqliteHandle.getCity(searchCity);
            if (listCity.size() != listHttpCity.size() && listHttpCity != null) {
                adapter = new CityAdapter(CitySearchActivity.this, listHttpCity);
                lvCitySearch.setAdapter(adapter);
                mCityMode = 0;
            } else {
                adapter = new CityAdapter(CitySearchActivity.this, listCity);
                lvCitySearch.setAdapter(adapter);
                mCityMode = 1;
            }
        } else {
            adapter = new CityAdapter(CitySearchActivity.this, listHttpCity);
            lvCitySearch.setAdapter(adapter);
            mCityMode = 0;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case Constant.BTN_BACK:     //返回键
                if (mMultiMode == Constant.MODE_MORE) {
                    mMultiMode = Constant.MODE_SINGLE;
                    multiModeView();
                } else {
                    Intent intent = new Intent();
                    setResult(Constant.RESULT_UPDATE, intent);
                    finish();
                }
                break;
            case R.id.tv_citysearch_clear:          //清空键
                if (!etCitySearch.getText().toString().equals("")) {
                    etCitySearch.setText("");
                }
                break;
            case R.id.tv_citysearch_gps:
                if (!tvCityGps.getText().toString().equals("定位失败,请检查设置") && !tvCityGps.getText().toString().equals("正在定位中")) {
                    sqliteHandle.saveHistory(tvCityGps.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("city_return", 0);
                    setResult(Constant.RESULT_HTTP, intent);
                    finish();
                }else {
                    getGpsLocation();
                    tvCityGps.setText("正在定位中");
                }
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.CITY_JSON:
                    listHttpCity = (ArrayList<String>) msg.obj;
                    chooseList(searchCity);
                    break;
                case Constant.GPS:
                    if (msg.obj != null) {
                        gpsLocation = msg.obj.toString();
                        httpSend.sendHttpRequest(sqliteHandle, Constant.SEARCH_CITY_URL + gpsLocation, handler, Constant.GPS);
                    } else {
                        tvCityGps.setText("定位失败,请检查设置");
                    }
                    break;
                case Constant.GPS_JSON:
                    if (!msg.obj.toString().equals("")) {
                        tvCityGps.setText(msg.obj.toString());
                    }
                    break;
                case Constant.NOT_CONNECT:
                    Toast.makeText(CitySearchActivity.this, "网络未连接,请检查设置!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (mMultiMode == Constant.MODE_MORE) {
            if (!selectPosition.contains(position)) {
                selectPosition.add(position);
            } else {
                selectPosition.remove(position);
            }
            if (selectPosition.isEmpty()) {
                mMultiMode = Constant.MODE_SINGLE;
                multiModeView();
            }
            tvCitySearchId.setText("已选择 " + selectPosition.size() + " 项");
            adapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent();
            if (etCitySearch.length() > 0) {
                if (mCityMode == 1) {
                    sqliteHandle.saveHistory(listCity.get(position));
                    intent.putExtra("city_return", position);
                    setResult(Constant.RESULT_LIST, intent);
                } else {
                    sqliteHandle.saveHistory(listHttpCity.get(position));
                    setResult(Constant.RESULT_HTTP, intent);
                }
            } else {
                sqliteHandle.saveHistory(listHistory.get(position));
                intent.putExtra("city_return", position);
                setResult(Constant.RESULT_LIST, intent);
            }
            finish();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,
                                   long l) {
        hideSoftWare();
        mMultiMode = Constant.MODE_MORE;
        multiModeView();
        selectPosition = new HashSet<Integer>();
        selectPosition.add(position);
        adapter.setSelectPosition(selectPosition, mMultiMode);
        tvCitySearchId.setText("已选择 " + selectPosition.size() + " 项");
        adapter.notifyDataSetChanged();
        return true;
    }

    private void multiModeView() {
        if (mMultiMode == Constant.MODE_MORE) {
            lySingleMode.setVisibility(View.GONE);
            tvCitySearchId = (TextView) findViewById(R.id.tv_citysearch_mutimode_id);
            tvCitySearchAll = (TextView) findViewById(R.id.tv_citysearch_mutimode_all);
            btnCitySearchDel = (ImageButton) findViewById(R.id.btn_citysearch_multimode_del);

            btnCitySearchDel.setOnClickListener(new multiModeClick());
            tvCitySearchAll.setOnClickListener(new multiModeClick());

            lyMultiMode.setVisibility(View.VISIBLE);
        } else {
            lySingleMode.setVisibility(View.VISIBLE);
            lyMultiMode.setVisibility(View.GONE);
            queryHistory();
        }
    }


    public void hideSoftWare() {
        InputMethodManager imm = (InputMethodManager) CitySearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(etCitySearch.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMultiMode == Constant.MODE_MORE) {
                mMultiMode = Constant.MODE_SINGLE;
                multiModeView();
            } else {
                Intent intent = new Intent();
                setResult(Constant.RESULT_UPDATE, intent);
                finish();
            }
        }
        return false;
    }


    public class multiModeClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_citysearch_multimode_del:     //删除键
                    mMultiMode = Constant.MODE_SINGLE;
                    sqliteHandle.deleteHistory(selectPosition, listHistory);
                    multiModeView();
                    break;
                case R.id.tv_citysearch_mutimode_all:       //全选键
                    if (selectPosition.size() != listHistory.size()) {
                        for (int i = 0; i < listHistory.size(); i++) {
                            selectPosition.add(i);
                        }
                        tvCitySearchId.setText("已选择 " + selectPosition.size() + " 项");
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }


}
