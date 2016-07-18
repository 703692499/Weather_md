package com.willchen.weather.https;

import android.os.Handler;

import com.willchen.weather.config.Constant;
import com.willchen.weather.utils.WeatherJson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpWeather {


    public void sendHttpRequestWeather(final String url, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                WeatherJson weatherJson=new WeatherJson();
                try {
                    URL address = new URL(url);
                    connection = (HttpURLConnection) address.openConnection();
                    InputStreamReader in = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(in);
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    weatherJson.allWeatherJson(response.toString(),handler);

                } catch (Exception e) {
                    handler.sendEmptyMessage(Constant.NOT_CONNECT);
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
