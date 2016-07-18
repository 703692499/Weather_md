package com.willchen.weather.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.willchen.weather.R;
import com.willchen.weather.config.Constant;
import com.willchen.weather.db.biz.SQLiteHandle;
import com.willchen.weather.https.HttpCity;
import com.willchen.weather.https.HttpWeather;
import com.willchen.weather.utils.LocationUtil;
import com.willchen.weather.utils.WeatherIconUtil;
import com.willchen.weather.widget.AppWidget;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateTimeService extends Service {

    private RemoteViews remoteViews;
    private static final int UPDATE = 1;
    private HttpWeather httpWeather;
    private HttpCity httpSend;
    private String cityName;
    private SQLiteHandle sqLiteHandle;
    private WeatherIconUtil iconUtil;
    private Calendar calendar;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    getGpsLocation();
                    break;
                case Constant.GPS:
                    if (msg.obj != null) {
                        String gpsLocation = msg.obj.toString();
                        httpSend.sendHttpRequest(sqLiteHandle, Constant.SEARCH_CITY_URL + gpsLocation, handler, Constant.GPS);
                    } else {
                        remoteViews.setTextViewText(R.id.tv_widget_city, "定位失败,请检查设置");
                        ComponentName componentName = new ComponentName(getApplicationContext(), AppWidget.class);
                        AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(componentName, remoteViews);
                    }
                    break;
                case Constant.GPS_JSON:
                    if (!msg.obj.toString().equals("")) {
                        cityName = msg.obj.toString().substring(0, msg.obj.toString().indexOf(","));

                    }
                    break;
                case Constant.SEARCH_NOW:
                    setWeatherNow(msg.obj.toString(), Constant.RESULT_HTTP);
                    break;
                case Constant.NOT_CONNECT:
                    Toast.makeText(UpdateTimeService.this, "网络未连接,请检查设置!", Toast.LENGTH_SHORT).show();
                    Log.d("service","1");
                    break;
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        sqLiteHandle = new SQLiteHandle(getApplicationContext());
        iconUtil = new WeatherIconUtil();
        httpWeather = new HttpWeather();
        httpSend = new HttpCity();
        remoteViews = new RemoteViews(getApplication().getPackageName(), R.layout.widget);

        Intent updateIntent = new Intent("update_weather");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), 0, updateIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.ibtn_widget_update, pendingIntent);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_weather");
        registerReceiver(broadcastReceiver, intentFilter);


        getGpsLocation();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = UPDATE;
                handler.sendMessage(msg);
            }
        }, 1, getTime());
    }


    /*
    * 设置今天天气
    * */
    private void setWeatherNow(String msg, int mode) {
        String[] weather = msg.split(",");
        if (mode == Constant.RESULT_HTTP) {
            sqLiteHandle.saveNowWeather(cityName, msg);
            calendar = Calendar.getInstance();
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
        remoteViews.setTextViewText(R.id.tv_widget_city, cityName);
        remoteViews.setTextViewText(R.id.tv_widget_word, weather[2]);
        remoteViews.setImageViewResource(R.id.iv_widget_icon, iconUtil.getIconView(Integer.parseInt(weather[1])));
        remoteViews.setTextViewText(R.id.tv_widget_text, weather[0]);
        remoteViews.setTextViewText(R.id.tv_widget_feel, weather[3]);
        remoteViews.setTextViewText(R.id.tv_widget_humidity, weather[5]);
        remoteViews.setTextViewText(R.id.tv_widget_update, sqLiteHandle.getUpdateTime(cityName));

        remoteViews.setTextViewText(R.id.tv_widget_wind, weather[9].substring(0, weather[9].indexOf("公")) + "km/h");
        remoteViews.setTextViewText(R.id.tv_widget_time, weather[13]);
        remoteViews.setViewVisibility(R.id.iv_widget_feel, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.iv_widget_huimidity, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.iv_widget_wind, View.VISIBLE);

        ComponentName componentName = new ComponentName(getApplicationContext(), AppWidget.class);
        AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(componentName, remoteViews);

     //   Toast.makeText(UpdateTimeService.this, "同步天气成功", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


    private void getGpsLocation() {
        LocationUtil locationUtil = new LocationUtil(getApplicationContext(), handler);
    }


    public long getTime() {
        int time;
        SharedPreferences share = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        time = share.getInt("time", 10);
        if (time == 10) {
            time = 10 * 1000 * 60;
        } else if (time == 60) {
            time = 3600 * 1000;
        } else if (time == 12) {
            time = 3600 * 1000 * 12;
        }
        return time;
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("update_weather")) {
                getGpsLocation();
                Toast.makeText(UpdateTimeService.this, "正在同步天气", Toast.LENGTH_SHORT).show();
            }
        }


    };


}
