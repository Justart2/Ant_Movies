package top.aezdd.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.entity.NowsEntity;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.viewholder.NowsViewHolder;

/**
 * Created by jianzhou.liu on 2017/3/13.
 */
public /**
 * @新闻list设置adapter类
 * */
class NowsAdapter extends BaseAdapter {
    List<NowsEntity> list;
    private Context mContext;
    private LayoutInflater layoutInflater;
    public NowsAdapter(Context context,List<NowsEntity> list){
        this.list = list;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
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
            convertView = layoutInflater.inflate(R.layout.fragment_nows_item,parent,false);
            nowsViewHolder.setNowsTitle((TextView) convertView.findViewById(R.id.nows_title));
            nowsViewHolder.setNowsType((TextView) convertView.findViewById(R.id.nows_type));
            nowsViewHolder.setNowsTime((TextView) convertView.findViewById(R.id.nows_date_time));
            nowsViewHolder.setNowsImage((ImageView) convertView.findViewById(R.id.a_nows_logo));
            convertView.setTag(nowsViewHolder);
        }
        nowsViewHolder = (NowsViewHolder)convertView.getTag();
        LoadImg.getMovieImage(nowsViewHolder.getNowsImage(), list.get(position).getThumbnail_pic_s(),mContext);
        nowsViewHolder.getNowsType().setText(list.get(position).getCategory());
        nowsViewHolder.getNowsTitle().setText(list.get(position).getTitle());
        nowsViewHolder.getNowsTime().setText(list.get(position).getDate());


        return convertView;
    }
}
