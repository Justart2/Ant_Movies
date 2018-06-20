package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.aezdd.www.adapter.MovieCityAdapter;
import top.aezdd.www.entity.MovieCity;
import top.aezdd.www.utils.HttpUtil;

public class ChoseMovieCityActivity extends Activity {
    SharedPreferences s;
    RequestQueue requestQueue;
    List<MovieCity> movieCityData = new ArrayList<>();
    private String cityName = "城市";
    @ViewInject(R.id.text_city_name)TextView city;
    @ViewInject(R.id.pull_to_refresh_movie_city)PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.list_movie_city)ListView movieCityListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_movie_city);
        ViewUtils.inject(this);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode ==RESULT_OK){
                cityName = data.getStringExtra("city_name");
            }
        }
        movieCityData.removeAll(movieCityData);
        getHttpData();
        Log.d("sss",cityName);
    }

    public void initView(){

        requestQueue = Volley.newRequestQueue(this);
        movieCityData.removeAll(movieCityData);
        s = getSharedPreferences("ant_movies_city", MODE_PRIVATE);
        cityName = s.getString("movie_city","城市");
        Log.e("sss-------------->", cityName);

        getHttpData();
        mPullToRefreshView = (PullToRefreshView) this.findViewById(R.id.pull_to_refresh_movie_city);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        movieCityData.removeAll(movieCityData);
                        getHttpData();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        movieCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences = getSharedPreferences("ant_movies_city", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("movie_city_id", movieCityData.get(position).getcId());
                editor.putString("movie_city_name", movieCityData.get(position).getcName());
                editor.putString("movie_city_address", movieCityData.get(position).getcAddress());
                editor.putString("movie_city_phone", movieCityData.get(position).getcPhone());
                editor.putString("movie_city", movieCityData.get(position).getcCity());
                editor.commit();
                finish();
            }
        });
    }

    public void getHttpData(){
        String url = HttpUtil.HttpUrl+"/moviecity/movie_city_android.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try{
                    JSONArray jsonArray = new JSONArray(s);
                    for(int i =0;i<jsonArray.length();i++){
                        MovieCity movieCity = new MovieCity();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        movieCity.setcId(jsonObject.getInt("cId"));
                        movieCity.setcName(jsonObject.getString("cName"));
                        movieCity.setcAddress(jsonObject.getString("cAddress"));
                        movieCity.setcCity(jsonObject.getString("cCity"));
                        movieCity.setcPhone(jsonObject.getString("cPhone"));
                        movieCityData.add(movieCity);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    city.setText(cityName);
                    movieCityListView.setAdapter(new MovieCityAdapter(ChoseMovieCityActivity.this,movieCityData,movieCityListView));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ChoseMovieCityActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                Log.d("000000--------",cityName);
                map.put("city_name",cityName);
                return map;
            }
        };
        stringRequest.setTag("movie_city_volley");
        requestQueue.add(stringRequest);
    }

    /**
     * volley与Activity生命周期联动
     * */
    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll("movie_city_volley");
    }

    /**
     * 选择城市
     * */
    public void toChoseCity(View v){
        Intent intent = new Intent(this,CityActivity.class);
        startActivityForResult(intent,1);
    }
    public void exitChoseMovieCity(View v){
        finish();
    }
}
