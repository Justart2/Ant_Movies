package top.aezdd.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.entity.MovieShow;
import top.aezdd.www.viewholder.MovieShowViewHolder;

/**
 * Created by jianzhou.liu on 2017/4/7.
 */

public class MovieShowAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context mContext;
    List<MovieShow> list;
    public MovieShowAdapter(Context context, List<MovieShow> list){
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        mContext = context;
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
        MovieShowViewHolder movieShowViewHolder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.layout_movie_show_item,parent,false);
            movieShowViewHolder = new MovieShowViewHolder();
            movieShowViewHolder.setMovieShowCountry((TextView)convertView.findViewById(R.id.text_a_movie_show_country));
            movieShowViewHolder.setMovieShowDiscount((TextView) convertView.findViewById(R.id.text_a_movie_show_discount));
            movieShowViewHolder.setMovieShowHall((TextView) convertView.findViewById(R.id.text_a_movie_show_hall));
            movieShowViewHolder.setMovieShowprice((TextView) convertView.findViewById(R.id.text_a_movie_show_price));
            movieShowViewHolder.setMovieShowTime((TextView) convertView.findViewById(R.id.text_a_movie_show_time));
            movieShowViewHolder.setMovieShowTimeLength((TextView) convertView.findViewById(R.id.text_a_movie_show_time_length));
            movieShowViewHolder.setMovieShowVersion((TextView) convertView.findViewById(R.id.text_a_movie_show_version));
            convertView.setTag(movieShowViewHolder);
        }
        movieShowViewHolder = (MovieShowViewHolder)convertView.getTag();
        if(list.get(position).getsOnSale()==0){
            movieShowViewHolder.getMovieShowDiscount().setText("暂无");
        }else{
            movieShowViewHolder.getMovieShowDiscount().setText("全场"+list.get(position).getsOnSale()+"折");
        }
        /*就座率统计*/
        String seat = list.get(position).getMoviehall().gethSeat();
        //List<String> seatList = new ArrayList<>();
        String s1[] = seat.split(";");
        int seatCount = 0;
        int seated = 0;
        for(int i=0;i<s1.length;i++){
            for(int j=0;j<s1[i].length();j++){
                //seatList.add(s1[i].charAt(j)+"");
                if((s1[i].charAt(j)+"").equals("1")){
                    seated++;
                }
                if(!(s1[i].charAt(j)+"").equals(" ")){
                    seatCount++;
                }
            }
        }
        Log.e("就座率：---",seated+"/"+seatCount);
        movieShowViewHolder.getMovieShowHall().setText(list.get(position).getMoviehall().gethName());
        movieShowViewHolder.getMovieShowTime().setText(new SimpleDateFormat("HH:mm").format(new Date(list.get(position).getsTime())));
        movieShowViewHolder.getMovieShowprice().setText("￥" + list.get(position).getMovie().getmPrice());
        movieShowViewHolder.getMovieShowTimeLength().setText(list.get(position).getMovie().getmTimeLength()+"分钟");
        String movieHallType = list.get(position).getMovie().getmVersion();
        if(movieHallType.contains("中国巨幕")){
            movieHallType = "巨幕";
        }else{
            movieHallType = "普通";
        }
        movieShowViewHolder.getMovieShowVersion().setText(movieHallType);
        movieShowViewHolder.getMovieShowCountry().setText("座位:" + seated+"/"+seatCount);

        return convertView;
    }
}
