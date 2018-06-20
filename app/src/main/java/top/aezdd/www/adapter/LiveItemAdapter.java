package top.aezdd.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.viewholder.LiveViewHolder;

/**
 * Created by jianzhou.liu on 2017/3/30.
 */

public class LiveItemAdapter extends BaseAdapter{

    List<Map<String,String>> list;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private RequestQueue requestQueue;
    public LiveItemAdapter(Context context, List<Map<String,String>> list, RequestQueue queue){
        this.list = list;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        requestQueue = queue;
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
        LiveViewHolder liveViewHolder = null;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.frame_ant_live_index_list_item,parent,false);
            liveViewHolder = new LiveViewHolder();
            liveViewHolder.setLiveImage((ImageView)convertView.findViewById(R.id.ant_live_index_item_image));
            liveViewHolder.setLiveSimName((TextView)convertView.findViewById(R.id.ant_live_index_sample_name));
            liveViewHolder.setLiveAllName((TextView)convertView.findViewById(R.id.ant_live_index_item_whole_name));
            liveViewHolder.setLiveOnline((TextView)convertView.findViewById(R.id.ant_live_index_item_online));
            liveViewHolder.setLiveTime((TextView)convertView.findViewById(R.id.ant_live_video_item_time));
            liveViewHolder.setLiveTimeLength((TextView)convertView.findViewById(R.id.ant_live_index_item_length));
            convertView.setTag(liveViewHolder);
        }
        liveViewHolder = (LiveViewHolder) convertView.getTag();
        /*"view_count": 0,
                "up_count": 0,
                "down_count": 0,
                "id": "XMjcxNzM0MjcyOA==",
                "title": "光武帝刘秀20",
                "thumbnail": "https://r1.ykimg.com/05410408563EAC406A0A46045463359B",
                "owner_name": "杨宇1975",
                "published": "2017-04-19 19:14:53",
                "category": "电视剧",
                "link": "http://v.youku.com/v_show/id_XMjcxNzM0MjcyOA==.html",
                "quality": "标清",
                "seconds": "37:47"*/
        //加载图片
        LoadImg.getMovieImage(liveViewHolder.getLiveImage(),list.get(position).get("thumbnail"),mContext);
        //LoadImg.getMovieImage(liveViewHolder.getLiveImage(),list.get(position).get("room_src"),mContext);
        //加载文字
        liveViewHolder.getLiveSimName().setText(list.get(position).get("owner_name"));
        liveViewHolder.getLiveAllName().setText(list.get(position).get("title"));
        liveViewHolder.getLiveOnline().setText(list.get(position).get("view_count"));
        //String publish = new SimpleDateFormat("MM-dd HH:mm").format(new Date(list.get(position).get("published")));
        liveViewHolder.getLiveTime().setText(list.get(position).get("published"));
        liveViewHolder.getLiveTimeLength().setText(list.get(position).get("seconds"));
        return convertView;
    }
}
