package com.willchen.weather.utils;

import com.willchen.weather.R;

/**
 * Created by Administrator on 2016/6/2.
 */
public class WeatherIconUtil {

    public int getIconView(final int mCode) {
        int iconInt;
        if (mCode == 100 || mCode == 900 || mCode == 38) {                     //晴天
            iconInt = R.drawable.iv_weather_sunny;
        } else if (mCode == 1 || mCode == 3) {              //夜间晴天
            iconInt = R.drawable.iv_weather_night_sun;
        } else if (mCode == 103) {                        //白天多云
            iconInt = R.drawable.iv_weather_sun_cloud;
        } else if (mCode == -1) {                        //晴天多云
            iconInt = R.drawable.iv_weather_night_cloud;
        } else if (mCode == 101) {                        //白天大多云
            iconInt = R.drawable.iv_weather_cloudy;
        } else if (mCode == 102) {                        //夜间大多云
            iconInt = R.drawable.iv_weather_cloudy;
        } else if (mCode == 104) {                        //阴天
            iconInt = R.drawable.iv_weather_cloudy;
        } else if (mCode == 300 || mCode == 301) {                        //阵雨
            iconInt = R.drawable.iv_weather_big_rain;
        } else if (mCode == 302 || mCode == 303) {                        //雷阵雨
            iconInt = R.drawable.iv_weather_thunder;
        } else if (mCode == 304) {                        //雷阵雨带冰雹
            iconInt = R.drawable.iv_weather_thunder;
        } else if (mCode == 305) {                        //小雨
            iconInt = R.drawable.iv_weather_light_rain;
        } else if (mCode == 306) {                        //中雨
            iconInt = R.drawable.iv_weather_big_rain;
        } else if (mCode == 307) {                        //大雨
            iconInt = R.drawable.iv_weather_big_rain;
        } else if (mCode == 308 || mCode == 309 || mCode == 310 || mCode == 311 || mCode == 312) {      //暴雨、大暴雨、特大暴雨
            iconInt = R.drawable.iv_weather_light_rain;
        } else if (mCode == 313) {                        //冻雨
            iconInt = R.drawable.iv_weather_rain_and_snow;
        } else if (mCode == 404 || mCode == 406) {                        //雨夹雪
            iconInt = R.drawable.iv_weather_rain_and_snow;
        } else if (mCode == 400 || mCode == 401 || mCode == 407) {      //小雪
            iconInt = R.drawable.iv_weather__snow;
        } else if (mCode == 402) {                     //大雪
            iconInt = R.drawable.iv_weather__snow;
        } else if (mCode == 503 || mCode == 504 || mCode == 506) {                        //浮尘
            iconInt = R.drawable.iv_weather_storm;
        } else if (mCode == 507 || mCode == 508) {     //风暴
            iconInt = R.drawable.iv_weather_storm;
        } else if (mCode == 500 || mCode == 501) {                           //雾
            iconInt = R.drawable.iv_weather_foggy;
        } else if (mCode == 502) {                           //霾
            iconInt = R.drawable.iv_weather_foggy;
        } else if (mCode == 200 || mCode == 201 || mCode == 202 || mCode == 203 || mCode == 204) {                           //风
            iconInt = R.drawable.iv_weather_wind;
        } else if (mCode == 205 || mCode == 206 || mCode == 207 || mCode == 208 || mCode == 209 || mCode == 210) {                           //大风
            iconInt = R.drawable.iv_weather_wind_scale;
        } else if (mCode == 211 || mCode == 212 || mCode == 213) {                           //飓风
            iconInt = R.drawable.iv_weather_wind_scale;
        } else if (mCode == 901) {                           //冷
            iconInt = R.drawable.iv_weather_light_rain;
        } else {
            iconInt = R.drawable.iv_weather_unknow;
        }
        // handler.sendMessage(iconInt);
        return iconInt;

    }

