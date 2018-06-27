package top.aezdd.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.aezdd.www.ant_movies.AMovieShowActivity;
import top.aezdd.www.ant_movies.ChoseMovieCityActivity;
import top.aezdd.www.ant_movies.R;
import top.aezdd.www.entity.Movie;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.viewholder.MoviesViewHolder;

/**
 * Created by aezdd on 2016/8/18.
 */
/**
 * 加载电影listview适配器采用ViewHolder优化
 */
public class MoviesAdapter extends BaseAdapter {
    private List<Movie> moviesList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    //电影喜欢度图标显示
    private int movieLikePImg[] = {R.drawable.cinema_icon_like_0,
            R.drawable.cinema_icon_like_20,
            R.drawable.cinema_icon_like_40,
            R.drawable.cinema_icon_like_60,
            R.drawable.cinema_icon_like_80,
            R.drawable.cinema_icon_like_100};

    public MoviesAdapter(Context context,List<Movie> list) {
        this.moviesList = list;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MoviesViewHolder moviesViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.movies_list_container, parent, false);
            moviesViewHolder = new MoviesViewHolder();
            moviesViewHolder.setMovieName((TextView) convertView.findViewById(R.id.textView_movie_name));
            moviesViewHolder.setMovieType((TextView) convertView.findViewById(R.id.textView_movie_type));
            moviesViewHolder.setMovieClickBtn((TextView) convertView.findViewById(R.id.to_movie_show_btn));
            moviesViewHolder.setMovieVersion((TextView) convertView.findViewById(R.id.textView_movie_version));
            moviesViewHolder.setMovieLogo((ImageView) convertView.findViewById(R.id.imageView_movie_img));
            moviesViewHolder.setMovieRate((TextView) convertView.findViewById(R.id.movie_like_rate));
            moviesViewHolder.setMovieRateIcon((ImageView)convertView.findViewById(R.id.movie_like_rate_icon));
            convertView.setTag(moviesViewHolder);

        }
        moviesViewHolder = (MoviesViewHolder) convertView.getTag();
        showLikeIcon(Integer.parseInt(moviesList.get(position).getmRate()),moviesViewHolder.getMovieRateIcon());
        moviesViewHolder.getMovieName().setText(moviesList.get(position).getmName());
        moviesViewHolder.getMovieRate().setText(moviesList.get(position).getmRate()+"% 人喜欢");
        moviesViewHolder.getMovieType().setText(moviesList.get(position).getmType());
        moviesViewHolder.getMovieVersion().setText(moviesList.get(position).getmVersion());
        LoadImg.getMovieImage(moviesViewHolder.getMovieLogo(), HttpUtil.MOVIE_IMG_HTTP_URL + moviesList.get(position).getmPicture(), mContext);
        moviesViewHolder.getMovieClickBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s = mContext.getSharedPreferences("ant_movies_city",mContext.MODE_PRIVATE);
                String ss = s.getString("movie_city_name","");
                if(ss.equals("")||ss==null){
                    Intent intent = new Intent();
                    intent.setClass(mContext, ChoseMovieCityActivity.class);
                    mContext.startActivity(intent);
                }else{
                    Intent intent = new Intent();
                    intent.setClass(mContext, AMovieShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("a_movie_info",moviesList.get(position));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }
    /*根据对应喜欢率显示对应的图标(整数显示)*/
    public void showLikeIcon(int likePrecentage,ImageView likePercentageImageView){
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
    }
}