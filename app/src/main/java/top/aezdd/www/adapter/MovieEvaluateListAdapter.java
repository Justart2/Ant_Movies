package top.aezdd.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.entity.UserEvaluate;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.view.CircleImageView;
import top.aezdd.www.viewholder.MovieEvaluateViewHolder;

/**
 * Created by jianzhou.liu on 2017/4/6.
 */

public class MovieEvaluateListAdapter extends BaseAdapter{

    List<UserEvaluate> mList;
    LayoutInflater inflate;
    Context mContext;
    RequestQueue requestQueue;
    public MovieEvaluateListAdapter(Context context, List<UserEvaluate> list, RequestQueue requestQueue){
        inflate = LayoutInflater.from(context);
        mContext = context;
        mList = list;
        this.requestQueue = requestQueue;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieEvaluateViewHolder movieEvaluateViewHolder = null;
        if(convertView==null){
            convertView = inflate.inflate(R.layout.ant_movie_detail_user_evaluate_list_item,parent,false);
            movieEvaluateViewHolder = new MovieEvaluateViewHolder();
            movieEvaluateViewHolder.setEvaluateImg((CircleImageView)convertView.findViewById(R.id.ant_movie_detail_user_evaluate_image));
            movieEvaluateViewHolder.setEvaluateName((TextView)convertView.findViewById(R.id.ant_movie_detail_user_evaluate_name));
            movieEvaluateViewHolder.setEvaluateContent((TextView)convertView.findViewById(R.id.ant_movie_detail_user_evaluate_content));
            movieEvaluateViewHolder.setEvaluateTime((TextView)convertView.findViewById(R.id.ant_movie_detail_user_evaluate_time));
            convertView.setTag(movieEvaluateViewHolder);
        }

        movieEvaluateViewHolder = (MovieEvaluateViewHolder) convertView.getTag();

        movieEvaluateViewHolder.getEvaluateName().setText(mList.get(position).getUser().getuName());
        movieEvaluateViewHolder.getEvaluateContent().setText(mList.get(position).geteInfo());
        //时间格式化
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String evaluateTime = dateFormat.format(new Date(mList.get(position).geteTime()));
        movieEvaluateViewHolder.getEvaluateTime().setText(evaluateTime);
        //设置图片
        String url = HttpUtil.IMGHTTPURL+mList.get(position).getUser().getuPicture();
        LoadImg.getUrlImageByVolley(mContext,requestQueue,url,movieEvaluateViewHolder.getEvaluateImg());

        return convertView;
    }
}
