package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

import top.aezdd.www.entity.MovieCity;
import top.aezdd.www.utils.UserUtils;
import top.aezdd.www.utils.httpUtils.HttpUtil;
import top.aezdd.www.viewholder.MovieCityViewHolder;

public class ChoseMovieCityActivity extends Activity {

    RequestQueue requestQueue;
    List<MovieCity> movieCityData = new ArrayList<>();
    @ViewInject(R.id.text_city_name)TextView city;
    @ViewInject(R.id.pull_to_refresh_movie_city)PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.list_movie_city)ListView movieCityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_movie_city);
        ViewUtils.inject(this);
        initView();
    }
    public void initView(){
        requestQueue = Volley.newRequestQueue(this);
        getHttpData();
        city.setText(UserUtils.city);
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
                    movieCityList.setAdapter(new MovieCityAdapter(movieCityData));
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
                map.put("city_name",UserUtils.city);
                return map;
            }
        };
        stringRequest.setTag("movie_city_volley");
        requestQueue.add(stringRequest);
    }
    class MovieCityAdapter extends BaseAdapter{
        List<MovieCity> list;
        public MovieCityAdapter(List<MovieCity> list){
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MovieCityViewHolder movieCityViewHolder;
            if(convertView == null){
                movieCityViewHolder = new MovieCityViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.movie_city_item,parent,false);
                movieCityViewHolder.setMovieCityAddress((TextView)convertView.findViewById(R.id.text_movie_city_item_address));
                movieCityViewHolder.setMovieCityName((TextView) convertView.findViewById(R.id.text_movie_city_item_name));
                convertView.setTag(movieCityViewHolder);
            }
            movieCityViewHolder = (MovieCityViewHolder)convertView.getTag();
            movieCityViewHolder.getMovieCityAddress().setText(list.get(position).getcAddress());
            movieCityViewHolder.getMovieCityName().setText(list.get(position).getcName());
            movieCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences sharedPreferences = getSharedPreferences("ant_movies_city",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("movie_city_id", list.get(position).getcId());
                    editor.putString("movie_city_name", list.get(position).getcName());
                    editor.putString("movie_city_address",list.get(position).getcAddress());
                    editor.putString("movie_city_phone",list.get(position).getcPhone());
                    editor.putString("movie_city",list.get(position).getcCity());
                    editor.commit();
                    finish();
                }
            });
            return convertView;
        }
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

    }
    public void exitChoseMovieCity(View v){
        finish();
    }
}
