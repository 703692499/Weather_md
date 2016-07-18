package com.willchen.weather.https;

import android.os.Handler;

import com.willchen.weather.config.Constant;
import com.willchen.weather.db.biz.SQLiteHandle;
import com.willchen.weather.utils.CityJsonUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpCity {

    public void sendHttpRequest(final SQLiteHandle sqliteHandle, final String url, final Handler handler, final int mode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                CityJsonUtil jsonUtil=new CityJsonUtil();
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
                    jsonUtil.handleCityJson(sqliteHandle, response.toString(), handler,mode);

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