    public int getDarkIcon(final int mCode) {
        int iconInt;
        if (mCode == 100 || mCode == 900 || mCode == 38) {                     //晴天
            iconInt = R.drawable.iv_weather_sunny_dark;
        } else if (mCode == 1 || mCode == 3) {              //夜间晴天
            iconInt = R.drawable.iv_weather_night_sun_dark;
        } else if (mCode == 103) {                        //白天多云
            iconInt = R.drawable.iv_weather_sun_cloud_dark;
        } else if (mCode == -1) {                        //晴天多云
            iconInt = R.drawable.iv_weather_night_cloud_dark;
        } else if (mCode == 101) {                        //白天大多云
            iconInt = R.drawable.iv_weather_cloudy_dark;
        } else if (mCode == 102) {                        //夜间大多云
            iconInt = R.drawable.iv_weather_cloudy_dark;
        } else if (mCode == 104) {                        //阴天
            iconInt = R.drawable.iv_weather_cloudy_dark;
        } else if (mCode == 300 || mCode == 301) {                        //阵雨
            iconInt = R.drawable.iv_weather_big_rain_dark;
        } else if (mCode == 302 || mCode == 303) {                        //雷阵雨
            iconInt = R.drawable.iv_weather_thunder_dark;
        } else if (mCode == 304) {                        //雷阵雨带冰雹
            iconInt = R.drawable.iv_weather_thunder_dark;
        } else if (mCode == 305) {                        //小雨
            iconInt = R.drawable.iv_weather_light_rain_dark;
        } else if (mCode == 306) {                        //中雨
            iconInt = R.drawable.iv_weather_big_rain_dark;
        } else if (mCode == 307) {                        //大雨
            iconInt = R.drawable.iv_weather_big_rain_dark;
        } else if (mCode == 308 || mCode == 309 || mCode == 310 || mCode == 311 || mCode == 312) {      //暴雨、大暴雨、特大暴雨
            iconInt = R.drawable.iv_weather_light_rain_dark;
        } else if (mCode == 313) {                        //冻雨
            iconInt = R.drawable.iv_weather_rain_and_snow_dark;
        } else if (mCode == 404 || mCode == 406) {                        //雨夹雪
            iconInt = R.drawable.iv_weather_rain_and_snow_dark;
        } else if (mCode == 400 || mCode == 401 || mCode == 407) {      //小雪
            iconInt = R.drawable.iv_weather__snow_dark;
        } else if (mCode == 402) {                     //大雪
            iconInt = R.drawable.iv_weather__snow_dark;
        } else if (mCode == 503 || mCode == 504 || mCode == 506) {                        //浮尘
            iconInt = R.drawable.iv_weather_storm_dark;
        } else if (mCode == 507 || mCode == 508) {     //风暴
            iconInt = R.drawable.iv_weather_storm_dark;
        } else if (mCode == 500 || mCode == 501) {                           //雾
            iconInt = R.drawable.iv_weather_foggy_dark;
        } else if (mCode == 502) {                           //霾
            iconInt = R.drawable.iv_weather_foggy_dark;
        } else if (mCode == 200 || mCode == 201 || mCode == 202 || mCode == 203 || mCode == 204) {                           //风
            iconInt = R.drawable.iv_weather_wind_dark;
        } else if (mCode == 205 || mCode == 206 || mCode == 207 || mCode == 208 || mCode == 209 || mCode == 210) {                           //大风
            iconInt = R.drawable.iv_weather_wind_scale_dark;
        } else if (mCode == 211 || mCode == 212 || mCode == 213) {                           //飓风
            iconInt = R.drawable.iv_weather_wind_scale_dark;
        } else if (mCode == 901) {                           //冷
            iconInt = R.drawable.iv_weather_light_rain_dark;
        } else {
            iconInt = R.drawable.iv_weather_unknow_dark;
        }
        // handler.sendMessage(iconInt);
        return iconInt;

    }


}
