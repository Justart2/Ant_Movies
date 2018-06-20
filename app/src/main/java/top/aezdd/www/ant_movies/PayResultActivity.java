package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import top.aezdd.www.entity.MovieShow;
import top.aezdd.www.utils.HttpUtil;

public class PayResultActivity extends Activity {
    private RequestQueue requestQueue;
    private String orderCode;
    private MovieShow movieShow;
    private String userPhone;
    private String trickCount;
    private String seatInfo = "";
    private String seatserviceInfo = "";
    private String payReasultState;
    private SharedPreferences s = null;
    private ArrayList<String> seatList;
    @ViewInject(R.id.ant_movie_order_pay_result_account)TextView accountTextView;
    @ViewInject(R.id.ant_movie_order_pay_result_movie_name)TextView movieNameTextView;
    @ViewInject(R.id.ant_movie_order_pay_result_movie_hall)TextView movieCityHallTextView;
    @ViewInject(R.id.ant_movie_order_pay_result_user_phone)TextView userPhoneTextView;
    @ViewInject(R.id.ant_movie_order_pay_result_seat)TextView seatTextView;
    @ViewInject(R.id.ant_movie_order_pay_result_code)TextView codeTextView;
    @ViewInject(R.id.ant_movie_order_pay_result_error)RelativeLayout errorLayout;
    @ViewInject(R.id.ant_movie_order_pay_result_succeed)RelativeLayout succeedLayout;
    @ViewInject(R.id.ant_movie_order_pay_result_icon)ImageView payResultIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        ViewUtils.inject(this);
        requestQueue = Volley.newRequestQueue(this);
        setData();
        saveOrderToInternet();
    }
    public void setData(){
        s = getSharedPreferences("ant_user_info",MODE_PRIVATE);
        userPhone = s.getString("user_phone","");
        Intent intent = getIntent();
        movieShow = (MovieShow)intent.getSerializableExtra("movie_show");
        orderCode = intent.getStringExtra("order_code");
        trickCount = intent.getDoubleExtra("account_price",0.0)+"";
        /*座位处理*/
        seatList = (ArrayList)intent.getSerializableExtra("seat_info");
        for(int i = 0;i<seatList.size();i++){
            String[] s= seatList.get(i).split(":");
            seatInfo += "【" + (Integer.parseInt(s[0])+1) + "排" + (Integer.parseInt(s[1])+1) + "座】";
            seatserviceInfo += s[0]+","+s[1];//逗号分隔座位排与列
            if(i == 2 && i < seatList.size()-1){
                seatInfo += "\n";
            }
            if(i != seatList.size()-1){
                seatserviceInfo += ";";//;分隔一个座位
            }
        }
        payReasultState = intent.getStringExtra("pay_state");
        accountTextView.setText(trickCount+" ￥");
        movieNameTextView.setText(movieShow.getMovie().getmName());
        movieCityHallTextView.setText(movieShow.getMoviehall().getMovieCity().getcName() + " " +movieShow.getMoviehall().gethName());
        seatTextView.setText(seatInfo);
        userPhoneTextView.setText(userPhone);
        codeTextView.setText(orderCode);
        if(payReasultState.equals("succeed")){
            succeedLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            payResultIcon.setImageResource(R.drawable.order_pay_succeed200);
        }else{
            succeedLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            payResultIcon.setImageResource(R.drawable.order_pay_error200);
        }
    }
    /*访问服务器将数据存入用户订单数据库中*/
    public void saveOrderToInternet(){
        String url = HttpUtil.HttpUrl+"/order/android_insert_order.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Toast.makeText(PayResultActivity.this, "成功", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PayResultActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("order_code",orderCode);
                map.put("user_id",s.getInt("user_id",1)+"");
                map.put("movie_show_id",movieShow.getsId()+"");
                map.put("order_price",trickCount);
                map.put("seat_info",seatserviceInfo);
                map.put("seat_service_info",seatserviceInfo);
                map.put("order_state",payReasultState);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void toMainActivity(View v){
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    /*限制订单支付结果返回到订单界面*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
