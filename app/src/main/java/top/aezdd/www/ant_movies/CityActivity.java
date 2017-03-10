package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class CityActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
    }

    public void exitChoseCity(View v){
        finish();
    }
}
