package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChoseLoginTypeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_login_type);
    }
    public void loginByPhone(View v){
        /*Intent intent = new Intent(ChoseLoginTypeActivity.this,LoginActivity.class);
        startActivity(intent);*/
    }
    public void loginByAnt(View v){

    }public void loginByOther(View v){

    }

}
