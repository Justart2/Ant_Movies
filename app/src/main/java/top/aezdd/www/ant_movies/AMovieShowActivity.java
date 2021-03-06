package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.aezdd.www.adapter.MovieShowAdapter;
import top.aezdd.www.entity.Movie;
import top.aezdd.www.entity.MovieCity;
import top.aezdd.www.entity.MovieHall;
import top.aezdd.www.entity.MovieShow;
import top.aezdd.www.utils.OtherUtils;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.viewholder.MovieShowViewHolder;

public class AMovieShowActivity extends Activity {
    /*
    * xUtils注入组件
    * */
    @ViewInject(R.id.text_a_movie_show_name)TextView textAMovieShowName;
    @ViewInject(R.id.text_a_movie_show_city)TextView textAMovieShowCity;
    @ViewInject(R.id.text_a_movie_show_city_address)TextView textAMovieShowAddress;
    @ViewInject(R.id.today_time)TextView todayTime;
    @ViewInject(R.id.a_movie_pull_to_refresh)
    PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.a_movie_show_list)
    ListView aMovieShowListView;
    Movie movie;
    SharedPreferences s = null;
    private List<MovieShow> aMovieShowData ;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amovie_show);
        /*XUtils初始化view*/
        ViewUtils.inject(this);
        /*Volley初始化请求队列*/
        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("a_movie_info");

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        aMovieShowData.removeAll(aMovieShowData);
                        getHttpData();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*获取存在Sharedpreference中的影院信息*/
        s = getSharedPreferences("ant_movies_city",MODE_PRIVATE);

        textAMovieShowName.setText(movie.getmName());
        textAMovieShowCity.setText(s.getString("movie_city_name",""));
        textAMovieShowAddress.setText(s.getString("movie_city_address",""));
        todayTime.setText(OtherUtils.getCourrentTime()+"  今天");
        getHttpData();
        aMovieShowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(AMovieShowActivity.this, ChoseSeatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("a_movie_show_for_chose_seat",aMovieShowData.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /*
        * 设置listview适配器
        * */

    /**
     * 加载网络数据
     */
    public void getHttpData() {

        String url = HttpUtil.HttpUrl + "/movieshow/a-movie-show-android.do";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                aMovieShowData = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        /*上映信息表字段*/
                        Log.i("info","--------------->");
                        MovieShow movieShow = new MovieShow();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        movieShow.sethId(jsonObject.getInt("hId"));
                        movieShow.setsId(jsonObject.getInt("sId"));
                        movieShow.setmId(jsonObject.getInt("mId"));
                        movieShow.setsTime(jsonObject.getString("sTime"));
                        movieShow.setsOnSale(jsonObject.getInt("sOnSale"));
                        /*上映关联的电影信息表字段*/
                        JSONObject jsonObject1 = jsonObject.getJSONObject("movie");
                        Movie movie = new Movie();
                        movie.setmId((Integer) jsonObject1.get("mId"));
                        movie.setmActor(jsonObject1.getString("mActor"));
                        movie.setmDirector(jsonObject1.getString("mDirector"));
                        movie.setmCountry(jsonObject1.getString("mCountry"));
                        movie.setmDescription(jsonObject1.getString("mDescription"));
                        movie.setmName(jsonObject1.getString("mName"));
                        movie.setmPicture(jsonObject1.getString("mPicture"));
                        movie.setmPrice(Float.parseFloat(jsonObject1.get("mPrice") + ""));
                        movie.setmRate(jsonObject1.getString("mRate"));
                        movie.setmReleaseTime(jsonObject1.getString("mReleaseTime"));
                        movie.setmStagePhotos(jsonObject1.getString("mStagePhotos"));
                        movie.setmTimeLength(jsonObject1.getString("mTimeLength"));
                        movie.setmType(jsonObject1.getString("mType"));
                        movie.setmVersion(jsonObject1.getString("mVersion"));
                        movieShow.setMovie(movie);
                        /*上映关联的影厅信息信息表字段*/
                        MovieHall movieHall = new MovieHall();
                        JSONObject jsonObject2 = jsonObject.getJSONObject("moviehall");
                        movieHall.sethId(jsonObject2.getInt("hId"));
                        movieHall.setcId(jsonObject2.getInt("cId"));
                        movieHall.sethName(jsonObject2.getString("hName"));
                        movieHall.sethSeat(jsonObject2.getString("hSeat"));
                        /*影厅关联影城的字段*/
                        JSONObject jsonObject3 = jsonObject2.getJSONObject("movieCity");
                        MovieCity movieCity = new MovieCity();
                        movieCity.setcId(jsonObject3.getInt("cId"));
                        movieCity.setcAddress(jsonObject3.getString("cAddress"));
                        movieCity.setcCity(jsonObject3.getString("cCity"));
                        movieCity.setcName(jsonObject3.getString("cName"));
                        movieCity.setcPhone(jsonObject3.getString("cPhone"));
                        movieHall.setMovieCity(movieCity);
                        movieShow.setMoviehall(movieHall);
                        aMovieShowData.add(movieShow);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    aMovieShowListView.setAdapter(new MovieShowAdapter(AMovieShowActivity.this,aMovieShowData));
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(AMovieShowActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("movie_city_id", s.getInt("movie_city_id",0)+"");
                map.put("city_name", s.getString("movie_city",""));
                map.put("movie_id", movie.getmId() + "");
                return map;
            }
        };
        request.setTag("a_movie_show_info");
        queue.add(request);

    }
    /*修改影城*/
    public void toChangeMovieCity(View v){
        Intent intent = new Intent(this,ChoseMovieCityActivity.class);
        startActivity(intent);
    }

    /*
    * volley与activity生命周期的联动
    * */

    @Override
    protected void onStop() {
        super.onStop();
        queue.cancelAll("a_movie_show_info");
    }

    /*
        * 返回上一个Activity
        * */
    public void exitActicity(View v) {
        finish();
    }
}
