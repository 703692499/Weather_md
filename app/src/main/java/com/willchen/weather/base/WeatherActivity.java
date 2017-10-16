package com.willchen.weather.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.willchen.weather.R;
import com.willchen.weather.adapter.CityAdapter;
import com.willchen.weather.adapter.GvDailyAdapter;
import com.willchen.weather.adapter.GvLifeAdapter;
import com.willchen.weather.adapter.GvNormalAdapter;
import com.willchen.weather.config.Constant;
import com.willchen.weather.db.biz.SQLiteHandle;
import com.willchen.weather.https.HttpWeather;
import com.willchen.weather.utils.WeatherIconUtil;
import com.willchen.weather.view.DailyView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class WeatherActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private int mMode;
    private int shareDay;
    private Toolbar mToolbar;
    private ImageButton ibWeatherAdd, ibWeatherShare, ibWeatherSetting;
    private TextView tvWeatherWord, tvWeatherIcon, tvWeatherFeel, tvWeatherHumidity, tvWeatherWind, tvWeatherTime, tvWeatherUpdateTime;
    private ImageView ivWeatherIcon;
    private GridView gvWeatherNormal, gvWeatherLife, gvWeatherDaily;
    private LinearLayout llWeatherFeel, llWeatherHum, llWeatherWind;
    private RelativeLayout rlWeatherDaily;
    private HttpWeather httpWeather;
    private String cityName;
    private List<Integer> mHighTemp, mLowTemp;
    private List<String> mBriefList, mTextList;
    private List<String> mDailyDate;
    private List<Integer> mDailyIcon;
    private GvLifeAdapter mLifeAdapter;
    private GvNormalAdapter mNormalAdapter;
    private GvDailyAdapter mDailyAdapter;
    private String[] shareDaily;
    private WeatherIconUtil iconUtil;
    private Spinner spWeatherCity;
    private CityAdapter cityAdapter;
    private List<String> historyCity;
    private SQLiteHandle sqLiteHandle;
    private SwipeRefreshLayout refreshLayout;
    private DailyView dailyView;
    private Calendar calendar;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SEARCH_NOW:
                    mMode = Constant.FROM_SQL;
                    setWeatherNow(msg.obj.toString(), Constant.RESULT_HTTP);
                    refreshLayout.setRefreshing(false);
                    break;
                case Constant.NOT_CONNECT:
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(WeatherActivity.this, "网络未连接,请检查设置!", Toast.LENGTH_SHORT).show();
                    mMode = Constant.FROM_SQL;
                    break;

            }
            return false;
        }

    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_show);
        init();
    }

    private void init() {
        ibWeatherAdd = (ImageButton) findViewById(R.id.ib_weather_add);
        ibWeatherShare = (ImageButton) findViewById(R.id.ib_weather_share);
        ibWeatherSetting = (ImageButton) findViewById(R.id.ib_weather_settings);
        tvWeatherWord = (TextView) findViewById(R.id.tv_widget_word);
        tvWeatherIcon = (TextView) findViewById(R.id.tv_widget_text);
        tvWeatherFeel = (TextView) findViewById(R.id.tv_weather_feel);
        tvWeatherHumidity = (TextView) findViewById(R.id.tv_weather_humidity);
        tvWeatherWind = (TextView) findViewById(R.id.tv_weather_wind);
        tvWeatherTime = (TextView) findViewById(R.id.tv_weather_time);
        tvWeatherUpdateTime = (TextView) findViewById(R.id.tv_weather_update);
        ivWeatherIcon = (ImageView) findViewById(R.id.iv_widget_icon);
        gvWeatherNormal = (GridView) findViewById(R.id.gv_weather_normal);
        gvWeatherLife = (GridView) findViewById(R.id.gv_weather_life);
        gvWeatherDaily = (GridView) findViewById(R.id.gv_weather_daily);
        mToolbar = (Toolbar) findViewById(R.id.tb_weather);
        spWeatherCity = (Spinner) findViewById(R.id.sp_weather_city);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_weather_refresh);
        refreshLayout.setColorSchemeResources(R.color.red);

        llWeatherFeel = (LinearLayout) findViewById(R.id.ly_weather_feel);
        llWeatherHum = (LinearLayout) findViewById(R.id.ly_weather_humidity);
        llWeatherWind = (LinearLayout) findViewById(R.id.ly_weather_wind);

        rlWeatherDaily = (RelativeLayout) findViewById(R.id.rl_weather_daily);

        initEvent();

    }

    private void initEvent() {
        iconUtil = new WeatherIconUtil();
        httpWeather = new HttpWeather();
        setSupportActionBar(mToolbar);
        shareDaily = new String[5];
        mBriefList = new ArrayList<>();
        mTextList = new ArrayList<>();
        mDailyDate = new ArrayList<>();
        mDailyIcon = new ArrayList<>();
        historyCity = new ArrayList<>();
        mHighTemp = new ArrayList<>();
        mLowTemp = new ArrayList<>();
        dailyView = (DailyView) findViewById(R.id.dv_weather_daily);
        isNull();


        spWeatherCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (mMode == Constant.RESULT_HTTP) {
                    onRefresh();
                    refreshLayout.setRefreshing(true);
                } else {
                    cityName = historyCity.get(i);
                    List<String> historyWeather = sqLiteHandle.getWeather(historyCity.get(i));
                    if (historyWeather.get(0) != null) {
                        setWeatherNow(historyWeather.get(0), 0);
                    }
                    if (historyWeather.get(1) != null) {
                        setWeatherDaily(historyWeather.get(1), 0);
                    }
                    if (historyWeather.get(2) != null) {
                        setLifeView(historyWeather.get(2), 0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ibWeatherAdd.setOnClickListener(this);
        ibWeatherShare.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        ibWeatherSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_weather_add:
                Intent intent = new Intent(WeatherActivity.this, CitySearchActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.ib_weather_share:
                shareDayWeather();
                break;
            case R.id.ib_weather_settings:
                popupSettingWindow();
                break;
        }
    }


    /*
    * 分享功能
    * */
    private void shareDayWeather() {
        final AlertDialog alertDialog = new AlertDialog.Builder(WeatherActivity.this, R.style.Dialog).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        final Window window = alertDialog.getWindow();
        window.setContentView(R.layout.weather_share_dialog);
        SeekBar sbSelect = (SeekBar) window.findViewById(R.id.sb_share_day);
        final TextView tvShareDay = (TextView) window.findViewById(R.id.tv_share_day);
        Button btnShare = (Button) window.findViewById(R.id.btn_share_share);

        sbSelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 3) {
                    if (i == 0) {
                        tvShareDay.setText("今");
                    } else if (i == 1) {
                        tvShareDay.setText("明");
                    } else if (i == 2) {
                        tvShareDay.setText("后");
                    }
                } else {
                    tvShareDay.setText(Integer.toString(i + 1));
                }
                shareDay = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                if (shareDaily.length > 0) {
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareDaily[shareDay]);

                }
                startActivity(Intent.createChooser(shareIntent, "分享"));
                alertDialog.dismiss();
            }
        });


    }

    /*
    * 设置窗口
    * */
    private void popupSettingWindow() {
        final AlertDialog alertDialog = new AlertDialog.Builder(WeatherActivity.this, R.style.Dialog).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        final Window window = alertDialog.getWindow();
        window.setContentView(R.layout.weather_setting_dialog);
        TextView tvTitle = (TextView) window.findViewById(R.id.tv_setting_title);
        Button btnTen = (Button) window.findViewById(R.id.btn_setting_ten);
        Button btnSixty = (Button) window.findViewById(R.id.btn_setting_sixty);
        Button btnHour = (Button) window.findViewById(R.id.btn_setting_twelve);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        int updateTime = sharedPreferences.getInt("time", 10);
        if (updateTime == 10) {
            tvTitle.setText("更新频率: " + updateTime + " 分钟");
        } else if (updateTime == 60) {
            tvTitle.setText("更新频率: " + updateTime + " 分钟");
        } else {
            tvTitle.setText("更新频率: " + updateTime + " 小时");
        }
        btnTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("time", 10);
                editor.apply();
                alertDialog.dismiss();
            }
        });
        btnSixty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("time", 60);
                editor.apply();
                alertDialog.dismiss();

            }
        });
        btnHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("time", 12);
                editor.apply();
                alertDialog.dismiss();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Constant.RESULT_LIST) {
                    historyCity = sqLiteHandle.getCityHistory();
                    cityAdapter = new CityAdapter(this, historyCity);
                    cityAdapter.setSelectPosition(null, Constant.MODE_WEATHER);
                    spWeatherCity.setAdapter(cityAdapter);
                    mMode = Constant.RESULT_HTTP;
                    spWeatherCity.setSelection(data.getIntExtra("city_return", 0), true);
                } else if (resultCode == Constant.RESULT_HTTP) {
                    historyCity = sqLiteHandle.getCityHistory();
                    cityAdapter = new CityAdapter(this, historyCity);
                    cityAdapter.setSelectPosition(null, Constant.MODE_WEATHER);
                    spWeatherCity.setAdapter(cityAdapter);
                    mMode = Constant.RESULT_HTTP;
                    spWeatherCity.setSelection(0, true);
                } else {
                    historyCity = sqLiteHandle.getCityHistory();
                    cityAdapter = new CityAdapter(this, historyCity);
                    cityAdapter.setSelectPosition(null, Constant.MODE_WEATHER);
                    spWeatherCity.setAdapter(cityAdapter);
                }
                break;
        }
    }


    /*
    * 链接网络
    * */
    private void refreshWeather() throws UnsupportedEncodingException {
        if (spWeatherCity.getCount() > 0) {
            String response = historyCity.get(Integer.parseInt(spWeatherCity.getSelectedItem().toString()));
            if (!response.equals("")) {
                cityName = response.substring(0, response.indexOf(","));
                httpWeather.sendHttpRequestWeather(Constant.SEARCH_WEATHER + URLEncoder.encode(cityName, "utf-8"), handler);
                Log.d("www", Constant.SEARCH_WEATHER + URLEncoder.encode(cityName, "utf-8"));

               /* httpWeather.sendHttpRequestWeather(Constant.SEARCH_WEATHER_NOW + URLEncoder.encode(cityName, "utf-8") +
                        Constant.SEARCH_WEATHER_LANGUAGE + Constant.SEARCH_WEATHER_UNIT, handler, Constant.SEARCH_NOW);

                httpWeather.sendHttpRequestWeather(Constant.SEARCH_WEATHER_DAILY + URLEncoder.encode(cityName, "utf-8") +
                        Constant.SEARCH_WEATHER_LANGUAGE + Constant.SEARCH_WEATHER_UNIT +
                        Constant.SEARCH_WEATHER_START + Constant.SEARCH_WEATHER_DAYS, handler, Constant.SEARCH_DAILY);

                httpWeather.sendHttpRequestWeather(Constant.SEARCH_WEATHER_SUGGESTION + URLEncoder.encode(cityName, "utf-8") +
                        Constant.SEARCH_WEATHER_LANGUAGE, handler, Constant.SEARCH_SUGGESTION);*/

            }
        } else {
            refreshLayout.setRefreshing(false);
        }
    }


    /*
    * 判断历史数据库
    * */
    public void isNull() {
        sqLiteHandle = new SQLiteHandle(this);
        historyCity = sqLiteHandle.getCityHistory();
        if (historyCity.size() == 0) {
            Intent intent = new Intent(WeatherActivity.this, CitySearchActivity.class);
            startActivityForResult(intent, 1);
        } else {
            cityAdapter = new CityAdapter(this, historyCity);
            cityAdapter.setSelectPosition(null, Constant.MODE_WEATHER);
            spWeatherCity.setAdapter(cityAdapter);

        }
    }


    /*
    * 设置生活指数
    * */
    private void setLifeView(String msg, int mode) {

        mBriefList.clear();
        String[] life = msg.split(",");
        for (int i = 0; i < life.length; i++)
            mBriefList.add(life[i]);

        if (mode == Constant.RESULT_HTTP) {
            sqLiteHandle.saveLife(cityName, msg);
        }
        gvWeatherLife.setAdapter(new GvLifeAdapter(WeatherActivity.this, mBriefList));
        gvWeatherLife.setVisibility(View.VISIBLE);

    }

    /*
    * 设置多天天气
    * */
    private void setWeatherDaily(String msg, int mode) {
        mDailyDate.clear();
        mDailyIcon.clear();
        mHighTemp.clear();
        mLowTemp.clear();
        String[] dayWeather = msg.split("。");
        for (int i = 0; i < 5; i++) {
            Log.d("mmm", dayWeather[i]);
            String[] eachDay = dayWeather[i].split(",");
            Log.d("ss", eachDay.length + "            asd");
            shareDaily[i] = "日期: " + eachDay[0] + "\n天气状况: " + eachDay[2] + "\n温度范围: " + eachDay[5] + "-" + eachDay[4] + "°C" + "\n降水概率: " +
                    eachDay[6] + "\n气压:" + eachDay[7] + "\n能见度:" + eachDay[8];
            mDailyDate.add(eachDay[0]);
            mDailyIcon.add(iconUtil.getDarkIcon(Integer.parseInt(eachDay[1])));
            mHighTemp.add(Integer.parseInt(eachDay[4]));
            mLowTemp.add(Integer.parseInt(eachDay[5]));
        }
        if (mode == Constant.RESULT_HTTP) {
            sqLiteHandle.saveDaliyWeather(cityName, msg);
        }
//        mHighTemp =new ArrayList<>();
//        mLowTemp = new ArrayList<>();
//        mHighTemp.add(30);
//        mHighTemp.add(40);
//        mHighTemp.add(30);
//        mHighTemp.add(60);
//        mHighTemp.add(30);
//        mLowTemp.add(20);
//        mLowTemp.add(30);
//        mLowTemp.add(10);
//        mLowTemp.add(7);
//        mLowTemp.add(2 );
        dailyView.setHighTemp(mHighTemp);
        dailyView.setLowTemp(mLowTemp);
        gvWeatherDaily.setAdapter(new GvDailyAdapter(WeatherActivity.this, mDailyDate, mDailyIcon));
        rlWeatherDaily.setVisibility(View.VISIBLE);
    }


    /*
    * 设置今天天气
    * */
    private void setWeatherNow(String msg, int mode) {

        String[] weather = msg.split(",");
        mTextList.clear();
        mTextList.add(weather[6].equals("") ? "无" : weather[6]);
        mTextList.add(weather[4].equals("") ? "无" : weather[4]);
        mTextList.add(weather[10].equals("") ? "无" : weather[10]);
        mTextList.add(weather[7].equals("") ? "无" : weather[7]);


        calendar = Calendar.getInstance();
        if (mode == Constant.RESULT_HTTP) {
            sqLiteHandle.saveNowWeather(cityName, msg);
            String updateTime = "";
            if (Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)).length() <= 1) {
                updateTime += "0" + calendar.get(Calendar.HOUR_OF_DAY) + " : ";
            } else {
                updateTime += calendar.get(Calendar.HOUR_OF_DAY) + " : ";
            }
            if (Integer.toString(calendar.get(Calendar.MINUTE)).length() <= 1) {
                updateTime += "0" + calendar.get(Calendar.MINUTE);
            } else {
                updateTime += calendar.get(Calendar.MINUTE);
            }
            sqLiteHandle.saveUpdateTime(cityName, updateTime);
        }

        tvWeatherUpdateTime.setText(sqLiteHandle.getUpdateTime(cityName));
        tvWeatherWord.setText(weather[2]);
        ivWeatherIcon.setBackgroundResource(iconUtil.getIconView(Integer.parseInt(weather[1])));
        tvWeatherIcon.setText(weather[0]);
        tvWeatherFeel.setText(weather[3]);
        tvWeatherHumidity.setText(weather[5]);
        tvWeatherWind.setText(weather[9]);
        tvWeatherTime.setText(weather[11]);

        llWeatherHum.setVisibility(View.VISIBLE);
        llWeatherWind.setVisibility(View.VISIBLE);
        llWeatherFeel.setVisibility(View.VISIBLE);


        gvWeatherNormal.setAdapter(new GvNormalAdapter(WeatherActivity.this, mTextList));
        gvWeatherNormal.setVisibility(View.VISIBLE);

        String[] otherWeather = msg.split("_");
        setLifeView(otherWeather[1], Constant.RESULT_HTTP);
        setWeatherDaily(otherWeather[2], Constant.RESULT_HTTP);
        Log.d("nnn", otherWeather[2]);
    }

    @Override
    public void onRefresh() {
        try {
            refreshWeather();
        } catch (UnsupportedEncodingException e) {
        }
    }
}
