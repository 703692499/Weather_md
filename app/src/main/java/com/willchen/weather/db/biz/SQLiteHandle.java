package com.willchen.weather.db.biz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.willchen.weather.config.Constant;
import com.willchen.weather.db.WeatherOpenHelper;
import com.willchen.weather.entity.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SQLiteHandle {

    private SQLiteDatabase mDb;

    public SQLiteHandle(Context context) {
        WeatherOpenHelper weatherOpenHelper = new WeatherOpenHelper(context, Constant.SQLITE_NAME, null, Constant.SQLITE_VERSION);
        mDb = weatherOpenHelper.getWritableDatabase();

    }

    public void saveCity(City city) {
        if (city != null) {
            mDb.beginTransaction();
            try {
                Cursor cursor = mDb.rawQuery("select * from " + Constant.TABLE_CITY + " where city_name like ?", new String[]{city.getCityName()});
                if (cursor.getCount() == 0) {
                    mDb.execSQL("insert into " + Constant.TABLE_CITY + "(city_name) values(?)"
                            , new Object[]{city.getCityName()});
                }
                mDb.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDb.endTransaction();
            }
        }
    }


    public List<String> getCity(String searchCity) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = mDb.query(Constant.TABLE_CITY, new String[]{"city_name"}, "city_name like ?", new String[]{searchCity + "%"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("city_name")));
            } while (cursor.moveToNext());
        }
        return list;
    }


    public boolean queryCity(String city) {
        Cursor cursor = mDb.rawQuery("select * from " + Constant.TABLE_CITY + " where city_name like ?", new String[]{city + "%"});
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;

    }

    /*
    * 搜索历史
    * */
    public Cursor queryHistory() {
        Cursor cursor = mDb.query(Constant.TABLE_CITY_HISTORY, new String[]{"id", "city_name"}, null, null, null, null, "id desc");
        return cursor;
    }


    /*
    * 保存城市历史
    * */
    public void saveHistory(String cityHistory) {

        Cursor cursor = mDb.rawQuery("select * from " + Constant.TABLE_CITY_HISTORY, null);
        mDb.beginTransaction();
        try {
            if (cursor.getCount() > 0) {
                cursor = mDb.rawQuery("select * from " + Constant.TABLE_CITY_HISTORY + " where city_name like ?", new String[]{cityHistory});

                if (cursor.getCount() > 0) {
                    mDb.execSQL("delete from " + Constant.TABLE_CITY_HISTORY + " where city_name like  ?", new String[]{cityHistory});
                }
            }
            mDb.execSQL("insert into " + Constant.TABLE_CITY_HISTORY + "(city_name) values(?)",
                    new Object[]{cityHistory});
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDb.endTransaction();
        }
    }


    /*
    * 保存更新时间
    * */
    public void saveUpdateTime(String cityName, String updateTime) {
        Cursor cursor = mDb.rawQuery("select * from " + Constant.TABLE_CITY_HISTORY + " where city_name like ?", new String[]{cityName + "%"});
        if (cursor.getCount() > 0) {
            mDb.beginTransaction();
            try {
                mDb.execSQL("update " + Constant.TABLE_CITY_HISTORY + " set time = ?" + " where city_name like ?", new Object[]{updateTime, cityName + "%"});
                mDb.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDb.endTransaction();
            }
        }
    }


    /*
    * 保存今天天气
    * */
    public void saveNowWeather(String cityName, String nowWeather) {
        Cursor cursor = mDb.rawQuery("select * from " + Constant.TABLE_CITY_HISTORY + " where city_name like ?", new String[]{cityName + "%"});
        if (cursor.getCount() > 0) {
            mDb.beginTransaction();
            try {
                mDb.execSQL("update " + Constant.TABLE_CITY_HISTORY + " set weather_now = ?" + " where city_name like ?", new Object[]{nowWeather, cityName + "%"});
                mDb.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDb.endTransaction();
            }
        }
    }

    /*
    * 保存多天天气
    * */
    public void saveDaliyWeather(String cityName, String dailyWeather) {

        Cursor cursor = mDb.rawQuery("select * from " + Constant.TABLE_CITY_HISTORY + " where city_name like  ?", new String[]{cityName + "%"});
        if (cursor.getCount() > 0) {
            mDb.beginTransaction();
            try {
                mDb.execSQL("update " + Constant.TABLE_CITY_HISTORY + " set daily = ?" + " where city_name like ?", new Object[]{dailyWeather, cityName + "%"});
                mDb.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDb.endTransaction();
            }
        }
    }

    /*
    * 保存生活指数
    * */
    public void saveLife(String cityName, String life) {

        Cursor cursor = mDb.rawQuery("select * from " + Constant.TABLE_CITY_HISTORY + " where city_name like  ?", new String[]{cityName + "%"});
        if (cursor.getCount() > 0) {
            mDb.beginTransaction();
            try {
                mDb.execSQL("update " + Constant.TABLE_CITY_HISTORY + " set life = ? " + " where city_name like ?", new Object[]{life, cityName + "%"});
                mDb.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDb.endTransaction();
            }
        }
    }


    /*
    * 获取天气
    * */
    public List<String> getWeather(String cityName) {
        List<String> list = new ArrayList<>();
        Cursor cursor = mDb.query(Constant.TABLE_CITY_HISTORY, new String[]{"city_name", "weather_now", "daily", "life"}, "city_name like ?", new String[]{cityName+"%"}, null, null, null);
        if (cursor.moveToFirst()) {
            list.add(cursor.getString(cursor.getColumnIndex("weather_now")));
            list.add(cursor.getString(cursor.getColumnIndex("daily")));
            list.add(cursor.getString(cursor.getColumnIndex("life")));
            Log.d("qwe",list.size()+"");
        }
        return list;
    }


    /*
    * 获取更新时间
    * */
    public String getUpdateTime(String cityName) {
        String updateTime = "最近更新: ";
        Cursor cursor = mDb.query(Constant.TABLE_CITY_HISTORY, new String[]{"city_name", "time"}, "city_name like ?", new String[]{cityName+"%"}, null, null, null);
        if (cursor.moveToFirst()) {
            updateTime += cursor.getString(cursor.getColumnIndex("time"));
            Log.d("ss",updateTime+"          "+cityName);
        }
        return updateTime;
    }


    public List<String> getCityHistory() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = mDb.query(Constant.TABLE_CITY_HISTORY, new String[]{"city_name"}, null, null, null, null, "id desc");
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("city_name")));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void deleteHistory(Set<Integer> position, List<String> listHistory) {
        mDb.beginTransaction();
        try {
            if (position.size() == listHistory.size()) {
                mDb.execSQL("delete from " + Constant.TABLE_CITY_HISTORY);
            } else if (position.size() > 0) {
                for (int i : position) {
                    mDb.execSQL("delete from " + Constant.TABLE_CITY_HISTORY + " where city_name like ?", new String[]{listHistory.get(i)});
                }
            }
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDb.endTransaction();
        }
    }


}