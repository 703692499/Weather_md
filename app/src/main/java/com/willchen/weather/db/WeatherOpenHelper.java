package com.willchen.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WeatherOpenHelper extends SQLiteOpenHelper {

    private static final String CITY = "create table City (" + "id integer primary key autoincrement," + " city_name text )";

    private static final String CITY_HISTORY = "create table History (" + "id integer primary key autoincrement," + "city_name text," + "weather_now text," +
            "life text," + "daily text,"+"time text)";


    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CITY);
        db.execSQL(CITY_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
