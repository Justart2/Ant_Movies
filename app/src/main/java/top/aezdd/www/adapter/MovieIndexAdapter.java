package top.aezdd.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import top.aezdd.www.ant_movies.AMovieShowActivity;
import top.aezdd.www.ant_movies.ChoseMovieCityActivity;
import top.aezdd.www.ant_movies.R;
import top.aezdd.www.entity.Movie;
import top.aezdd.www.utils.HttpUtil;

import static top.aezdd.www.ant_movies.MainActivity.RECYCLE_LINEAR;
import static top.aezdd.www.ant_movies.MainActivity.RECYCLE_STAG;

public class MovieIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MovieIndexAdapter";

    private final static int HEAD_INDEX = 0;
    private final static int FOOTER_INDEX = -1;

    List<Movie> list;
    int layoutType;
    Context mContent;

    public MovieIndexAdapter(List<Movie> list, int layoutType) {
        this.list = list;
        this.layoutType = layoutType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContent = parent.getContext();
        if (layoutType == RECYCLE_LINEAR) {
            return createBodyLinearItemHolder(parent);
        }
        if (layoutType == RECYCLE_STAG) {
            return createBodyStagItemHolder(parent);
        }
        return null;
    }

    /*private HeadItemHolder createHeadItemHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.movie_list_head_item, viewGroup, false);
        return new HeadItemHolder(view);
    }*/

    private BodyLinearItemHolder createBodyLinearItemHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.movies_list_container, viewGroup, false);
        return new BodyLinearItemHolder(view);
    }

    private BodyStagItemHolder createBodyStagItemHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.movie_waterfall_container, viewGroup, false);
        return new BodyStagItemHolder(view);
    }

    /*private FooterItemHolder createFooterItemHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.movie_list_footer_item, viewGroup, false);
        return new FooterItemHolder(view);
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Log.d(TAG,"onBindViewHolder: " + position);
        if (holder instanceof BodyLinearItemHolder) {
            bindBodyLinearHolder(holder, position);
        } else if (holder instanceof BodyStagItemHolder) {
            bindBodyStagHolder(holder, position);
        }
    }

 /*   private void bindHeadHolder(RecyclerView.ViewHolder holder, int position) {
        HeadItemHolder headItemHolder = (HeadItemHolder) holder;
    }*/

    private void bindBodyStagHolder(RecyclerView.ViewHolder holder,final int position) {

        BodyStagItemHolder bodyItemHolder = (BodyStagItemHolder) holder;

        //bodyItemHolder.movieLogo.setImageBitmap();
        //getNetworkImage(bodyItemHolder.movieLogo,list.get(position).get("img"));
        //Picasso 加载网络图片
        Picasso.with(mContent).load(HttpUtil.MOVIE_LOGOG_IMG_HTTP_URL+list.get(position).getmPicture()).noFade().into(bodyItemHolder.movieLogo);
        bodyItemHolder.movieName.setText(list.get(position).getmName());
        bodyItemHolder.movieType.setText(list.get(position).getmType());
        //bodyItemHolder.movieContent.setText(list.get(position).get("commonSpecial"));
        bodyItemHolder.movieLikeCount.setText(list.get(position).getmRate());
        showLikeIcon(list.get(position).getmRate(),bodyItemHolder.movieLike);
        //if(list.get(position).get("ratingFinal"))
        bodyItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemCLickListener.onItemClick(list,position);
            }
        });

    }


    private void bindBodyLinearHolder(RecyclerView.ViewHolder holder, final int position) {

        BodyLinearItemHolder bodyItemHolder = (BodyLinearItemHolder) holder;

        //bodyItemHolder.movieLogo.setImageBitmap();
        //getNetworkImage(bodyItemHolder.movieLogo,list.get(position-1).get("img"));
        //Picasso 加载网络图片
        Picasso.with(mContent).load(HttpUtil.MOVIE_LOGOG_IMG_HTTP_URL+list.get(position).getmPicture()).noFade().into(bodyItemHolder.movieLogo);
        bodyItemHolder.movieName.setText(list.get(position).getmName());
        bodyItemHolder.movieType.setText(list.get(position).getmType());
        bodyItemHolder.movieContent.setText(list.get(position).getmVersion());
        bodyItemHolder.movieLikeCount.setText(list.get(position).getmRate());
        showLikeIcon(list.get(position).getmRate(),bodyItemHolder.movieLike);
        //if(list.get(position).get("ratingFinal"))
        bodyItemHolder.movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s = mContent.getSharedPreferences("ant_movies_city",mContent.MODE_PRIVATE);
                String ss = s.getString("movie_city_name","");
                if(ss.equals("")||ss==null){
                    Intent intent = new Intent();
                    intent.setClass(mContent, ChoseMovieCityActivity.class);
                    mContent.startActivity(intent);
                }else{
                    Intent intent = new Intent();
                    intent.setClass(mContent, AMovieShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("a_movie_info",list.get(position));
                    intent.putExtras(bundle);
                    mContent.startActivity(intent);
                }
            }
        });
        bodyItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemCLickListener.onItemClick(list,position);
            }
        });


    }

