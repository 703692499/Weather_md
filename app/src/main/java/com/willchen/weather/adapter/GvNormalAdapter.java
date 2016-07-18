package com.willchen.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.willchen.weather.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 */
public class GvNormalAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mTextfList;
    private List<Integer> mIconList;
    private List<String> mTitleList;
    private LayoutInflater inflater;


    public GvNormalAdapter(Context context, List<String> textList) {
        mTextfList = textList;
        inflater = LayoutInflater.from(context);
        mTitleList = Arrays.asList("可见度", "气压", "风力级别", "风向");
        mIconList = Arrays.asList(R.drawable.iv_weather_see,R.drawable.iv_weather_pressure,R.drawable.iv_weather_wind_scale,
                R.drawable.iv_weather_wind);

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
            view = inflater.inflate(R.layout.weather_show_normal, null);
            viewHolder = new ViewHolder();
            viewHolder.mText = (TextView) view.findViewById(R.id.tv_weather_normal_text);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.iv_widget_icon);
            viewHolder.mTitle = (TextView) view.findViewById(R.id.tv_weather_normal_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTitle.setText(mTitleList.get(i));
        viewHolder.mText.setText(mTextfList.get(i));
        viewHolder.mIcon.setBackgroundResource(mIconList.get(i));
        return view;
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mText;
        TextView mTitle;

    }
}
