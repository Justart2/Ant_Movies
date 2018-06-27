package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.entity.Order;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.viewholder.OrderViewHolder;

public class UserOrderActivity extends Activity {
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private SweetAlertDialog mDialog = null;
    private List<Order> orderList = new ArrayList<>();
    @ViewInject(R.id.ant_user_order_list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        ViewUtils.inject(this);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("ant_user_info", MODE_PRIVATE);
        getInternetData();
    }

    public void getInternetData() {
        String url = HttpUtil.HttpUrl + "/order/android_user_order.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("sss", s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        Order order = gson.fromJson(jsonObject.toString(), Order.class);
                        orderList.add(order);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    listView.setAdapter(new UserOrderAdapter(UserOrderActivity.this, orderList));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UserOrderActivity.this, "网络开小差了！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", sharedPreferences.getInt("user_id", -1) + "");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void deleteUserOrder(final String id){
        String url = HttpUtil.HttpUrl + "/order/android_delete_user_order.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.equals("succeed")){
                    orderList.removeAll(orderList);
                    getInternetData();
                    mDialog.setTitleText("Deleted!")
                            .setContentText("删除成功!")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }else{
                    mDialog.setTitleText("Error!")
                            .setContentText("发生未知错误！")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.setTitleText("Error!")
                        .setContentText("出错啦，请检查网络连接!")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("order_id",  id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    class UserOrderAdapter extends BaseAdapter {
        private List<Order> list;
        private Context mContext;
        private LayoutInflater layoutInflater;
        public UserOrderAdapter(Context context, List<Order> list){
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
        public View getView(final int position, View convertView, ViewGroup parent) {
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
            String seatInfo[] = list.get(position).getoSeatInfo().split(";");
            final List<String> seatList = new ArrayList<>();
            for(int i = 0;i<seatInfo.length;i++){
                String x = seatInfo[i].split(",")[0];
                String y = seatInfo[i].split(",")[1];
                String xy = x+":"+y;
                seatList.add(xy);
            }
            orderViewHolder.getPayText().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去支付
                    //Toast.makeText(mContext, "支付", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserOrderActivity.this,OrderActivity.class);
                    intent.putExtra("a_movie_show_info", list.get(position).getMovieShow());
                    intent.putExtra("a_movie_show_seat",(Serializable)seatList);
                    mContext.startActivity(intent);
                }
            });
            orderViewHolder.getDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除
                    new SweetAlertDialog(UserOrderActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("确定要删除吗？")
                            .setCancelText("取消")
                            .setConfirmText("确定")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sDialog) {
                                    mDialog = sDialog;
                                    //执行删除操作
                                    deleteUserOrder(list.get(position).getoId());
                                }
                            })
                            .show();

                }
            });

            return convertView;
        }
    }

    public void exitActivity(View v) {
        finish();
    }
}
