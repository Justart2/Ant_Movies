package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.entity.User;
import top.aezdd.www.fragment.LoginByAccountFragment;
import top.aezdd.www.fragment.LoginByPhoneFragment;
import top.aezdd.www.utils.CloudInfSMSUtils;
import top.aezdd.www.utils.DynamicPermission;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.utils.MultipartRequest;
import top.aezdd.www.utils.OtherUtils;

public class AntLoginActivity extends Activity implements LoginByAccountFragment.LoginByAccountInterface, LoginByPhoneFragment.LoginByPhoneInterface {
    private FragmentManager fragmentManager;
    private RequestQueue requestQueue;
    private final static String TAG = "login_request";
    private SweetAlertDialog pDialog;
    private SweetAlertDialog pDialog1;
    private EditText antAccountText;
    private EditText phoneText;
    private EditText codeText;
    private EditText pswText;
    private TextView sendCodeBtn;
    private SharedPreferences sharedPreference;
    //短信验证--接口地址
    private final static String url = "http://www.etuocloud.com/gateway.action";
    //应用 app_key
    private final static String APP_KEY = "OtVTPBKRyV3I7nggseEyevQJJcCSxJAW";
    //应用 app_secret
    private final static String APP_SECRET = "2Lb6jcnFtexuQJgSbX5t8XlQh6kSyszzOOXHuxwKkY9y4gYR4VVXjkMSegClPzIn";
    //接口响应格式 json或xml
    private final static String FORMAT = "json";
    private static String smsCode;
    //QQ登录接口
    private final static String appId = "1106107494";
    String openidString = "";
    String nicknameString = "";
    String imagePath = "";
    String imageUrl = "";
    private static Tencent mTencent = null;

