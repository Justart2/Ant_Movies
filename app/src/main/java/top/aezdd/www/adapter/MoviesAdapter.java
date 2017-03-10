package top.aezdd.www.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.entity.Movie;

/**
 * Created by aezdd on 2016/8/18.
 */
public class MoviesAdapter extends BaseAdapter {
    private List<Movie> list;
    public MoviesAdapter(List<Movie> list){
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
        if(convertView == null){
            //convertView = View.inflate(,R.layout.movies_list_container,parent)
        }

        return null;
    }
}