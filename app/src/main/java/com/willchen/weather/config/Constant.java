package com.willchen.weather.config;


public class Constant {

    /*
    * 数据库
    * */
    public static final String SQLITE_NAME = "Weather";       //数据库名
    public static final int SQLITE_VERSION = 1;            //数据库版本


    //    /*表名*/
    public static final String TABLE_CITY = "City";
    public static final String TABLE_CITY_HISTORY = "History";


    /*
    * Http
    * */
    public static final int CITY_JSON = 1;           //返回城市列表



    /*
    * 心知天气API
    * */

    public static final String SEARCH_CITY_URL = "https://api.thinkpage.cn/v3/location/search.json?key=kn8xwx1xiysxehxg&q=";      //查询城市
    public static final int SEARCH_NOW = 10;
    public static final int SEARCH_DAILY = 20;
    public static final int SEARCH_SUGGESTION = 30;
    public static final int SEARCH_SUNOUTOFF=40;



    /*
    * 查询实况天气
    * */
    public static final String SEARCH_WEATHER_NOW = "https://api.thinkpage.cn/v3/weather/now.json?key=kn8xwx1xiysxehxg&location=";               //今日天气
    public static final String SEARCH_WEATHER_DAILY = "https://api.thinkpage.cn/v3/weather/daily.json?key=kn8xwx1xiysxehxg&location=";           //多天天气
    public static final String SEARCH_WEATHER_SUGGESTION = "https://api.thinkpage.cn/v3/life/suggestion.json?key=kn8xwx1xiysxehxg&location=";     //生活指数
    public static final String SEARCH_WEATHER_SUNOUTOFF = "https://api.thinkpage.cn/v3/geo/sun.json?key=kn8xwx1xiysxehxg&location=";              //日出日落

    public static final String SEARCH_WEATHER="https://api.heweather.com/x3/weather?key=701ffb21e08c4b8f8cca83155f60e5a1&city=";            //和风天气


    public static final String SEARCH_WEATHER_LANGUAGE = "&language=zh-Hans";
    public static final String SEARCH_WEATHER_UNIT = "&unit=c";
    public static final String SEARCH_WEATHER_START = "&start=0";

    public static final String SEARCH_WEATHER_DAYS = "&days=5";
    public static final String SEARCH_WEATHER_SUN_DAYS = "&days=1";     //日出日落


    /*
    * Message.what*/
    public static final int NOT_CONNECT = -21;      //网络无连接
    public static final int FROM_SQL=-22;           //从数据库获取天气



    /*
    * 返回选择的城市
    * */
    public static final int RESULT_LIST = 223;
    public static final int RESULT_YOURS = 221;
    public static final int RESULT_HTTP =224;
    public static final int RESULT_UPDATE =220 ;


    /*
    * 长按模式
    * */
    public static final int MODE_MORE = 1024;
    public static final int MODE_SINGLE = 512;
    public static final int MODE_WEATHER = 256;


    /*
    * ToolBar
    * */
    public static final int BTN_BACK = -1;



    /*
    * 获取GPS地址
    * */
    public static final int GPS = 110;
    public static final int GPS_JSON = 111;


}
