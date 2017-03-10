package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;

import top.aezdd.www.entity.Movie;
import top.aezdd.www.utils.httpUtils.HttpUtil;

public class MovieDetailActivity extends Activity {
    private Movie movie;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ViewUtils.inject(this);
        Intent intent = getIntent();
        movie = (Movie)intent.getSerializableExtra("movie_info");
        setData();
    }
    public void setData(){
        String url1 = HttpUtil.IMGHTTPURL + movie.getmPicture();
        getInternetImage(imageMovieLogo, url1);
        textMovieCountry.setText(movie.getmCountry());
        textMovieVersion.setText(movie.getmVersion());
        textMovieName.setText(movie.getmName());
        textMovieReleaseTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(movie.getmReleaseTime())));
        textMovieType.setText(movie.getmType());
        textMovieTimeLength.setText(movie.getmTimeLength());
        textMovieDirector.setText(movie.getmDirector());
        textMovieActor.setText(movie.getmActor());
        textMovieDescription.setText(movie.getmDescription());
        String[] imagePhotos = movie.getmStagePhotos().split(";");
        getInternetImage(imageMoviePhoto1, HttpUtil.IMGHTTPURL + imagePhotos[0]);
        getInternetImage(imageMoviePhoto2, HttpUtil.IMGHTTPURL + imagePhotos[1]);
        getInternetImage(imageMoviePhoto3, HttpUtil.IMGHTTPURL+imagePhotos[2]);
        getInternetImage(imageMoviePhoto4, HttpUtil.IMGHTTPURL + imagePhotos[3]);

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
        Intent intent = new Intent();
        intent.setClass(this, AMovieShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("a_movie_info",movie);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
    /**
     * 返回上一层Activity
     * */
    public void exitActicity(View v){
        finish();
    }
}
