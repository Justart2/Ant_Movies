package top.aezdd.www.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.entity.NowsEntity;
import top.aezdd.www.entity.Order;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.viewholder.NowsViewHolder;
import top.aezdd.www.viewholder.OrderViewHolder;

/**
 * Created by jianzhou.liu on 2017/3/13.
 */
public /**
 * @新闻list设置adapter类
 * */
class UserOrderAdapter extends BaseAdapter {
    private List<Order> list;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private RequestQueue requestQueue;
    public UserOrderAdapter(Context context, List<Order> list,RequestQueue requestQueue){
        this.list = list;
        this.requestQueue = requestQueue;
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
        OrderViewHolder orderViewHolder;
        if(convertView == null){
            orderViewHolder = new OrderViewHolder();
            convertView = layoutInflater.inflate(R.layout.ant_user_order_list_item,parent,false);
            orderViewHolder.setMovieLogo((ImageView)convertView.findViewById(R.id.ant_user_order_movie_image));
            orderViewHolder.setMovieName((TextView)convertView.findViewById(R.id.ant_user_order_movie_name));
            orderViewHolder.setMovieShowCityHaLL((TextView)convertView.findViewById(R.id.ant_user_order_movie_show_city));
            orderViewHolder.setMovieShowTime((TextView)convertView.findViewById(R.id.ant_user_order_movie_show_time));
            orderViewHolder.setOrderAccount((TextView)convertView.findViewById(R.id.ant_user_order_price));
            orderViewHolder.setOrderState((TextView)convertView.findViewById(R.id.ant_user_order_state));
            orderViewHolder.setPayStateIcon((ImageView)convertView.findViewById(R.id.ant_movie_user_order_pay_icon));
            orderViewHolder.setDelete((TextView)convertView.findViewById(R.id.user_order_delete));
            orderViewHolder.setPayText((TextView) convertView.findViewById(R.id.user_order_pay));
            convertView.setTag(orderViewHolder);
        }
        orderViewHolder = (OrderViewHolder) convertView.getTag();
        //加载图片
        String url = HttpUtil.MOVIE_IMG_HTTP_URL+list.get(position).getMovieShow().getMovie().getmPicture();
        LoadImg.getUrlImageByVolley(mContext,requestQueue,url,orderViewHolder.getMovieLogo());
        orderViewHolder.getMovieName().setText(list.get(position).getMovieShow().getMovie().getmName());
        orderViewHolder.getMovieShowCityHaLL().setText(list.get(position).getMovieShow().getMoviehall().getMovieCity().getcName()
                +" "+list.get(position).getMovieShow().getMoviehall().gethName());
        orderViewHolder.getMovieShowTime().setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(list.get(position).getMovieShow().getsTime())));

        //订单状态处理
        String state = list.get(position).getoState();
        if(state.equals("succeed")){
            orderViewHolder.getPayStateIcon().setImageResource(R.drawable.pay_ok_red);
            orderViewHolder.getPayText().setVisibility(View.GONE);
        }else{
            orderViewHolder.getPayStateIcon().setImageResource(R.drawable.ant_movie_pay_no_black);
            orderViewHolder.getPayText().setVisibility(View.VISIBLE);
        }
        //获取座位信息
        String seatInfo = list.get(position).getoSeatInfo();

        orderViewHolder.getPayText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去支付
                Toast.makeText(mContext, "支付", Toast.LENGTH_SHORT).show();
            }
        });
        orderViewHolder.getDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
