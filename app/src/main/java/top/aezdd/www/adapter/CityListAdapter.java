package top.aezdd.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.viewholder.CityListViewHolder;

/**
 * Created by jianzhou.liu on 2017/3/10.
 */
public class CityListAdapter extends BaseAdapter{
    List<String> list_key=new ArrayList<String>();
    Map<String,List<String>> map;
    LayoutInflater inflate;
    Context mContext;
    public CityListAdapter(Context context,Map<String,List<String>> m){
        mContext = context;
        inflate = LayoutInflater.from(context) ;
        map = m;
        //定义一个用来存放key列表
        Iterator<String> iter=m.keySet().iterator();
        while(iter.hasNext()){
            list_key.add(iter.next());
        }
    }
    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return map.get(list_key.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityListViewHolder listViewHolder ;
        if(convertView == null){
            convertView = inflate.inflate(R.layout.city_list_item,parent,false);
            listViewHolder = new CityListViewHolder();
            listViewHolder.setCityTitle((TextView)convertView.findViewById(R.id.city_pinyin_type));
            listViewHolder.setCityGrid((GridView) convertView.findViewById(R.id.city_grid_item_id));
            convertView.setTag(listViewHolder);
        }
        listViewHolder = (CityListViewHolder)convertView.getTag();
        listViewHolder.getCityTitle().setText(list_key.get(position));
        /*gridview 设置adapter*/
        listViewHolder.getCityGrid().setAdapter(new CityGridAdapter(mContext, (List) map.get(list_key.get(position))));

        return convertView;
    }

}
