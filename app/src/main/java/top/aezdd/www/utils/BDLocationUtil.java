package top.aezdd.www.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.aezdd.www.ant_movies.TestActivity;

/**
 * Created by jianzhou.liu on 2017/4/1.
 */

public class BDLocationUtil {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public Context mContext;
    public Map<String,String> map ;
    public BDLocationUtil(Context context, TextView textView){
        mContext = context;
        map = new HashMap<>();
    }


    public void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);

    }
    /*开启定位*/
    public Map<String,String> startLocal(){
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        mLocationClient.start();
        return map;
    }
    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果

            map.put("time",location.getTime());//获取定位时间
            map.put("type",location.getLocType()+"");//获取类型类型
            map.put("latitude",location.getLatitude()+"");//获取纬度信息
            map.put("lontitude",location.getLongitude()+"");//获取经度信息
            map.put("radius",location.getRadius()+"");//获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                //gps定位结果
                map.put("city",location.getCity());//获取定位城市
                map.put("address",location.getAddrStr());//获取地址信息
                map.put("operationers",location.getOperators()+"");//获取运营商信息
                Log.i("bdlocal","gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                map.put("city",location.getCity());//获取定位城市
                map.put("address",location.getAddrStr());//获取地址信息
                map.put("operationers",location.getOperators()+"");//获取运营商信息
                Log.i("bdlocal","网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                Log.i("bdlocal","离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                Log.i("bdlocal","服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                Log.i("bdlocal","网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                Log.i("bdlocal","无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.e("info", s + " " + i);
        }
    }

}
