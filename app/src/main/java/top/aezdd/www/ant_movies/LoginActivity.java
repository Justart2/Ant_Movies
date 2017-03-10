package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkland.sdk.sms.SMSCaptcha;
import com.thinkland.sdk.util.BaseData;
import com.thinkland.sdk.util.CommonFun;

public class LoginActivity extends Activity {
    private String phone;
    private SMSCaptcha smsCaptcha;
    private String code;
    private EditText phoneText;
    private EditText codeText;
    private TextView sendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CommonFun.initialize(getApplicationContext(), true);
        setContentView(R.layout.activity_main);
        smsCaptcha = SMSCaptcha.getInstance();
        phoneText = (EditText) this.findViewById(R.id.phone_num);
        codeText = (EditText) this.findViewById(R.id.phone_code);
        sendInfo = (TextView) this.findViewById(R.id.send_info);

    }
    public void setView(){
        if((phoneText.getText()==null||phoneText.getText().equals(""))&&(codeText.getText()==null||codeText.getText().equals(""))){
            sendInfo.setBackgroundColor(0xFFDDDDDD);
            sendInfo.setEnabled(false);
        }else{
            sendInfo.setBackgroundColor(0xFF5FB9E4);
            sendInfo.setEnabled(true);
        }
    }
    /*点击发送验证码*/
    public void getYanzhengma(View v) {
        phone = phoneText.getText().toString().trim();
        smsCaptcha.sendCaptcha(phone, new BaseData.ResultCallBack() {

            @Override
            public void onResult(int code, String reason, String result) {
				/*
				 * code 返回码: 服务器: 0 成功; 1 错误; 本地: -2 本地网络异常; -3 服务器网络异常;-4
				 * 解析错误;-5初始化异常 reason 返回信息 成功或错误原因. result
				 * 返回结果,JSON格式.错误或者无返回值时为空.
				 */
                if (code == 0) {
                    Log.i("send", "succeed");
                    Log.i("reson", reason);
                    Log.i("result", result);
                    Toast.makeText(getApplicationContext(), "发送验证码成功",
                            Toast.LENGTH_SHORT).show();
                    Thread th = new Thread(new SendInfoThread());
                    th.start();
                } else {
                    Log.i("code", code + "");
                    Log.i("send", "failed");
                    Log.i("reson", reason);
                    Log.i("result", result);
                    Toast.makeText(getApplicationContext(), reason,
                            Toast.LENGTH_SHORT).show();
                    Thread th = new Thread(new SendInfoThread());
                    th.start();
                }
            }
        });

    }

    /* 验证登录 */
    public void login(View v) {
		/* 提交验证码 */
        code = codeText.getText().toString().trim();
        smsCaptcha.commitCaptcha(phone, code, new BaseData.ResultCallBack() {

            @Override
            public void onResult(int code, String reason, String result) {
                if (code == 0) {
                    Log.i("commit", "succeed");
                    Log.i("reson", reason);
                    Log.i("result", result);
                    Toast.makeText(getApplicationContext(), "登录成功",
                            Toast.LENGTH_SHORT).show();
                    //finish();
                } else {
                    Log.i("code", code + "");
                    Log.i("send", "failed");
                    Log.i("reson", reason);
                    Log.i("result", result);
                    Toast.makeText(getApplicationContext(), "登录失败",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /*设置发送验证码后再次发送等待时间*/
    class SendInfoThread implements Runnable {
        int i = 60;

        @Override
        public void run() {
            loop:
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    sendInfo.post(new Runnable() {
                        @Override
                        public void run() {
                            sendInfo.setText(i + "s后发送");
                            sendInfo.setEnabled(false);
                            i--;
                        }
                    });
                    Thread.sleep(1000);

                    if (i == 0) {

                        sendInfo.post(new Runnable() {
                            @Override
                            public void run() {
                                sendInfo.setText("获取验证码");
                                sendInfo.setEnabled(true);
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
