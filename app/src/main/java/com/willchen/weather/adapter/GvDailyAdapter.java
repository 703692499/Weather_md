package com.willchen.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.willchen.weather.R;

import java.util.List;

public class GvDailyAdapter extends BaseAdapter {


    private List<String> mDateList;
    private List<Integer> mIconList;
    private LayoutInflater inflater;


    public GvDailyAdapter(Context context, List<String> dateList, List<Integer> iconList) {
        mDateList = dateList;
        mIconList = iconList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mDateList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.weather_show_daily, null);
            viewHolder = new ViewHolder();
            viewHolder.mDate = (TextView) view.findViewById(R.id.tv_daily_date);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.iv_daily_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mDate.setText(mDateList.get(i));
        viewHolder.mIcon.setBackgroundResource(mIconList.get(i));
        return view;
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mDate;
    }
}
