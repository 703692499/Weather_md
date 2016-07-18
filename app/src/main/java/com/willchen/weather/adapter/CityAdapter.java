package com.willchen.weather.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.willchen.weather.R;
import com.willchen.weather.config.Constant;

import java.util.List;
import java.util.Set;


public class CityAdapter extends BaseAdapter {


    private Context mcontext;
    private List<String> mList;
    private LayoutInflater layoutInflater;
    private Set<Integer> selectPosition;
    private int mMode;


    public CityAdapter(Context context, List<String> cityList) {
        mcontext = context;
        mList = cityList;
        layoutInflater = LayoutInflater.from(context);
    }


    public void setSelectPosition(Set<Integer> selectPosition, int mode) {
        this.selectPosition = selectPosition;
        mMode = mode;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = layoutInflater.inflate(R.layout.city_search_lv_item, null);
            holder.tv = (TextView) view.findViewById(R.id.tv_city_item);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }




        if (mMode == Constant.MODE_MORE) {
            holder.tv.setText(mList.get(position));
            if (selectPosition.contains(position)) {
                view.setBackgroundResource(R.color.lv_item_background_selected);
                holder.tv.setTextColor(Color.WHITE);
            } else {
                view.setBackgroundResource(R.color.lv_item_background_normal);
                holder.tv.setTextColor(Color.parseColor("#6c6c6c"));
            }
        } else if(mMode==Constant.MODE_WEATHER){
            String[] cityName=mList.get(position).split(",");
            Log.d("city",cityName[0]);
            holder.tv.setText(cityName[0]);
            holder.tv.setTextSize(20);
            holder.tv.setTextColor(Color.parseColor("#ffffff"));
        }else {
            holder.tv.setText(mList.get(position));
            view.setBackgroundResource(R.color.lv_item_background_normal);
            holder.tv.setTextColor(Color.parseColor("#6c6c6c"));
        }


        return view;
    }

    final class Holder {
        TextView tv;
    }


}
