package top.aezdd.www.adapter;

import android.content.Context;
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

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.ant_movies.UserEvaluateActivity;
import top.aezdd.www.entity.UserEvaluate;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.view.CircleImageView;
import top.aezdd.www.view.SwipeListLayout;
import top.aezdd.www.viewholder.MovieEvaluateViewHolder;
import top.aezdd.www.viewholder.UserEvaluateViewHolder;

/**
 * Created by jianzhou.liu on 2017/4/11.
 */

public class UserEvaluateListAdapter extends BaseAdapter{
    List<UserEvaluate> mList;
    LayoutInflater inflate;
    Context mContext;
    RequestQueue requestQueue;

    public UserEvaluateListAdapter(Context context, List<UserEvaluate> list, RequestQueue requestQueue){
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
        UserEvaluateViewHolder userEvaluateViewHolder = null;
        if(convertView==null){
            convertView = inflate.inflate(R.layout.ant_movie_detail_user_evaluate_list_item,parent,false);
            userEvaluateViewHolder = new UserEvaluateViewHolder();
            userEvaluateViewHolder.setEvaluateImg((CircleImageView)convertView.findViewById(R.id.ant_movie_detail_user_evaluate_image));
            userEvaluateViewHolder.setEvaluateName((TextView)convertView.findViewById(R.id.ant_movie_detail_user_evaluate_name));
            userEvaluateViewHolder.setEvaluateContent((TextView)convertView.findViewById(R.id.ant_movie_detail_user_evaluate_content));
            userEvaluateViewHolder.setEvaluateTime((TextView)convertView.findViewById(R.id.ant_movie_detail_user_evaluate_time));
            userEvaluateViewHolder.setSwipeListLayout((SwipeListLayout)convertView.findViewById(R.id.swipe_list_item_id));
            userEvaluateViewHolder.setDelete((TextView)convertView.findViewById(R.id.tv_delete));
            convertView.setTag(userEvaluateViewHolder);
        }

        userEvaluateViewHolder = (UserEvaluateViewHolder) convertView.getTag();

        userEvaluateViewHolder.getEvaluateName().setText(mList.get(position).getMovie().getmName());
        userEvaluateViewHolder.getEvaluateContent().setText(mList.get(position).geteInfo());
        //时间格式化
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String evaluateTime = dateFormat.format(new Date(mList.get(position).geteTime()));
        userEvaluateViewHolder.getEvaluateTime().setText(evaluateTime);
        //设置图片
        String url = HttpUtil.IMGHTTPURL+mList.get(position).getMovie().getmPicture();
        LoadImg.getUrlImageByVolley(mContext,requestQueue,url,userEvaluateViewHolder.getEvaluateImg());
        return convertView;
    }
}
