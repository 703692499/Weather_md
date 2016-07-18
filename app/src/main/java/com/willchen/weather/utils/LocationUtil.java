package com.willchen.weather.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.willchen.weather.config.Constant;

public class LocationUtil {

    private LocationClient mLocationClient;
    private static LocationUtil locationUtil;
    private LocationListener mLocationListener = new LocationListener();
    private Handler mHandler;

    public LocationUtil(Context context,Handler handler) {
        mLocationClient = new LocationClient(context);
        mHandler=handler;
        mLocationClient.registerLocationListener(mLocationListener);
        initLocation();
    }



    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setOpenGps(true);                                      //设置是否使用gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);                                 //设置是否需要地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }


    public class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String cityName = null;
            if (location.getLocType() == BDLocation.TypeGpsLocation) {//通过GPS定位
                cityName = location.getLatitude() + ":" + location.getLongitude();

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//通过网络连接定位
                cityName = location.getLatitude() + ":" + location.getLongitude();
            }
            Message message=new Message();
            message.what= Constant.GPS;
            message.obj=cityName;
            mHandler.sendMessage(message);
        }

    }

}
