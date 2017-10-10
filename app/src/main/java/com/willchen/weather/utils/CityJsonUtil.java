package com.willchen.weather.utils;

import android.os.Handler;
import android.os.Message;

import com.willchen.weather.config.Constant;
import com.willchen.weather.db.biz.SQLiteHandle;
import com.willchen.weather.entity.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.message;


public class CityJsonUtil {


    /*
    * 解析市返回数据
    * */
    public void handleCityJson(final SQLiteHandle db, final String response, final Handler handler, final int mode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (response != null && !response.equals("")) {
                    String bigCity = "";
                    try {
                        int i = 0;
                        List<String> cityName = new ArrayList<String>();

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONArray("HeWeather5");
                        while (!results.isNull(i)) {
                            JSONObject basicObject= results.getJSONObject(i);
                            JSONObject cityObject = basicObject.getJSONObject("basic");
                            City city = new City();
                            city.setCityId(cityObject.getString("id"));
                            bigCity = cityObject.getString("city")+","+cityObject.getString("prov")+","+cityObject.getString("cnty");
                            city.setCityName(bigCity);
                            db.saveCity(city);
                            if (!cityName.contains(bigCity)) {
                                cityName.add(bigCity);
                            }
                            i++;
                        }
                        Message message = new Message();
                        if (mode == Constant.GPS) {
                            message.what = Constant.GPS_JSON;
                            message.obj = bigCity;
                        } else if (mode == 0) {
                            message.what = Constant.CITY_JSON;
                            message.obj = cityName;
                        }
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


}

