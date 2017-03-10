package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.aezdd.www.entity.Movie;
import top.aezdd.www.entity.NowsEntity;
import top.aezdd.www.fragment.MovieListFragment;
import top.aezdd.www.fragment.MovieShowFragment;
import top.aezdd.www.fragment.NowsFragment;
import top.aezdd.www.fragment.UserCoreFragment;
import top.aezdd.www.utils.UserUtils;
import top.aezdd.www.utils.httpUtils.HttpUtil;
import top.aezdd.www.viewholder.MoviesViewHolder;
import top.aezdd.www.viewholder.NowsViewHolder;

public class MainActivity extends Activity implements MovieShowFragment.MovieShowFragmentInterface, MovieListFragment.MovieListFragmentInterface ,NowsFragment.NowsFragmentInterface,UserCoreFragment.UserFragmentInterface{
    private String tag = "now_movies_info";
    String url;
    FragmentManager fragmentManager;
    /*设置索引栏图标*/
    @ViewInject(R.id.index_1)
    ImageView index1;
    @ViewInject(R.id.index_2)
    ImageView index2;
    @ViewInject(R.id.index_3)
    ImageView index3;
    @ViewInject(R.id.index_4)
    ImageView index4;
    TextView showingButton;
    TextView willingButton;
    ListView movies_now_list;
    ListView nows_list;
    PullToRefreshView mPullToRefreshView;
    TextView fragMovieShowName;
    private RequestQueue requestQueue;
    private List<Movie> moviesList = new ArrayList<Movie>();
    private List<NowsEntity> nowsList = new ArrayList<NowsEntity>();
    private TextView movieCity;
    private SharedPreferences s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        requestQueue = Volley.newRequestQueue(this);




        /*sweet-alert-dialog插件*/

