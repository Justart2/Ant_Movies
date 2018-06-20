package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.utils.ActivityCollector;
import top.aezdd.www.utils.CloudInfSMSUtils;
import top.aezdd.www.utils.HttpUtil;

public class RegisterActivity extends Activity {
    private RequestQueue requestQueue;
    private final static String TAG = "login_request";
    private SweetAlertDialog pDialog;
    private EditText antAccountText;
    private EditText phoneText;
    private EditText codeText;
    private EditText pswText;
    private TextView sendCodeBtn;
    private SharedPreferences sharedPreference;
    //接口地址
    private final static String url = "http://www.etuocloud.com/gateway.action";
    //应用 app_key
    private final static String APP_KEY = "OtVTPBKRyV3I7nggseEyevQJJcCSxJAW";
    //应用 app_secret
    private final static String APP_SECRET = "2Lb6jcnFtexuQJgSbX5t8XlQh6kSyszzOOXHuxwKkY9y4gYR4VVXjkMSegClPzIn";
    //接口响应格式 json或xml
    private final static String FORMAT = "json";
    private static String smsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);
        ViewUtils.inject(this);
        requestQueue = Volley.newRequestQueue(this);
        initView();
    }

    public void initView() {
        phoneText = (EditText) findViewById(R.id.register_phone_num_text);
        codeText = (EditText) findViewById(R.id.register_phone_code_text);
        Button loginBtn = (Button) findViewById(R.id.register_phone_btn);
        sendCodeBtn = (TextView) findViewById(R.id.register_phone_send_code_btn);
        /*设置dialog*/
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#85B84F"));
        pDialog.setTitleText("验证中…");
        pDialog.setCancelable(false);
        //发送验证码
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneText.getText().toString().trim().length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请输入正确手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendSMSCode();
            }
        });
        //验证 验证码正确性来登录
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (codeText.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    pDialog.show();
                }
                if (codeText.getText().toString().trim().equals(smsCode)) {
                    //验证码正确之后操作
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getHttpData();
                        }
                    }, 1500);

                } else {
                    //验证码错误的提示
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                            pDialog.cancel();
                        }
                    }, 1000);

                }
            }
        });

    }

    //访问网络注册用户信息
    public void getHttpData() {
        String url = HttpUtil.HttpUrl + "/user/android_check_phone.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功返回字符串s
                Log.i(TAG, s);
                if (s.equals("false")) {
                    Toast.makeText(RegisterActivity.this, "您已注册过，可以直接登录！", Toast.LENGTH_SHORT).show();
                } else {
                    //执行验证成功后的跳转操作。
                    pDialog.cancel();
                    Intent intent = new Intent(RegisterActivity.this, SetPswActivity.class);
                    intent.putExtra("user_phone", phoneText.getText().toString().trim());
                    startActivity(intent);

                }
                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //请求失败返回错误信息
                Toast.makeText(RegisterActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_code", phoneText.getText().toString().trim());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /*发送验证码*/
    public void sendSMSCode() {
        final String smsC = CloudInfSMSUtils.getSMSCode();
        final String phoneNum = phoneText.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.get("result").toString().equals("0")) {
                        smsCode = smsC;
                        Log.e("smsCode---->", smsCode);
                        Toast.makeText(RegisterActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        new Thread(new SendInfoThread()).start();
                    } else {
                        Toast.makeText(RegisterActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error---->", volleyError.getMessage());
                Toast.makeText(RegisterActivity.this, "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = null;
                try {
                    params = new HashMap<String, String>();
                    params.put("app_key", APP_KEY);
                    params.put("view", FORMAT);
                    params.put("method", "cn.etuo.cloud.api.sms.simple");
                    params.put("to", phoneNum);
                    params.put("template", "1");
                    params.put("smscode", smsC);
                    params.put("sign", CloudInfSMSUtils.genSign(params));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params;
            }
        };
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    /*设置发送验证码后再次发送等待时间*/
    class SendInfoThread implements Runnable {
        int i = 60;

        @Override
        public void run() {
            loop:
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    sendCodeBtn.post(new Runnable() {
                        @Override
                        public void run() {
                            sendCodeBtn.setText(i + "s后发送");
                            sendCodeBtn.setBackgroundResource(R.color.color1);
                            sendCodeBtn.setEnabled(false);
                            i--;
                        }
                    });
                    Thread.sleep(1000);

                    if (i == 0) {

                        sendCodeBtn.post(new Runnable() {
                            @Override
                            public void run() {
                                sendCodeBtn.setText("获取验证码");
                                sendCodeBtn.setBackgroundResource(R.color.colorTheme);
                                sendCodeBtn.setEnabled(true);
                            }
                        });
                        break loop;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void exitActivity(View v) {
        finish();
    }
}
