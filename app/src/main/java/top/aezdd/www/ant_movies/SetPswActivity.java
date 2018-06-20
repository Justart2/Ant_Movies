package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.utils.ActivityCollector;
import top.aezdd.www.utils.HttpUtil;

public class SetPswActivity extends Activity {
    RequestQueue requestQueue = null;
    String phone = "";
    SweetAlertDialog pDialog = null;
    @ViewInject(R.id.register_user_password)EditText userPsw;
    @ViewInject(R.id.register_user_password_re)EditText userPswRe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_psw);
        ActivityCollector.addActivity(this);
        ViewUtils.inject(this);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        phone = intent.getStringExtra("user_phone");

        /*设置dialog*/
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#08F5ED"));
        pDialog.setTitleText("注册中…");
        pDialog.setCancelable(false);
    }
    public void registerUser(View v){
        if(userPsw.getText().toString().trim().equals("")){
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        }else if(!userPswRe.getText().toString().trim().equals(userPsw.getText().toString().trim())){
            Toast.makeText(this, "前后两次输入的密码不一样！", Toast.LENGTH_SHORT).show();
        }else{
            pDialog.show();
            /*数据库添加数据*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addPswInServer();
                }
            },1500);

        }
    }

    public void addPswInServer(){
        String url = HttpUtil.HttpUrl+"/user/android_register_user.do";
        StringRequest s = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.equals("succeed")){
                    ActivityCollector.finishAll();
                    Toast.makeText(SetPswActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(SetPswActivity.this,AntLoginActivity.class);
                    startActivity(intent);*/
                }else{
                    Toast.makeText(SetPswActivity.this, "系统错误！", Toast.LENGTH_SHORT).show();
                }
                pDialog.cancel();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SetPswActivity.this, "网络开小差了！", Toast.LENGTH_SHORT).show();
                pDialog.cancel();
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("user_code",phone);
                map.put("user_psw",userPsw.getText().toString().trim());
                return map;
            }
        };
        requestQueue.add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void exitActivity(View v){
        finish();
    }
}
