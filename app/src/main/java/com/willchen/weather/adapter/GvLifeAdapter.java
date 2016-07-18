package com.willchen.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.willchen.weather.R;

import java.util.Arrays;
import java.util.List;

public class GvLifeAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mBriefList;
    private List<Integer> mIconList;
    private List<String> mTitleList;
    private LayoutInflater inflater;

    private LinearLayout.LayoutParams params;

    public GvLifeAdapter(Context context, List<String> briefList) {
        mBriefList = briefList;
        mContext = context;
        inflater = LayoutInflater.from(context);
        mTitleList = Arrays.asList("紫外线", "穿衣", "感冒", "运动", "旅行", "洗车");
        mIconList = Arrays.asList(R.drawable.iv_weather_life_uv,
                R.drawable.iv_weather_life_dressing,
                R.drawable.iv_weather_life_flu,
                R.drawable.iv_weather_life_sport,
                R.drawable.iv_weather_life_travel,
                R.drawable.iv_weather_life_carwash);


    }


    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public Object getItem(int i) {
        return mTitleList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.weather_show_life, null);
            viewHolder = new ViewHolder();
            viewHolder.mBrief = (TextView) view.findViewById(R.id.tv_weather_normal_text);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.iv_weather_life_icon);
            viewHolder.mTitle = (TextView) view.findViewById(R.id.tv_weather_normal_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTitle.setText(mTitleList.get(i));
        viewHolder.mBrief.setText(mBriefList.get(i));
        viewHolder.mIcon.setBackgroundResource(mIconList.get(i));
        return view;
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mBrief;
        TextView mTitle;

    }
}
