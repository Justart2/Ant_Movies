package top.aezdd.www.ant_movies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.entity.MovieShow;
import top.aezdd.www.entity.User;
import top.aezdd.www.pay.alipay.OrderInfoUtil2_0;
import top.aezdd.www.pay.alipay.PayResult;
import top.aezdd.www.utils.OtherUtils;

public class OrderActivity extends Activity {
    private ArrayList<String> seatList;
    private MovieShow movieShow;
    private User user;
    private TextView movieNameText;
    private TextView movieTypeText;
    private TextView movieShowTimeText;
    private TextView movieSeatText;
    private TextView movieCityAndHallText;
    private String seatStr = "";
    private double amountPay;
    private String orderCode = "";
    /**
     * XUtils加载view组件
     * */
    @ViewInject(R.id.order_user_name)TextView username;
    @ViewInject(R.id.order_user_phone)TextView userPhone;
    @ViewInject(R.id.order_movie_discount)TextView movieDiscount;
    @ViewInject(R.id.order_tickets_count) TextView movieTicketCount;
    @ViewInject(R.id.order_tickets_price)TextView ticketPrice;
    @ViewInject(R.id.order_tickets_discount)TextView ticketDiscount;
    @ViewInject(R.id.order_count_price)TextView countPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        ViewUtils.inject(this);
        initView();
        setData();
    }
    public void initView(){
        Intent intent = getIntent();
        seatList = (ArrayList)intent.getSerializableExtra("a_movie_show_seat");
        movieShow = (MovieShow)intent.getSerializableExtra("a_movie_show_info");

        for(int i = 0;i<seatList.size();i++){
            String[] s= seatList.get(i).split(":");
            seatStr += "【" + (Integer.parseInt(s[0])+1) + "排" + (Integer.parseInt(s[1])+1) + "座】";
            if(i == 2 && i < seatList.size()-1){
                seatStr += "\n";
            }
        }
        //Toast.makeText(OrderActivity.this, seatStr, Toast.LENGTH_SHORT).show();
        movieNameText = (TextView)findViewById(R.id.order_movie_name_text);
        movieTypeText = (TextView)findViewById(R.id.order_movie_type_text);
        movieShowTimeText = (TextView)findViewById(R.id.order_movie_show_time_text);
        movieCityAndHallText = (TextView)findViewById(R.id.order_movie_city_hall_text);
        movieSeatText = (TextView)findViewById(R.id.order_movie_chose_seat_text);
    }
    /*public String getSharedPreferencesData(){
        SharedPreferences sharedPreferences = getSharedPreferences("ant_movies_city",MODE_PRIVATE);
        String movieCityName = sharedPreferences.getString("movie_city_name","");
        return movieCityName;
    }*/
    /*设置确认订单信息*/
    public void setData(){
        /*设置影票信息*/
        movieNameText.setText(movieShow.getMovie().getmName());
        movieTypeText.setText(movieShow.getMovie().getmVersion());
        movieShowTimeText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(movieShow.getsTime())));
        movieCityAndHallText.setText(movieShow.getMoviehall().getMovieCity().getcName()+"  "+ movieShow.getMoviehall().gethName());
        movieSeatText.setText(seatStr);
        /*设置用户信息*/
        SharedPreferences s = getSharedPreferences("ant_user_info",MODE_PRIVATE);
        username.setText(s.getString("user_name",""));
        userPhone.setText(s.getString("user_phone",""));
        /*设置折扣信息*/
        movieDiscount.setText("本场电影 "+ movieShow.getsOnSale() +" 折，祝您观影愉快O(∩_∩)O");
        //<font color='#FF0000'><small>红颜色</small></font>  设置textview部分字体大小和颜色
        /*设置结算信息*/
        movieTicketCount.setText(seatList.size()+"");
        ticketPrice.setText(movieShow.getMovie().getmPrice()+" 元");
        ticketDiscount.setText(movieShow.getsOnSale()+"");
        if(movieShow.getsOnSale()==0){
            amountPay = seatList.size()*movieShow.getMovie().getmPrice();
        }else{
            amountPay = seatList.size()*movieShow.getMovie().getmPrice()*movieShow.getsOnSale()/10;
        }

        countPrice.setText(amountPay+"");

    }
    /**
     *支付点击事件
     * @ 1.支付宝
     * @ 2.微信
     * @ 3.中国银联
     * */

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016072800111998";
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCIHLULKxa5vSgOXewTjq78XYV4ue+4SRUxeifzTtB07zGJ0IfrcKBHUWw036l46nipNubxjX0vj4WtswkfZaBm4HSl28oGwdtDWfu1x+DaWdGzBQYvhuqp5Sh6I9NeuMo0VW47WYGWg3IP8HNGapnqpwRFZkbY9hgh0ANSQTNJNea18UKU7coXclX525EBTTuAzry+yYCDMNJtXU+MhjQgahXpxGwyshXFgrMrDaEwyFKXU+F7mmpO1nq868tmbD8ni2NKIJ0YSscqmfKcm6RsgThwpkhxGHxAL+q9SDmv3JCMsEoND3wIDoWq2ud4CVwgg7geWCTkyWLOszJXh5LFAgMBAAECggEARCHCif2emYjS1SY0drG0a8TQe9bu5NtITYTtHIB2HiJhNK/6aWLuB+T8+hZ++XS8w08lGQKPCFCaRJFa3ql7OiPen+s93FYLUFJpidusdQAC1M6OAxpQuoxnz4PBWSe8p6efzvuFv3a6kLn2qefQdcH2hFqm2hGNkNdT4keayWkD2RR24N58xl4Qb4kSsCzy0pRNFoI8+4dzzcPvS6T7M1JZ2UlvKLIAEm5WnuAaJLZUeRoYlJMMWU0Xzc2BPfJTu0K7krFiKXHXAdYyuEoje/36TTYIFW+hi+gHuj8C3cbzP2/C/gyqr7rRhDQS3eXsbuB4L6LqERz5iLY25EICgQKBgQC8jX+wm8okdAuUgBylX9W2yiXdk5TUvZZzi0gmZ7aKE8mr1tfPimXu0l9KpOJZYlssOcSHx84URMpJQIdeOFEPYdf2UUcYz6F6mFzsSVKaZV/bobyVl3//DTo9fuljz6REI20hMYcYPFMtKFyzzr79hfvsK6q4dxjmLtq4e5i7IQKBgQC4zQVfVRxUdyfeDEmLHN6S7X7GANxCWIsGpya38l+wuhcrZ8OFpLDgTLKzADaOglww5abficU6P2HkgNqE9FS/xuYmpFQJKJiQgT+GfUMwK9fTejuCnJqwmQcE9nYk6cvlsi4QGRFIwMGCPGNJTRdwqbWalLxgswZcgcGtPiKnJQKBgQC6UTh0U/2Tt/gZbd7gq6qLTYXNRiAzeIRK+L769FE0cAlAfLQUqTXAoLD5GaVxnfdp5EuHwLCq5q7dY55gRFYVVSIKjJYstj9i3Vw1QN8XIkVuyouHZoVcir2aNuZ5cp1nl3om6sIL+NBYNyIjq5CfBN7DqQKx9T3tdy86TSfHAQKBgDN+BRI0L+mlLfmkKNXXXHlKLbnUQYtHCq3240X8BgfDQnN4vTU+134YQO65j3oT/sYtJ1tAEzGQuaEm+jxzrC2nqTd5MGVA54nUSbQasR76CHBHrc9N/ZhZe2g5mF/euU6uYEFiCrvf4ct9MhkZnv9oek6xRPAY0hvyl9nC53r1AoGAcvqzmMUYRHimgpgn+ZeBkDBpjBKjsUJWKMrd8RQb/kZpcY6FjumYgskQK+OkeGTlAqjDFDh8zYDl7Dkx0275lv1o2ebgVbwXgeGvjFaLRaRtrLJgMQXYqNiuKKtFRGrS593RiGo2Fs+c5enAqMcZQ56d7OIm7dfQKqhRFYCnpOM=";
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Intent intent = new Intent(OrderActivity.this,PayResultActivity.class);
                        intent.putExtra("movie_show",movieShow);
                        intent.putExtra("seat_info",seatList);
                        intent.putExtra("account_price",amountPay);
                        intent.putExtra("order_code",orderCode);
                        intent.putExtra("pay_state","succeed");
                        startActivity(intent);
                        //Toast.makeText(OrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Intent intent = new Intent(OrderActivity.this,PayResultActivity.class);
                        intent.putExtra("movie_show",movieShow);
                        intent.putExtra("seat_info",seatList);
                        intent.putExtra("account_price",amountPay);
                        intent.putExtra("order_code",orderCode);
                        intent.putExtra("pay_state","error");
                        startActivity(intent);
                        //Toast.makeText(OrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    public void toAliPay(View v){
        orderCode = OtherUtils.getOrderCode();
        String payTitle = "“"+movieShow.getMovie().getmName()+"” 订单支付";
        String price = amountPay+"";
        String payContent = movieShow.getMovie().getmName()+" 影价支付";
        OrderInfoUtil2_0.setOrderParamStr(orderCode, price, payTitle, payContent);
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    public void toWXPay(View v){
        new SweetAlertDialog(this)
                .setTitleText("敬请期待！")
                .show();
    }

    public void toCBUPay(View v){
        new SweetAlertDialog(this)
                .setTitleText("敬请期待！")
                .show();
    }

    public void exitActivity(View view){
        finish();
    }
}
