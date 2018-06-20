package top.aezdd.www.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import top.aezdd.www.ant_movies.ChoseMovieCityActivity;
import top.aezdd.www.ant_movies.CityActivity;
import top.aezdd.www.ant_movies.R;
import top.aezdd.www.viewholder.CityGridViewHolder;

/**
 * Created by jianzhou.liu on 2017/3/10.
 */
public class CityGridAdapter extends BaseAdapter{
    List<String> list;
    LayoutInflater inflate;
    Context mContext;
    public CityGridAdapter(Context context,List<String> l){
        inflate = LayoutInflater.from(context);
        mContext = context;
        list = l;
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
        CityGridViewHolder gridViewHolder ;
        if(convertView == null){
            convertView = inflate.inflate(R.layout.city_grid_item, parent, false);
            gridViewHolder = new CityGridViewHolder();
            gridViewHolder.setCityName((TextView) convertView.findViewById(R.id.city_grid_name));
            convertView.setTag(gridViewHolder);
        }
        gridViewHolder = (CityGridViewHolder)convertView.getTag();
        gridViewHolder.getCityName().setText(list.get(position));
        gridViewHolder.getCityName().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity)mContext;
                Intent intent = new Intent(mContext, ChoseMovieCityActivity.class);
                intent.putExtra("city_name",list.get(position));
                activity.setResult(activity.RESULT_OK,intent);
                activity.finish();
                //Toast.makeText(mContext, list.get(position) + "", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

}
