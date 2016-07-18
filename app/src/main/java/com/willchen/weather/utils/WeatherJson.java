package com.willchen.weather.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.willchen.weather.config.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/1.
 */
public class WeatherJson {

    public void nowWeatherJson(final String response, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!response.equals("") && response != null) {
                    try {
                        String nowResults = "";
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONArray("results");
                        JSONObject resultsJSONObject = results.getJSONObject(0);
                        if (resultsJSONObject.has("now")) {
                            JSONObject nowObject = new JSONObject(resultsJSONObject.getString("now"));
                            nowResults += nowObject.has("text") ? nowObject.getString("text") + "," : "无,";                 //天气现象文字
                            nowResults += nowObject.has("code") ? nowObject.getString("code") + "," : "99,";                        //天气现象代码
                            nowResults += nowObject.has("temperature") ? nowObject.getString("temperature") + "°," : "无,";                   //温度，单位为c摄氏度或f华氏度
                            nowResults += nowObject.has("feels_like") ? nowObject.getString("feels_like") + "°," : "无,";                     //体感温度，单位为c摄氏度或f华氏度
                            nowResults += nowObject.has("pressure") ? nowObject.getString("pressure") + "百帕," : "无,";                       //气压，单位为mb百帕或in英寸
                            nowResults += nowObject.has("humidity") ? nowObject.getString("humidity") + "%," : "无,";                       //相对湿度，0~100，单位为百分比
                            nowResults += nowObject.has("visibility") ? nowObject.getString("visibility") + "公里," : "无,";                      //能见度，单位为km公里或mi英里
                            nowResults += nowObject.has("wind_direction") ? nowObject.getString("wind_direction") + "," : "无,";                  //风向文字
                            nowResults += nowObject.has("wind_direction_degree") ? nowObject.getString("wind_direction_degree") + "," : "无,";           //风向角度，范围0~360，0为正北，90为正东，180为正南，270为正西
                            nowResults += nowObject.has("wind_speed") ? nowObject.getString("wind_speed") + "公里/小时," : "无,";                       //风速，单位为km/h公里每小时或mph英里每小时
                            nowResults += nowObject.has("wind_scale") ? nowObject.getString("wind_scale") + "," : "无,";                        //风力等级
                            nowResults += nowObject.has("clouds") ? nowObject.getString("clouds") + "," : "无,";                             //云量
                            nowResults += nowObject.has("dew_point") ? nowObject.getString("dew_point") + "," : "无,";                          //露点温度--------共13个数据
                        }
                        String dateTime = resultsJSONObject.getString("last_update");
                        nowResults += "今天 " + dateTime.substring(0, dateTime.indexOf("T")).replace("-", "/");

                        Message message = new Message();
                        message.what = Constant.SEARCH_NOW;
                        message.obj = nowResults;
                        handler.sendMessage(message);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    /*
    * 解析5天天气数据
    * */
    public void dailyWeatherJson(final String response, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!response.equals("") && response != null) {
                    try {
                        int i = 0;
                        String dailyResults = "";
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONArray("results");
                        JSONObject resultsJSONObject = results.getJSONObject(0);
                        JSONArray dailyArray = resultsJSONObject.getJSONArray("daily");
                        while (!dailyArray.isNull(i)) {
                            JSONObject date = dailyArray.getJSONObject(i);
                            date.getString("date");
                            dailyResults += date.has("date") ? date.getString("date") + "," : "无,";
                            dailyResults += date.has("text_day") ? date.getString("text_day") + "," : "无,";
                            dailyResults += date.has("code_day") ? date.getString("code_day") + "," : "99,";
                            dailyResults += date.has("text_night") ? date.getString("text_night") + "," : "无,";
                            dailyResults += date.has("code_night") ? date.getString("code_night") + "," : "无,";
                            dailyResults += date.has("high") ? date.getString("high") + "," : "无,";
                            dailyResults += date.has("low") ? date.getString("low") + "," : "无,";
                            dailyResults += date.has("precip") ? date.getString("precip") + "," : "无,";
                            dailyResults += date.has("wind_direction") ? date.getString("wind_direction") + "," : "无,";
                            dailyResults += date.has("wind_direction_degree") ? date.getString("wind_direction_degree") + "," : "无,";
                            dailyResults += date.has("wind_speed") ? date.getString("wind_speed") + "," : "无,";
                            dailyResults += date.has("wind_scale") ? date.getString("wind_scale") + "," : "无,";
                            dailyResults += "/";
                            i++;
                        }
                        Message message = new Message();
                        message.what = Constant.SEARCH_DAILY;
                        message.obj = dailyResults;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void suggestionJson(final String response, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!response.equals("") && response != null) {
                    try {
                        String suggestionResults = "";
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONArray("results");
                        JSONObject resultsJSONObject = results.getJSONObject(0);
                        JSONObject suggestionObject = resultsJSONObject.getJSONObject("suggestion");
                        suggestionResults += suggestionObject.has("uv") ? suggestionObject.getJSONObject("uv").getString("brief") + "," : "无,";        //紫外线
                        suggestionResults += suggestionObject.has("dressing") ? suggestionObject.getJSONObject("dressing").getString("brief") + "," : "无,";        //穿衣
                        suggestionResults += suggestionObject.has("flu") ? suggestionObject.getJSONObject("flu").getString("brief") + "," : "无,";        //感冒
                        suggestionResults += suggestionObject.has("sport") ? suggestionObject.getJSONObject("sport").getString("brief") + "," : "无,";        //运动
                        suggestionResults += suggestionObject.has("travel") ? suggestionObject.getJSONObject("travel").getString("brief") + "," : "无,";        //旅游
                        suggestionResults += suggestionObject.has("car_washing") ? suggestionObject.getJSONObject("car_washing").getString("brief") + "," : "无,";        //洗车
                        Message message = new Message();
                        message.what = Constant.SEARCH_SUGGESTION;
                        message.obj = suggestionResults;
                        handler.sendMessage(message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /*
    * 日出日落
    * */
    public void sunOutOffJson(final String response, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!response.equals("") && response != null) {
                    try {
                        String sunResults = "";
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONArray("results");
                        JSONObject resultsJSONObject = results.getJSONObject(0);
                        JSONArray sunArray = resultsJSONObject.getJSONArray("sun");
                        JSONObject sunObject = sunArray.getJSONObject(0);
                        sunResults += sunObject.has("sunrise") ? sunObject.getString("sunrise") + "," : ",";
                        sunResults += sunObject.has("sunset") ? sunObject.getString("sunset") + "," : ",";
                        Message message = new Message();
                        message.what = Constant.SEARCH_SUNOUTOFF;
                        message.obj = sunResults;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void allWeatherJson(final String response, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (!response.equals("") && response != null) {
                    try {
                        String nowResults = "";
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONArray("HeWeather data service 3.0");
                        JSONObject resultsJSONObject = results.getJSONObject(0);
                        if (resultsJSONObject.has("now")) {
                            JSONObject nowObject = new JSONObject(resultsJSONObject.getString("now"));
                            JSONObject TextObject = nowObject.getJSONObject("cond");
                            nowResults += TextObject.has("txt") ? TextObject.getString("txt") + "," : "无,";                 //天气现象文字
                            nowResults += TextObject.has("code") ? TextObject.getString("code") + "," : "99,";                        //天气现象代码
                            nowResults += nowObject.has("tmp") ? nowObject.getString("tmp") + "°," : "无,";                   //温度，单位为c摄氏度或f华氏度
                            nowResults += nowObject.has("fl") ? nowObject.getString("fl") + "°," : "无,";                     //体感温度，单位为c摄氏度或f华氏度
                            nowResults += nowObject.has("pres") ? nowObject.getString("pres") + "百帕," : "无,";                       //气压，单位为mb百帕或in英寸
                            nowResults += nowObject.has("hum") ? nowObject.getString("hum") + "%," : "无,";                       //相对湿度，0~100，单位为百分比
                            nowResults += nowObject.has("vis") ? nowObject.getString("vis") + "公里," : "无,";                      //能见度，单位为km公里或mi英里
                            if (!nowObject.has("wind")) {
                                nowResults += "无,无,无,无";
                            } else {
                                JSONObject WindObject = nowObject.getJSONObject("wind");     //风向文字  + //风向角度，范围0~360，0为正北，90为正东，180为正南，270为正西
                                nowResults += WindObject.getString("dir") + "," +
                                        WindObject.getString("deg") + ","
                                        + WindObject.getString("spd") + " Kmph," +
                                        WindObject.getString("sc") + ",";
                            }
                        }
                        String dateTime = resultsJSONObject.getJSONObject("basic").getJSONObject("update").getString("loc");
                        nowResults += "今天 " + dateTime.substring(0, dateTime.indexOf(" ")).replace("-", "/") + ",";

                        String suggestionResult = "";
                        if (resultsJSONObject.has("suggestion")) {
                            JSONObject suggestion = resultsJSONObject.getJSONObject("suggestion");
                            suggestionResult += "_" + suggestion.getJSONObject("uv").getString("brf") + ",";
                            suggestionResult += suggestion.getJSONObject("drsg").getString("brf") + ",";
                            suggestionResult += suggestion.getJSONObject("flu").getString("brf") + ",";
                            suggestionResult += suggestion.getJSONObject("sport").getString("brf") + ",";
                            suggestionResult += suggestion.getJSONObject("trav").getString("brf") + ",";
                            suggestionResult += suggestion.getJSONObject("cw").getString("brf");
                        }

                        String dailyResult = "_";
                        if (resultsJSONObject.has("daily_forecast")) {
                            JSONObject dailyObject[] = new JSONObject[5];
                            for (int i = 0; i < 5; i++) {
                                dailyObject[i] = resultsJSONObject.getJSONArray("daily_forecast").getJSONObject(i);
                                dailyResult += dailyObject[i].getString("date").substring(5) + ",";
                                JSONObject condObject = dailyObject[i].getJSONObject("cond");
                                dailyResult += condObject.getString("code_d") + "," + condObject.getString("txt_d") + "," + condObject.getString("txt_n") + ",";
                                dailyResult += dailyObject[i].getJSONObject("tmp").getString("max") + "," + dailyObject[i].getJSONObject("tmp").getString("min") + ",";
                                dailyResult += dailyObject[i].getString("pop") + "%,";
                                dailyResult += dailyObject[i].getString("pres") + "Pa,";        //气压
                                dailyResult += dailyObject[i].getString("vis") + "Km" + "。";    //能见度
                            }
                        }
                        Log.d("daily", dailyResult);

                        Message message = new Message();
                        message.what = Constant.SEARCH_NOW;
                        message.obj = nowResults + suggestionResult + dailyResult;
                        handler.sendMessage(message);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();

    }
}