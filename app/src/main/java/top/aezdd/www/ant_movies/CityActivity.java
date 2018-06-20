package top.aezdd.www.ant_movies;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.aezdd.www.adapter.CityListAdapter;
import top.aezdd.www.utils.AlertDialogUtil;
import top.aezdd.www.utils.BDLocationUtil;
import top.aezdd.www.utils.DynamicPermission;
import top.aezdd.www.utils.GetJsonDataUtil;

public class CityActivity extends Activity {
    private TextView localText;
    private GetJsonDataUtil getJsonDataUtil;
    private ListView listView;
    private EditText editText;
    private List<Map<String, String>> list;
    private Map<String, String> localMap;
    private String city = "";

    private List<String> searchCityData = null;


    /*百度地图定位全局变量*/
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    public Context mContext;
    private AnimationDrawable animationDrawable;
    public Map<String, String> map;

    @ViewInject(R.id.ant_movie_city_chose_city)
    LinearLayout choseCityLayout;
    @ViewInject(R.id.ant_movie_city_search_city)
    LinearLayout searchCityLayout;
    @ViewInject(R.id.ant_movie_city_search_city_list_view)
    ListView searchCityListView;
    @ViewInject(R.id.ant_movie_city_local_loading_icon)
    ImageView localIcon;
    @ViewInject(R.id.local_city_state_text)
    TextView localStateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ViewUtils.inject(this);

        mContext = this;
        getJsonDataUtil = new GetJsonDataUtil();
        listView = (ListView) findViewById(R.id.city_list);
        editText = (EditText) findViewById(R.id.city_search_edit_text);
        localText = (TextView) findViewById(R.id.local_city_name);
        localAnimShow();
        localText.setText("");
        localText.setClickable(false);
        localStateText.setText("正在定位…");
        //开启定位
        checkPermission();
        /*edittext设置内容改变监听*/
        editText.addTextChangedListener(textWatcher);
        /*解析城市json文件，将数据存入list中*/
        InputStream inputStream = getResources().openRawResource(R.raw.city);
        String jsonStr = getJsonDataUtil.fileReader(inputStream);
        list = getJsonDataUtil.getJsonData(jsonStr);
        /*将list中的数据封装成A:.....,B:....形式数据*/
        Map<String, List<String>> map = getJsonDataUtil.formatCityData(list);
        /*listview设置适配器*/
        listView.setAdapter(new CityListAdapter(this, map));
        searchCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ChoseMovieCityActivity.class);
                intent.putExtra("city_name", searchCityData.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /*设置edittext内容变化事件*/
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Toast.makeText(MainActivity.this, editText.getText()+" onTextChanged"+s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !"".equals(s.toString().trim())) {
                searchCityData = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).get("name").contains(s)) {
                        searchCityData.add(list.get(i).get("name"));
                    }
                }
                //AlertDialogUtil.getlistDialog(mContext, l);
                choseCityLayout.setVisibility(View.GONE);
                searchCityLayout.setVisibility(View.VISIBLE);
                searchCityListView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.search_city_list_item, R.id.ant_movie_search_city_list_item_text_View, searchCityData));

            } else {
                choseCityLayout.setVisibility(View.VISIBLE);
                searchCityLayout.setVisibility(View.GONE);
            }
        }
    };

    public void exitChoseCity(View v) {
        finish();
    }

    public void choseLocalCity(View v) {
        Intent intent = new Intent(mContext, ChoseMovieCityActivity.class);
        intent.putExtra("city_name", localText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void localAnimShow() {
        localIcon.setImageResource(R.drawable.ant_movie_local_city_anim_loading);
        animationDrawable = (AnimationDrawable) localIcon.getDrawable();
        animationDrawable.start();
        localIcon.setVisibility(View.VISIBLE);
    }

    public void localAnimClose() {
        localIcon.setImageResource(R.drawable.ant_movie_local_city_anim_loading);
        animationDrawable = (AnimationDrawable) localIcon.getDrawable();
        animationDrawable.stop();
        localIcon.setVisibility(View.GONE);
    }

    /*设置定位动态权限*/
    public void checkPermission() {
        if (DynamicPermission.checkPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            bdLocalCity();
        }
    }

    //动态权限申请结束
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权
                    bdLocalCity();
                } else {
                    //用户拒绝授权
                    Toast.makeText(this, "您拒绝访问！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*百度地图定位获取城市*/
    public void bdLocalCity() {
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(new MyLocationListener());
        initLocation();
        mLocationClient.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(city.equals("")){
                    localAnimClose();
                    localText.setText("");
                    localText.setClickable(false);
                    localStateText.setText("定位失败");
                }else{
                    localAnimClose();
                    localText.setText(city.substring(0,city.length()-1));
                    localText.setClickable(true);
                    localStateText.setText("当前定位城市");
                }
            }
        },4000);

        //new MyThreadLocal().start();
    }

    public void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(0);
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

    class MyLocationListener implements BDLocationListener {
        int i = 0;
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.e("bdlocal", "onReceiveLocation");
            //获取定位结果
            Log.e("time", location.getTime());//获取定位时间
            Log.e("type", location.getLocType() + "");//获取类型类型
            Log.e("latitude", location.getLatitude() + "");//获取纬度信息
            Log.e("lontitude", location.getLongitude() + "");//获取经度信息
            Log.e("radius", location.getRadius() + "");//获取定位精准度

            Log.e("location.getLocType()", location.getLocType() + "");
            Log.e("Gps", BDLocation.TypeGpsLocation + "");
            Log.e("NetWork", BDLocation.TypeNetWorkLocation + "");
            Log.e("OffLine", BDLocation.TypeOffLineLocation + "");

            if (location.getLocType() == BDLocation.TypeGpsLocation ||
                    location.getLocType() == BDLocation.TypeNetWorkLocation ||
                    location.getLocType() == BDLocation.TypeOffLineLocation) {
                Log.e("address", location.getAddrStr() + "");//获取城市
                Log.e("city", location.getCity() + "");//获取城市
                Log.e("bdlocal", "------>"+location.getLocType());
                city = location.getCity();
                Log.e("bdlocal", "定位成功");
            } else {
                Log.e("bdlocal", "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.e("info", s + " " + i);
        }
    }
}
