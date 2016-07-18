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
                        JSONArray results = jsonObject.getJSONArray("results");
                        while (!results.isNull(i)) {
                            JSONObject cityObject = results.getJSONObject(i);
                            City city = new City();
                            city.setCityId(cityObject.getString("id"));
                            bigCity = cityObject.getString("path");
                            city.setCityName(bigCity);
                            db.saveCity(city);
                            if (!cityName.contains(bigCity)) {
                                cityName.add(bigCity);
                            }
                            i++;
                        }
                        if (mode == Constant.GPS) {
                            Message message = new Message();
                            message.what = Constant.GPS_JSON;
                            message.obj = bigCity;
                            handler.sendMessage(message);
                        } else if (mode == 0) {
                            Message message = new Message();
                            message.what = Constant.CITY_JSON;
                            message.obj = cityName;
                            handler.sendMessage(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


}

