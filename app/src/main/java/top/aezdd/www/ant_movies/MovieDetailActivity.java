package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.aezdd.www.adapter.MovieEvaluateListAdapter;
import top.aezdd.www.entity.Movie;
import top.aezdd.www.entity.User;
import top.aezdd.www.entity.UserEvaluate;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.utils.LoginUtil;
import top.aezdd.www.view.MovieEvaluateListView;

public class MovieDetailActivity extends Activity {
    private Movie movie;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private int likeMovieState;
    private List<UserEvaluate> evaluateList;
    private int flag = 0;
    //电影喜欢度图标显示
    private int movieLikePImg[] = {R.drawable.cinema_icon_like_0,
            R.drawable.cinema_icon_like_20,
            R.drawable.cinema_icon_like_40,
            R.drawable.cinema_icon_like_60,
            R.drawable.cinema_icon_like_80,
            R.drawable.cinema_icon_like_100};
    /**
     * xUtils依赖注入组件
     * */
    @ViewInject(R.id.image_movie_detail_name)ImageView imageMovieLogo;
    @ViewInject(R.id.text_movie_detail_name)TextView textMovieName;
    @ViewInject(R.id.text_movie_detail_country)TextView textMovieCountry;
    @ViewInject(R.id.text_movie_detail_version)TextView textMovieVersion;
    @ViewInject(R.id.text_movie_detail_type)TextView textMovieType;
    @ViewInject(R.id.text_movie_detail_release_time)TextView textMovieReleaseTime;
    @ViewInject(R.id.text_movie_detail_time_length)TextView textMovieTimeLength;
    @ViewInject(R.id.text_movie_detail_director)TextView textMovieDirector;
    @ViewInject(R.id.text_movie_detail_actor)TextView textMovieActor;
    @ViewInject(R.id.text_movie_detail_description)TextView textMovieDescription;
    @ViewInject(R.id.image_movie_detail_photo1)ImageView imageMoviePhoto1;
    @ViewInject(R.id.image_movie_detail_photo2)ImageView imageMoviePhoto2;
    @ViewInject(R.id.image_movie_detail_photo3)ImageView imageMoviePhoto3;
    @ViewInject(R.id.image_movie_detail_photo4)ImageView imageMoviePhoto4;
    /*喜欢模块*/
    @ViewInject(R.id.ant_movie_detail_user_like_image_view)ImageView userLikeImageView;
    @ViewInject(R.id.ant_movie_detail_user_like_percentage_text_view)TextView likePercentageTextView;
    @ViewInject(R.id.ant_movie_detail_user_like_percentage_image_view)ImageView likePercentageImageView;
    @ViewInject(R.id.ant_movie_detail_user_dislike_image_view)ImageView userDislikeImageView;
    /*评价模块*/
    @ViewInject(R.id.ant_movie_detail_user_evaluate_list_view)MovieEvaluateListView evaluateListView;
    @ViewInject(R.id.ant_movie_detail_user_evaluate_click_icon)ImageView evaluateIcon;
    @ViewInject(R.id.ant_movie_detail_user_evaluate_click_imageview)ImageView evaluateImageView;
    @ViewInject(R.id.ant_movie_detail_user_evaluate_layout)LinearLayout evaluateLayout;
    @ViewInject(R.id.ant_movie_detail_user_evaluate_edit_text)EditText evaluateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ViewUtils.inject(this);
        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        movie = (Movie)intent.getSerializableExtra("movie_info");
        setData();
        getMovieEvaluate();
    }
    public void setData(){
        String url1 = HttpUtil.IMGHTTPURL + movie.getmPicture();
        getInternetImage(imageMovieLogo, url1);
        textMovieCountry.setText(movie.getmCountry());
        textMovieVersion.setText(movie.getmVersion());
        textMovieName.setText(movie.getmName());
        textMovieReleaseTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(movie.getmReleaseTime())));
        textMovieType.setText(movie.getmType());
        textMovieTimeLength.setText(movie.getmTimeLength()+" 分钟");
        textMovieDirector.setText(movie.getmDirector());
        textMovieActor.setText(movie.getmActor());
        textMovieDescription.setText(movie.getmDescription());
        String[] imagePhotos = movie.getmStagePhotos().split(";");
        getInternetImage(imageMoviePhoto1, HttpUtil.IMGHTTPURL + imagePhotos[0]);
        getInternetImage(imageMoviePhoto2, HttpUtil.IMGHTTPURL + imagePhotos[1]);
        getInternetImage(imageMoviePhoto3, HttpUtil.IMGHTTPURL+imagePhotos[2]);
        getInternetImage(imageMoviePhoto4, HttpUtil.IMGHTTPURL + imagePhotos[3]);
        //显示喜欢率
        showLikeIcon(Integer.parseInt(movie.getmRate()));
        //evaluateListView.setAdapter(new MovieEvaluateListAdapter(this,requestQueue));

    }



    /**
     * xUtils获取网络图片
     * */
    public void getInternetImage(ImageView imageVIew,String url){
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.display(imageVIew,url);
    }
    /**
     * 选座购票
     * */
    public void toChoseSite(View v){
        SharedPreferences s = getSharedPreferences("ant_movies_city",MODE_PRIVATE);
        String ss = s.getString("movie_city_name","");
        if(ss.equals("")||ss==null){
            Intent intent = new Intent();
            intent.setClass(this, ChoseMovieCityActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent();
            intent.setClass(this, AMovieShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("a_movie_info",movie);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }

    }
    /**
     * 电影喜欢率栏目------------------------->start
     * */

    /*Activity生命周期函数刷新界面显示喜欢率*/
    @Override
    protected void onStart() {
        super.onStart();

        if(LoginUtil.checkLogin(this)){
            requestInternet();
        }else{
            userLikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_like_n);
            userLikeImageView.setClickable(true);
            userDislikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_dislike_n);
            userDislikeImageView.setClickable(true);
        }
    }


    /*点击我喜欢和ImageView的点击事件*/
    public void likeMovieClick(View v){
        if(LoginUtil.checkLogin(this)){
            if(likeMovieState == 0){
                //我喜欢
                toLikeOrDisLikeMovieByInternet("like");
            }
            if(likeMovieState == 1){
                //取消我喜欢（删除这条记录）
                toLikeOrDisLikeMovieByInternet("delete");
            }
        }else{
            LoginUtil.toLogin(this);
        }
    }
    /*点击我不喜欢ImageView的点击事件*/
    public void dislikeMovieClick(View v){
        if(LoginUtil.checkLogin(this)){
            if(likeMovieState == 0){
                //我不喜欢
                toLikeOrDisLikeMovieByInternet("dislike");
            }
            if(likeMovieState == -1){
                //取消我不喜欢（删除这条记录）
                toLikeOrDisLikeMovieByInternet("delete");
            }
        }else{
            LoginUtil.toLogin(this);
        }
    }


    /*访问网络设置喜欢或不喜欢电影*/
    public void toLikeOrDisLikeMovieByInternet(final String state){
        String url = HttpUtil.HttpUrl+"/like/insert_like.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                String s = str.split("#")[0];
                int rate = Integer.parseInt(str.split("#")[1]);
                if(s.equals("like")){
                    userLikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_has_liked);
                    userDislikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_dislike_p);
                    userDislikeImageView.setClickable(false);
                    likeMovieState = 1;
                    showLikeIcon(rate);
                }else if(s.equals("dislike")){
                    userLikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_like_p);
                    userLikeImageView.setClickable(false);
                    userDislikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_has_disliked);
                    likeMovieState = -1;
                    showLikeIcon(rate);
                }else if(s.equals("delete")){
                    userLikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_like_n);
                    userDislikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_dislike_n);
                    userLikeImageView.setClickable(true);
                    userDislikeImageView.setClickable(true);
                    likeMovieState = 0;
                    showLikeIcon(rate);
                }else{
                    Toast.makeText(MovieDetailActivity.this, "系统内部错误", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MovieDetailActivity.this, "网络开小差了！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
                map.put("movie_like_state",state);
                map.put("user_id",LoginUtil.getUserId(MovieDetailActivity.this)+"");
                map.put("movie_id",movie.getmId()+"");
                Log.e("movie_like",state+"  "+LoginUtil.getUserId(MovieDetailActivity.this)+" "+movie.getmId());
                return map;
            }
        };
        stringRequest.setTag("movieLike");
        requestQueue.add(stringRequest);
    }

    /*访问网络查询当前用户对电影喜欢状态*/
    public void requestInternet(){
        String url = HttpUtil.HttpUrl+"/like/like_state.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.equals("null")){
                    userLikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_like_n);
                    userDislikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_dislike_n);
                    userLikeImageView.setClickable(true);
                    userDislikeImageView.setClickable(true);
                    likeMovieState = 0;
                }else{
                    if(s.equals("1")){
                        userLikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_has_liked);
                        userDislikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_dislike_p);
                        userDislikeImageView.setClickable(false);
                        likeMovieState = 1;
                    }else if(s.equals("-1")){
                        userLikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_like_p);
                        userLikeImageView.setClickable(false);
                        userDislikeImageView.setImageResource(R.drawable.cinema_icon_film_detail_has_disliked);
                        likeMovieState = -1;
                    }else{
                        Toast.makeText(MovieDetailActivity.this, "系统错误！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MovieDetailActivity.this, "网络开小差了！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("user_id",LoginUtil.getUserId(MovieDetailActivity.this)+"");
                map.put("movie_id",movie.getmId()+"");
                Log.e("movie_like",LoginUtil.getUserId(MovieDetailActivity.this)+" "+movie.getmId());
                return map;
            }
        };
        stringRequest.setTag("movieLike");
        requestQueue.add(stringRequest);
    }

    /*根据对应喜欢率显示对应的图标(整数显示)*/
    public void showLikeIcon(int likePrecentage){
        if(likePrecentage==0){
            likePercentageImageView.setImageResource(movieLikePImg[0]);
        }else if(likePrecentage<=25){
            likePercentageImageView.setImageResource(movieLikePImg[1]);
        }else if(likePrecentage<=50){
            likePercentageImageView.setImageResource(movieLikePImg[2]);
        }else if(likePrecentage<=75){
            likePercentageImageView.setImageResource(movieLikePImg[3]);
        }else if(likePrecentage<100){
            likePercentageImageView.setImageResource(movieLikePImg[4]);
        }else{
            likePercentageImageView.setImageResource(movieLikePImg[5]);
        }
        likePercentageTextView.setText(likePrecentage+"%");
    }
    /**
     * 电影喜欢率栏目------------------------->end
     * */

    /**
     * 用户评价模块-------------------------->start
     * */
    /*(1)访问网络获取该电影所有的影评*/
    public void getMovieEvaluate(){
        String url = HttpUtil.HttpUrl+"/evaluate/android_query_evaluate.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("evaluate_info",s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    evaluateList = new ArrayList<>();
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject evaluateJson= jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        UserEvaluate userEvaluate = gson.fromJson(evaluateJson.toString(),UserEvaluate.class);
                        evaluateList.add(userEvaluate);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                evaluateListView.setFocusable(false);
                evaluateListView.setAdapter(new MovieEvaluateListAdapter(MovieDetailActivity.this,evaluateList,requestQueue));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MovieDetailActivity.this, "网络出小差了！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>() ;
                map.put("movie_id",movie.getmId()+"");
                return map;
            }
        };
        stringRequest.setTag("evaluate");
        requestQueue.add(stringRequest);
    }

    /*弹出评价布局*/
    public void goEvaluateMovie(View v){
        if(LoginUtil.checkLogin(this)){
            evaluateIcon.setVisibility(View.GONE);
            evaluateLayout.setVisibility(View.VISIBLE);
            evaluateText.setText("");
        }else{
            LoginUtil.toLogin(this);
        }
    }
    /*评价电影*/
    public void evaluateMovie(View v){
        final String evaluateStr = evaluateText.getText().toString().trim();
        if(evaluateStr.equals("")){
            //Toast.makeText(this, "您还没有写评论", Toast.LENGTH_SHORT).show();
            evaluateIcon.setVisibility(View.VISIBLE);
            evaluateLayout.setVisibility(View.GONE);
            return;
        }
        String url = HttpUtil.HttpUrl+"/evaluate/android_insert_evaluate.do";
        StringRequest s = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                evaluateIcon.setVisibility(View.VISIBLE);
                evaluateLayout.setVisibility(View.GONE);
                //刷新评价列表
                getMovieEvaluate();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MovieDetailActivity.this, "网络出小差了！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("user_id",LoginUtil.getUserId(MovieDetailActivity.this)+"");
                map.put("movie_id",movie.getmId()+"");
                map.put("evaluate_info",evaluateStr);
                return map;
            }
        };

        requestQueue.add(s);

    }
    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll("movieLike");
        requestQueue.cancelAll("evaluate");
    }

    /**
     * 返回上一层Activity
     * */
    public void exitActivity(View v){
        finish();
    }
}
