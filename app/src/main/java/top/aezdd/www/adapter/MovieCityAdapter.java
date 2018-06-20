package top.aezdd.www.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.entity.Movie;
import top.aezdd.www.entity.MovieCity;
import top.aezdd.www.viewholder.MovieCityViewHolder;

/**
 * Created by jianzhou.liu on 2017/3/13.
 */
public class MovieCityAdapter extends BaseAdapter {

    private List<MovieCity> list;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ListView mListView;
    public MovieCityAdapter(Context context,List<MovieCity> list,ListView listView){
        mListView = listView;
        this.list = list;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
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
            convertView = layoutInflater.inflate(R.layout.movie_city_item,parent,false);
            movieCityViewHolder.setMovieCityAddress((TextView)convertView.findViewById(R.id.text_movie_city_item_address));
            movieCityViewHolder.setMovieCityName((TextView) convertView.findViewById(R.id.text_movie_city_item_name));
            convertView.setTag(movieCityViewHolder);
        }
        movieCityViewHolder = (MovieCityViewHolder)convertView.getTag();
        movieCityViewHolder.getMovieCityAddress().setText(list.get(position).getcAddress());
        movieCityViewHolder.getMovieCityName().setText(list.get(position).getcName());

        return convertView;
    }
}
