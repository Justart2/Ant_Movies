package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import top.aezdd.www.entity.MovieShow;

public class OrderActivity extends Activity {
    ArrayList<String> seatList;
    MovieShow movieShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        seatList = (ArrayList)intent.getSerializableExtra("a_movie_show_seat");
        movieShow = (MovieShow)intent.getSerializableExtra("a_movie_show_info");
        Toast.makeText(OrderActivity.this, seatList.size()+"", Toast.LENGTH_SHORT).show();
    }
    public void exitActicity(View view){
        finish();
    }
}
