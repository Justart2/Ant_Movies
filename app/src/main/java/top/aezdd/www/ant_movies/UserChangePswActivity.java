package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import top.aezdd.www.utils.HttpUtil;

public class UserChangePswActivity extends Activity {
    @ViewInject(R.id.ant_user_change_psw_old)EditText oldPsw;
    @ViewInject(R.id.ant_user_change_psw_new)EditText newPsw;
    @ViewInject(R.id.ant_user_change_psw_new_re)EditText newPswRe;
    private SweetAlertDialog pDialog;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_psw);
        ViewUtils.inject(this);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("ant_user_info",MODE_PRIVATE);
    }
    /*表单button点击事件*/
    public void changeUserPswClick(View view){
        toChangePsw();
    }
    /*表单验证、访问网络业务处理*/
    public void toChangePsw(){
        if("".equals(oldPsw.getText().toString().trim())||
                "".equals(newPsw.getText().toString().trim())||
                "".equals(newPswRe.getText().toString().trim())){
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }else if(!oldPsw.getText().toString().trim().equals(sharedPreferences.getString("user_psw",""))){
            Toast.makeText(this, "旧密码不正确！", Toast.LENGTH_SHORT).show();
            return;
        }else if(!newPswRe.getText().toString().trim().equals(newPsw.getText().toString().trim())){
            Toast.makeText(this, "前后两次输入的密码不一样！", Toast.LENGTH_SHORT).show();
            return;
        }else{
            //开始dialog
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("修改中····");
            pDialog.setCancelable(false);
            pDialog.show();
            //执行修改服务器上用户密码
            changePswtoInternet();
        }
    }
    /*访问网络业务*/
    public void changePswtoInternet(){
        String url = HttpUtil.HttpUrl+"/user/modify_psw.do";
        StringRequest sRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.cancel();
                if(s.equals("succeed")){
                    Toast.makeText(UserChangePswActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    //修改Sharedpreference中用户密码
                    sharedPreferences.edit().putString("user_psw",newPsw.getText().toString().trim()).commit();
                    //清除文本框输入记录
                    oldPsw.setText("");
                    newPsw.setText("");
                    newPswRe.setText("");
                }else{
                    Toast.makeText(UserChangePswActivity.this,"系统错误，请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.cancel();
                Toast.makeText(UserChangePswActivity.this,"系统错误或者网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                //Log.e("dddddd",newPsw.getText().toString().trim());
                map.put("user_new_psw",newPsw.getText().toString().trim());
                map.put("user_id",sharedPreferences.getInt("user_id",-1)+"");
                return map;
            }
        };
        sRequest.setTag("user_change_psw_request");
        requestQueue.add(sRequest);
    }

    /*关闭当前Activity，放回上一个Activity*/
    public void exitActivity(View c){
        finish();
    }

    /*Volley与Activity生命周期的联动，Activity停止，请求取消*/
    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll("user_change_psw_request");
    }
}