/*    private void bindFooterHolder(RecyclerView.ViewHolder holder, int position) {
        FooterItemHolder footerHolder = (FooterItemHolder) holder;
        footerHolder.textView.setText("Game Over");
    }*/


    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /*class HeadItemHolder extends RecyclerView.ViewHolder {
        ImageView headImage;

        public HeadItemHolder(View itemView) {
            super(itemView);
            headImage = (ImageView) itemView.findViewById(R.id.movie_list_head_item_img);
        }
    }*/

    class BodyLinearItemHolder extends RecyclerView.ViewHolder {
        ImageView movieLogo;
        ImageView movieLike;
        TextView movieName;
        TextView movieType;
        TextView movieContent;
        TextView movieLikeCount;
        TextView movieBtn;

        public BodyLinearItemHolder(View itemView) {
            super(itemView);
            movieLogo = (ImageView) itemView.findViewById(R.id.imageView_movie_img);
            movieLike = (ImageView) itemView.findViewById(R.id.movie_like_rate_icon);
            movieName = (TextView) itemView.findViewById(R.id.textView_movie_name);
            movieType = (TextView) itemView.findViewById(R.id.textView_movie_version);
            movieContent = (TextView) itemView.findViewById(R.id.textView_movie_type);
            movieLikeCount = (TextView) itemView.findViewById(R.id.movie_like_rate);
            movieBtn = (TextView) itemView.findViewById(R.id.to_movie_show_btn);
        }
    }

    class BodyStagItemHolder extends RecyclerView.ViewHolder {
        ImageView movieLogo;
        ImageView movieLike;
        TextView movieName;
        TextView movieType;
        TextView movieContent;
        TextView movieLikeCount;
        TextView movieBtn;

        public BodyStagItemHolder(View itemView) {
            super(itemView);
            movieLogo = (ImageView) itemView.findViewById(R.id.imageView_movie_img);
            movieLike = (ImageView) itemView.findViewById(R.id.movie_like_rate_icon);
            movieName = (TextView) itemView.findViewById(R.id.textView_movie_name);
            movieType = (TextView) itemView.findViewById(R.id.textView_movie_type);
            //movieContent = (TextView)itemView.findViewById(R.id.textView_movie_type);
            movieLikeCount = (TextView) itemView.findViewById(R.id.movie_like_rate);
            //movieBtn = (TextView)itemView.findViewById(R.id.to_movie_show_btn);
        }
    }

    /*class FooterItemHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public FooterItemHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.movie_list_footer_item_text_view);
        }
    }*/

    public interface OnItemCLickListener{
        void onItemClick(List<Movie> list,int position);
    }
    private OnItemCLickListener onItemCLickListener;
    public MovieIndexAdapter setOnClickListener(OnItemCLickListener listener){
        onItemCLickListener = listener;
        return this;
    }
    //电影喜欢度图标显示
    private int movieLikePImg[] = {R.drawable.cinema_icon_like_0,
            R.drawable.cinema_icon_like_20,
            R.drawable.cinema_icon_like_40,
            R.drawable.cinema_icon_like_60,
            R.drawable.cinema_icon_like_80,
            R.drawable.cinema_icon_like_100};

    /*根据对应喜欢率显示对应的图标(整数显示)*/
    public void showLikeIcon(String precentage,ImageView likePercentageImageView){
        int likePrecentage = Integer.parseInt(precentage);
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