        /*加载主页的fragment*/
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new MovieListFragment(), "MovieListFragment").commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        s = getSharedPreferences("ant_movies_city", MODE_PRIVATE);
        String mCity = s.getString("movie_city_name", "");
        if(!mCity.equals("")){
            movieCity.setText(mCity.substring(2, 4)+"店");
        }else{
            movieCity.setText("影院->");
        }

    }

    /**
     * volley与activity生命周期的联动
     */
    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll(tag);
        requestQueue.cancelAll("nows_volley");
    }
    /**
     * 标题栏点击事件
     * */
    public void toShowingMovies(View v){
        showingButton.setBackgroundColor(0xFFEEEEEE);
        willingButton.setBackgroundColor(0xFF888888);
        willingButton.setTextColor(0xFFFFFFFF);
        showingButton.setTextColor(0xFF555555);
        url = HttpUtil.HttpUrl + "/movies/now_movies_android.do";
        getHttpData();
    }

    public void toWillingMovies(View v) {
        willingButton.setBackgroundColor(0xFFEEEEEE);
        showingButton.setBackgroundColor(0xFF888888);
        showingButton.setTextColor(0xFFFFFFFF);
        willingButton.setTextColor(0xFF555555);
        url = HttpUtil.HttpUrl + "/movies/will_movies_android.do";
        getHttpData();
    }
    public void toChoseMovieCity(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ChoseMovieCityActivity.class);
        startActivity(intent);
    }

    /**
     * volley框架加载文本数据
     */
    public void getHttpData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Toast.makeText(MainActivity.this,s, Toast.LENGTH_LONG).show();

                try {
                    moviesList.removeAll(moviesList);
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setmId((Integer) jsonObject.get("mId"));
                        movie.setmActor(jsonObject.getString("mActor"));
                        movie.setmDirector(jsonObject.getString("mDirector"));
                        movie.setmCountry(jsonObject.getString("mCountry"));
                        movie.setmDescription(jsonObject.getString("mDescription"));
                        movie.setmName(jsonObject.getString("mName"));
                        movie.setmPicture(jsonObject.getString("mPicture"));
                        movie.setmPrice(Float.parseFloat(jsonObject.get("mPrice") + ""));
                        movie.setmRate(jsonObject.getString("mRate"));
                        movie.setmReleaseTime(jsonObject.getString("mReleaseTime"));
                        movie.setmStagePhotos(jsonObject.getString("mStagePhotos"));
                        movie.setmTimeLength(jsonObject.getString("mTimeLength"));
                        movie.setmType(jsonObject.getString("mType"));
                        movie.setmVersion(jsonObject.getString("mVersion"));
                        moviesList.add(movie);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    movies_now_list.setAdapter(new MoviesAdapter(moviesList));

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag(tag);
        requestQueue.add(stringRequest);
    }

    /**
     * @Xutils加载网络图片
     */
    public void getMovieImage(ImageView imageView, String url) {
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.display(imageView, url);
    }

    /**
     * 加载电影listview适配器采用ViewHolder优化
     */
    class MoviesAdapter extends BaseAdapter {
        private List<Movie> list;

        public MoviesAdapter(List<Movie> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MoviesViewHolder moviesViewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.movies_list_container, parent, false);
                moviesViewHolder = new MoviesViewHolder();
                moviesViewHolder.setMovieName((TextView) convertView.findViewById(R.id.textView_movie_name));
                moviesViewHolder.setMovieType((TextView) convertView.findViewById(R.id.textView_movie_type));
                moviesViewHolder.setMovieReleaseTime((TextView) convertView.findViewById(R.id.textView_movie_time));
                moviesViewHolder.setMovieVersion((TextView) convertView.findViewById(R.id.textView_movie_version));
                moviesViewHolder.setMovieLogo((ImageView) convertView.findViewById(R.id.imageView_movie_img));
                moviesViewHolder.setMovieRate((TextView) convertView.findViewById(R.id.ratingBar_movie_rank));
                convertView.setTag(moviesViewHolder);

            }
            moviesViewHolder = (MoviesViewHolder) convertView.getTag();
            moviesViewHolder.getMovieName().setText(moviesList.get(position).getmName());
            moviesViewHolder.getMovieRate().setText(moviesList.get(position).getmRate() + "级");
            moviesViewHolder.getMovieType().setText(moviesList.get(position).getmType());
            moviesViewHolder.getMovieVersion().setText(moviesList.get(position).getmVersion());
            getMovieImage(moviesViewHolder.getMovieLogo(), HttpUtil.IMGHTTPURL + moviesList.get(position).getmPicture());
            movies_now_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MovieDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie_info", moviesList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    /**
     * @获取聚合数据新闻api
     * */
    public void getNowsInternetData(){
        url = "http://v.juhe.cn/toutiao/index";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for(int i = 0;i<jsonArray.length();i++){
                        NowsEntity nows = new NowsEntity();
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        nows.setAuthor_name(jsonObject3.getString("author_name"));
                        nows.setTitle(jsonObject3.getString("title"));
                        nows.setDate(jsonObject3.getString("date"));
                        nows.setRealtype(jsonObject3.getString("realtype"));
                        nows.setUrl(jsonObject3.getString("url"));
                        nows.setType(jsonObject3.getString("type"));
                        nows.setThumbnail_pic_s(jsonObject3.getString("thumbnail_pic_s"));
                        nows.setThumbnail_pic_s02(jsonObject3.getString("thumbnail_pic_s02"));
                        nows.setThumbnail_pic_s03(jsonObject3.getString("thumbnail_pic_s03"));
                        nowsList.add(nows);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    nows_list.setAdapter(new NowsAdapter(nowsList));
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("type", "top");
                map.put("key", "1ebfe70323925ba4e80dfbe84f0efa47");
                return map;
            }
        };
        stringRequest.setTag("nows_volley");
        requestQueue.add(stringRequest);
    }
    /**
     * @新闻list设置adapter类
     * */
    class NowsAdapter extends BaseAdapter{
        List<NowsEntity> list;
        public NowsAdapter(List<NowsEntity> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NowsViewHolder nowsViewHolder;
            if(convertView == null){
                nowsViewHolder = new NowsViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.fragment_nows_item,parent,false);
                nowsViewHolder.setNowsTitle((TextView) convertView.findViewById(R.id.nows_title));
                nowsViewHolder.setNowsType((TextView) convertView.findViewById(R.id.nows_type));
                nowsViewHolder.setNowsTime((TextView) convertView.findViewById(R.id.nows_date_time));
                nowsViewHolder.setNowsImage((ImageView) convertView.findViewById(R.id.a_nows_logo));
                convertView.setTag(nowsViewHolder);
            }
            nowsViewHolder = (NowsViewHolder)convertView.getTag();
            getMovieImage(nowsViewHolder.getNowsImage(), list.get(position).getThumbnail_pic_s());
            nowsViewHolder.getNowsType().setText(list.get(position).getRealtype());
            nowsViewHolder.getNowsTitle().setText(list.get(position).getTitle());
            nowsViewHolder.getNowsTime().setText(list.get(position).getDate());
            nows_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,NowsDetailsActivity.class);
                    intent.putExtra("web_nows_url", list.get(position).getUrl());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
    /**
     * @设置索引栏
     */
    public void toIndex(View v) {
        index1.setImageResource(R.drawable.cinema_home_tab_ticket_p);
        index2.setImageResource(R.drawable.cinema_home_tab_wallet_n);
        index3.setImageResource(R.drawable.cinema_home_tab_invite_n);
        index4.setImageResource(R.drawable.cinema_home_tab_member_n);
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main,new MovieListFragment(),"MovieListFragment").commit();
    }

    public void toMovieShow(View v) {
        index1.setImageResource(R.drawable.cinema_home_tab_ticket_n);
        index2.setImageResource(R.drawable.cinema_home_tab_wallet_p);
        index3.setImageResource(R.drawable.cinema_home_tab_invite_n);
        index4.setImageResource(R.drawable.cinema_home_tab_member_n);
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main,new MovieShowFragment(),"MovieShowFragment").commit();
    }

    public void toOther(View v) {
        index1.setImageResource(R.drawable.cinema_home_tab_ticket_n);
        index2.setImageResource(R.drawable.cinema_home_tab_wallet_n);
        index3.setImageResource(R.drawable.cinema_home_tab_invite_p);
        index4.setImageResource(R.drawable.cinema_home_tab_member_n);
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main,new NowsFragment(),"NowsFragment").commit();
    }

    public void toUser(View v) {
        index1.setImageResource(R.drawable.cinema_home_tab_ticket_n);
        index2.setImageResource(R.drawable.cinema_home_tab_wallet_n);
        index3.setImageResource(R.drawable.cinema_home_tab_invite_n);
        index4.setImageResource(R.drawable.cinema_home_tab_member_p);
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main,new UserCoreFragment(),"UserCoreFragment").commit();
    }


    /**
     * @实现Fragment方法
     */
    @Override
    public void loadMovieListData() {
        moviesList.removeAll(moviesList);
        url = HttpUtil.HttpUrl + "/movies/now_movies_android.do";
        getHttpData();
        Fragment fragment = fragmentManager.findFragmentByTag("MovieListFragment");
        movieCity = (TextView)fragment.getView().findViewById(R.id.text_movie_city_name);
        /*加载SharedPreference中存的影院信息*/
        s = getSharedPreferences("ant_movies_city", MODE_PRIVATE);
        String mCity = s.getString("movie_city_name", "");
        if(!"".equals(mCity) || mCity != null){
            movieCity.setText(mCity.substring(2, 4)+"店");
        }else{
            movieCity.setText("影院->");
        }

        /*pull-refresh-listview-插件*/
        showingButton = (TextView)fragment.getView().findViewById(R.id.test_movie_showing);
        willingButton = (TextView)fragment.getView().findViewById(R.id.test_movie_willing);
        movies_now_list = (ListView)fragment.getView().findViewById(R.id.movie_now_list);
        mPullToRefreshView = (PullToRefreshView) fragment.getView().findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moviesList.removeAll(moviesList);
                        getHttpData();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void loadMovieshowData() {
        Fragment fragment = fragmentManager.findFragmentByTag("MovieShowFragment");

    }

    @Override
    public void getNowsData() {
        Fragment fragment = fragmentManager.findFragmentByTag("NowsFragment");
        nows_list = (ListView)fragment.getView().findViewById(R.id.nows_list);
        getNowsInternetData();
        mPullToRefreshView = (PullToRefreshView)fragment.getView().findViewById(R.id.pull_to_refresh_nows);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nowsList.removeAll(nowsList);
                        getNowsInternetData();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void getUserData() {
        Fragment fragment = fragmentManager.findFragmentByTag("UserCoreFragment");
    }
}