    /*Xutils组件初始化*/
    @ViewInject(R.id.login_zh_label)
    TextView loginZLabel;
    @ViewInject(R.id.login_zh_nav)
    TextView loginZNav;
    @ViewInject(R.id.login_phone_label)
    TextView loginPLabel;
    @ViewInject(R.id.login_phone_nav)
    TextView loginPNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_login);
        ViewUtils.inject(this);
        initView();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.login_frame_layout, new LoginByAccountFragment(), "LoginByAccountFragment").commit();
    }

    /*初始化布局view*/
    public void initView() {
        /*volley的RequestQueue队列初始化*/
        requestQueue = Volley.newRequestQueue(this);
        sharedPreference = getSharedPreferences("ant_user_info", MODE_PRIVATE);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#85B84F"));
        pDialog.setTitleText("登录中…");
        pDialog.setCancelable(false);

    }

    /*访问网络*/
    public void getHttpData(String url,final EditText name,final EditText psw) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功返回字符串s
                Log.i(TAG, s);
                if (s.equals("error")) {
                    Toast.makeText(AntLoginActivity.this, "用户名或密码有误", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    User user = gson.fromJson(s, User.class);
                    SharedPreferences.Editor edit = sharedPreference.edit();
                    edit.putString("user_phone", user.getuPhone());
                    edit.putString("user_name", user.getuName());
                    edit.putString("user_psw", user.getuPsw());
                    edit.putString("user_picture", user.getuPicture());
                    edit.putString("user_email", user.getuMail());
                    edit.putInt("user_id", user.getuId());
                    edit.commit();
                    //Toast.makeText(AntLoginActivity.this, user.getuPhone(), Toast.LENGTH_SHORT).show();
                    //执行登录成功后的跳转操作。
                    finish();
                }
                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //请求失败返回错误信息
                Toast.makeText(AntLoginActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_code", name.getText().toString().trim());
                map.put("user_psw", psw.getText().toString().trim());
                return map;
            }
        };
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    public void initLoginAccountView() {
        Fragment fragment = fragmentManager.findFragmentByTag("LoginByAccountFragment");
        View view = fragment.getView();
        antAccountText = (EditText) view.findViewById(R.id.login_user_account_input);
        pswText = (EditText) view.findViewById(R.id.login_user_psw_input);
        Button loginBtn = (Button) view.findViewById(R.id.login_ant_account_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!antAccountText.getText().toString().trim().equals("") &&
                        !pswText.getText().toString().trim().equals("")) {
                    pDialog.show();
                    //提交数据网络验证
                    String url = HttpUtil.HttpUrl + "/user/android_login.do";
                    getHttpData(url,antAccountText,pswText);
                } else {
                    Toast.makeText(AntLoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    public void initLoginByPhoneView() {
        Fragment fragment = fragmentManager.findFragmentByTag("LoginByPhoneFragment");
        View view = fragment.getView();
        phoneText = (EditText) view.findViewById(R.id.login_phone_num_text);
        codeText = (EditText) view.findViewById(R.id.login_phone_code_text);
        Button loginBtn = (Button) view.findViewById(R.id.login_phone_btn);
        sendCodeBtn = (TextView) view.findViewById(R.id.login_phone_send_code_btn);
        //发送验证码
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneText.getText().toString().trim().length() != 11) {
                    Toast.makeText(AntLoginActivity.this, "请输入正确手机号码", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AntLoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (codeText.getText().toString().trim().equals(smsCode)) {
                    //验证码正确之后操作
                    pDialog.show();
                    getHttpData(HttpUtil.HttpUrl+"/user/android_login_by_phone.do",phoneText,codeText);

                } else {
                    //验证码错误的提示
                    Toast.makeText(AntLoginActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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
                        Toast.makeText(AntLoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        new Thread(new SendInfoThread()).start();
                    } else {
                        Toast.makeText(AntLoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error---->", volleyError.getMessage());
                Toast.makeText(AntLoginActivity.this, "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll(TAG);
    }
    /*跳入注册界面*/
    public void toRegister(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void exitActivity(View v) {
        finish();
    }

    public void loginByAccountClick(View v) {
        loginZLabel.setTextColor(0xFF3F5060);
        loginPLabel.setTextColor(0xFF888888);
        loginZNav.setBackgroundColor(0xFF3F5060);
        loginPNav.setBackgroundColor(0xFFDDDDDD);
        fragmentManager.beginTransaction().replace(R.id.login_frame_layout, new LoginByAccountFragment(), "LoginByAccountFragment").commit();
    }

    public void loginByPhoneClick(View v) {
        loginZLabel.setTextColor(0xFF888888);
        loginPLabel.setTextColor(0xFF3F5060);
        loginPNav.setBackgroundColor(0xFF3F5060);
        loginZNav.setBackgroundColor(0xFFDDDDDD);
        fragmentManager.beginTransaction().replace(R.id.login_frame_layout, new LoginByPhoneFragment(), "LoginByPhoneFragment").commit();
    }

    /**当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法，
     * onComplete  onCancel onError
     *分别表示第三方登录成功，取消 ，错误。*/
    private class BaseUiListener implements IUiListener {

        public void onCancel() {
            pDialog1.cancel();
            Toast.makeText(AntLoginActivity.this, "您已取消登录！", Toast.LENGTH_SHORT).show();
        }
        public void onComplete(Object response) {

            try {
                // 获得的数据是JSON格式的，获得你想获得的内容
                // 如果你不知道你能获得什么，看一下下面的LOG
                initOpenidAndToken((JSONObject) response);
                Log.e(TAG, "-------------"+response.toString());
                openidString = ((JSONObject) response).getString("openid");
                //openidTextView.setText(openidString);
                Log.e(TAG, "-------------"+openidString);
                pDialog1.cancel();

                //access_token= ((JSONObject) response).getString("access_token");              //expires_in = ((JSONObject) response).getString("expires_in");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**到此已经获得OpneID以及其他你想获得的内容了
             QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
             sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
             如何得到这个UserInfo类呢？  */
            pDialog.show();
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);
            //这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON
            info.getUserInfo(new IUiListener() {

                public void onComplete(Object responses) {

                    JSONObject response = (JSONObject) responses;
                    if (response.has("nickname")) {
                        try {
                            nicknameString=response.getString("nickname");
                            imageUrl = response.getString("figureurl_qq_2");
                            Log.e(TAG, "--"+nicknameString+" "+imageUrl);
                            /**由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接
                             */
                            ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {

                                    Log.e("bitmap",bitmap.toString());
                                    imagePath = OtherUtils.saveBitmap(AntLoginActivity.this,bitmap);
                                    //Log.e("img",imagePath.toString());
                                    //设置访问延时
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            uploadImgToInternet(imagePath);
                                        }
                                    },1000);
                                }
                            }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(AntLoginActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                                    pDialog.cancel();
                                }
                            });
                            requestQueue.add(imageRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e(TAG, "-----111---"+response.toString());


                }
                public void onCancel() {
                    pDialog.cancel();
                    Log.e(TAG, "--------------111112");
                }
                public void onError(UiError arg0) {
                    pDialog.cancel();
                    Log.e(TAG, "-111113"+":"+arg0);
                }
            });
        }
        public void onError(UiError arg0) {
            pDialog1.cancel();
            Toast.makeText(AntLoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
        }
    }
    /*上传第三方登录信息到服务器*/
    public void uploadImgToInternet(String imgFileAbsolutePath){
        //构造参数列表
        List<Part> partList = new ArrayList<Part>();

        try {
            partList.add(new StringPart("user_name", URLEncoder.encode(nicknameString,"utf-8")));
            partList.add(new StringPart("user_third_part", openidString));
            partList.add(new FilePart("user_img", new File(imgFileAbsolutePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = HttpUtil.HttpUrl+"/user/android_third_login.do";
        //生成请求
        MultipartRequest profileUpdateRequest = new MultipartRequest(url, partList.toArray(new Part[partList.size()]), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("ssss--QQ",s);
                Gson gson = new Gson();
                User user = gson.fromJson(s,User.class);
                sharedPreference = getSharedPreferences("ant_user_info",MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreference.edit();
                edit.putString("user_phone", user.getuPhone());
                edit.putString("user_name", user.getuName());
                edit.putString("user_psw", user.getuPsw());
                edit.putString("user_picture", user.getuPicture());
                edit.putString("user_email", user.getuMail());
                edit.putInt("user_id", user.getuId());
                edit.commit();
                Toast.makeText(AntLoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                pDialog.cancel();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //处理失败错误信息
                Log.e("MultipartRequest", error.getMessage(), error);
                Toast.makeText(getApplication(), "网络错误！", Toast.LENGTH_SHORT).show();
                pDialog.cancel();
            }
        });
        //将请求加入队列
        requestQueue.add(profileUpdateRequest);
    }
    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }

        } catch(Exception e) {
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,new BaseUiListener());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //QQ登录
    public void QQLogin(View v){
        //申请动态权限
        if(DynamicPermission.checkPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE})){
            loginQQDetail();
        }

    }
    //动态权限申请结束
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length >0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    //Toast.makeText(this, "您已授权，请重新选择！", Toast.LENGTH_SHORT).show();
                    loginQQDetail();
                }else{
                    //用户拒绝授权
                    Toast.makeText(this, "您拒绝访问！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void loginQQDetail(){
        pDialog1 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog1.getProgressHelper().setBarColor(Color.parseColor("#85B84F"));
        pDialog1.setTitleText("正在进入QQ…");
        pDialog1.setCancelable(false);
        pDialog1.show();
        mTencent = Tencent.createInstance(appId,getApplicationContext());
        mTencent.login(AntLoginActivity.this,"all", new BaseUiListener());
    }
    //新浪微博登录
    public void sinaLogin(View v){
        new SweetAlertDialog(this)
                .setTitleText("敬请期待！")
                .show();
    }
    //微信登录
    public void wxLogin(View v){
        new SweetAlertDialog(this)
                .setTitleText("敬请期待!")
                .show();
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
}
