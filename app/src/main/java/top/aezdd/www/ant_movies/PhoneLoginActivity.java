package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneLoginActivity extends Activity {
    private String phone;
    private String code;
    private EditText phoneText;
    private EditText codeText;
    private TextView sendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_login_by_phone);

        setView();

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


    }

    /* 验证登录 */
    public void login(View v) {
		/* 提交验证码 */

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
