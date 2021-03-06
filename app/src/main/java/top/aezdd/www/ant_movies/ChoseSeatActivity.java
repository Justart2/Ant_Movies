package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.aezdd.www.entity.MovieShow;
import top.aezdd.www.entity.User;
import top.aezdd.www.utils.LoginUtil;
import top.aezdd.www.view.MovieSeatView;

public class ChoseSeatActivity extends Activity {
    private SharedPreferences s;
    private MovieShow movieShow ;
    List<String> seatTemp = new ArrayList<>();
    Map<Integer,String> map = null;
    List<String> list = new ArrayList<String>();
    private User user;
    private GridView movieGridView;
    @ViewInject(R.id.chose_seat_a_movie_show_name)TextView movieName;
    @ViewInject(R.id.chose_seat_movie_show_time)TextView movieSHowTime;
    @ViewInject(R.id.chose_seat_movie_show_city_hall)TextView movieCityHall;
    @ViewInject(R.id.chose_seat_result)TextView movieChosedSeat;
    @ViewInject(R.id.choose_seat_view)MovieSeatView movieSeatView;
    @ViewInject(R.id.chose_seat_next_stage_text)TextView nextStage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_seat);
        ViewUtils.inject(this);
        initViewAndData();
    }
    public void initViewAndData(){
        s = getSharedPreferences("ant_movies_city",MODE_PRIVATE);
        Intent intent = getIntent();
        movieShow = (MovieShow)intent.getSerializableExtra("a_movie_show_for_chose_seat");
        movieName.setText(movieShow.getMovie().getmName());

        Date date = new Date(movieShow.getsTime());
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 HH:mm");
        movieSHowTime.setText(sf.format(date));

        movieCityHall.setText(s.getString("movie_city_name","")+" "+movieShow.getMoviehall().gethName());
        //getSeatInfo();
        movieSeatView.setData(getSeatInfoToStringArray());
        //movieGridView.setAdapter(new SeatGridViewAdapter());
        //Toast.makeText(ChoseSeatActivity.this, list.size()+"", Toast.LENGTH_SHORT).show();
        movieSeatView.setChooseSeatListener(new MovieSeatView.ChooseSeatListener() {
            @Override
            public void chooseSeatResult(List<ArrayList<Integer>> chooseList) {
                seatData.removeAll(seatData);
                String seatstr = "";
                for(int i = 0;i<chooseList.size();i++){

                    seatData.add((chooseList.get(i).get(0)) + ":" + (chooseList.get(i).get(1)));
                    if(chooseList.size()>3&&i == 3 ){
                        seatstr += "\n";
                    }
                    seatstr += "【" + (chooseList.get(i).get(0) +1) + "排，" + (chooseList.get(i).get(1)+1) + "座】";

                }
                movieChosedSeat.setText(seatstr);

                if (seatData.size() != 0) {
                    nextStage.setBackgroundColor(Color.parseColor("#FF4400"));
                    nextStage.setClickable(true);
                } else {
                    nextStage.setBackgroundColor(Color.parseColor("#BBBBBB"));
                    nextStage.setClickable(false);
                }
            }
        });
    }
    private String[][] seatStr;
    private List<String> seatData = new ArrayList<>();
    public String[][] getSeatInfoToStringArray(){

        String str = movieShow.getMoviehall().gethSeat();
        String s1[] = str.split(";");
        seatStr = new String[s1.length][s1[0].length()];
        for(int i=0;i<s1.length;i++){
            for(int j=0;j<s1[i].length();j++){
               //list.add(s1[i].charAt(j)+"");
                seatStr[i][j] = s1[i].charAt(j)+"";
            }
        }
        return seatStr;
    }
    class SeatGridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.gridview_chose_seat_layout,parent,false);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.girdview_chose_seat_image);
            if(list.get(position).equals("1")){
                imageView.setImageResource(R.drawable.cinema_seat_sold_small);
            }else if(list.get(position).equals("0")){
                imageView.setImageResource(R.drawable.cinema_seat_selectable_small);
            }else{
                imageView.setImageResource(R.drawable.cinema_not_seat);
            }

            movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageView imageView = (ImageView) view.findViewById(R.id.girdview_chose_seat_image);
                    if (list.get(position).equals("0")) {
                        if (map == null) {
                            map = new HashMap<Integer, String>();
                            int i = 0;
                            imageView.setImageResource(R.drawable.cinema_seat_selected_small);
                            map.put(position, position / 19 + ":" + position % 19 + ":" + i);
                            seatTemp.add(position / 19 + ":" + position % 19);
                        } else if (map.get(position) == null) {
                            if (seatTemp.size() == 6) {
                                Toast.makeText(ChoseSeatActivity.this, "您最多只能选6个座位", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            int i = 0;
                            imageView.setImageResource(R.drawable.cinema_seat_selected_small);
                            map.put(position, position / 19 + ":" + position % 19 + ":" + i);
                            seatTemp.add(position / 19 + ":" + position % 19);
                        } else {
                            String str[] = map.get(position).split(":");
                            if (str[2].equals("0")) {
                                imageView.setImageResource(R.drawable.cinema_seat_selectable_small);
                                seatTemp.remove(position / 19 + ":" + position % 19);
                                map.put(position, position / 19 + ":" + position % 19 + ":" + 1);
                            } else {
                                if (seatTemp.size() == 6) {
                                    Toast.makeText(ChoseSeatActivity.this, "您最多只能选6个座位", Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                                imageView.setImageResource(R.drawable.cinema_seat_selected_small);
                                seatTemp.add(position / 19 + ":" + position % 19);
                                map.put(position, position / 19 + ":" + position % 19 + ":" + 0);
                            }
                        }
                        if (seatTemp.size() != 0) {
                            nextStage.setBackgroundColor(Color.parseColor("#FF4400"));
                            nextStage.setClickable(true);
                        } else {
                            nextStage.setBackgroundColor(Color.parseColor("#BBBBBB"));
                            nextStage.setClickable(false);
                        }
                        String ss = "";
                        for (int i = 0; i < seatTemp.size(); i++) {
                            if (i == 3) {
                                ss += "\n";
                            }
                            ss += "【" + (Integer.parseInt(seatTemp.get(i).split(":")[0]) + 1) + "排，" + (Integer.parseInt(seatTemp.get(i).split(":")[1]) + 1) + "座】";
                        }
                        movieChosedSeat.setText(ss);

                    }

                }
            });
            return convertView;
        }
    }

    public void toNextOrder(View v){
        if(checkUserLogined()){
            Intent intent = new Intent();
            intent.setClass(this,OrderActivity.class);
            intent.putExtra("a_movie_show_info", movieShow);
            intent.putExtra("a_movie_show_seat",(Serializable)seatData);
            startActivity(intent);
        }else{
            //去登陆
            LoginUtil.toLogin(this);
        }

    }
    /*验证用户是否已登录*/
    public boolean checkUserLogined(){
        SharedPreferences s = getSharedPreferences("ant_user_info",MODE_PRIVATE);
        String userPhone = s.getString("user_phone","null");
        //Log.e("sssss_user_info------>",userPhone);
        if(userPhone.equals("null")){
            return false;
        }else{
            return true;
        }
    }
    public void exitActivity(View v){
        finish();
    }
}
