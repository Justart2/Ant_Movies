package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.entity.UserLikeMovie;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.utils.LoginUtil;
import top.aezdd.www.view.CircleImageView;
import top.aezdd.www.view.SwipeListLayout;
import top.aezdd.www.viewholder.UserLikeMoviesViewHolder;

public class UserLikeActivity extends Activity {
    private RequestQueue requestQueue;
    private List<UserLikeMovie> list = new ArrayList<>();
    private Set<SwipeListLayout> sets = new HashSet();
    private SweetAlertDialog mDialog;
    @ViewInject(R.id.ant_user_like_list_view)ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_like);
        ViewUtils.inject(this);
        requestQueue = Volley.newRequestQueue(this);
        setData();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list.removeAll(list);
        setData();
    }

    /*访问网络删除数据*/
    public void deleteData(final int position){
        String url = HttpUtil.HttpUrl+"/like/delete_like_movie.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.equals("succeed")){
                    list.removeAll(list);
                    setData();
                    mDialog.setTitleText("Deleted!")
                            .setContentText("删除成功!")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }else{
                    Toast.makeText(UserLikeActivity.this, "系统错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.setTitleText("Error!")
                        .setContentText("出错啦，请检查网络连接!")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
                map.put("uc_id", list.get(position).getUcId()+"");
                map.put("movie_id",list.get(position).getmId()+"");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void setData(){
        String url = HttpUtil.HttpUrl+"/like/query_like_movie.do";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //list设置数据
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject evaluateJson= jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        UserLikeMovie userLikeMovie = gson.fromJson(evaluateJson.toString(),UserLikeMovie.class);
                        list.add(userLikeMovie);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //list设置适配器
                listView.setAdapter(new UserLikeMovieListAdapter(UserLikeActivity.this,list));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UserLikeActivity.this, "网络开小差了！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
                map.put("user_id", LoginUtil.getUserId(UserLikeActivity.this)+"");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /*设置布局改变监听器*/
    class MySwipeStatusListener implements SwipeListLayout.OnSwipeStatusListener{
        private SwipeListLayout slipListLayout;

        public MySwipeStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }
        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }
        @Override
        public void onStartCloseAnimation() {
        }
        @Override
        public void onStartOpenAnimation() {
        }
    }
    /*设置适配器*/
    public class UserLikeMovieListAdapter extends BaseAdapter {
        List<UserLikeMovie> mList;
        LayoutInflater inflate;
        Context mContext;
        public UserLikeMovieListAdapter(Context context, List<UserLikeMovie> list){
            inflate = LayoutInflater.from(context);
            mContext = context;
            mList = list;
        }
        @Override
        public int getCount() {
            return mList.size();
        }
        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView,ViewGroup parent) {
            UserLikeMoviesViewHolder userlikeMoviesViewHolder = null;
            if(convertView==null){
                convertView = inflate.inflate(R.layout.ant_user_like_list_item,parent,false);
                userlikeMoviesViewHolder = new UserLikeMoviesViewHolder();
                userlikeMoviesViewHolder.setUserLikeMovieImg((CircleImageView)convertView.findViewById(R.id.ant_movie_like_image));
                userlikeMoviesViewHolder.setUserLikeMovieName((TextView)convertView.findViewById(R.id.ant_movie_like_name));
                userlikeMoviesViewHolder.setUserLikeMovieContent((TextView)convertView.findViewById(R.id.ant_movie_info_like_content));
                userlikeMoviesViewHolder.setUserLikeMovieState((ImageView)convertView.findViewById(R.id.user_like_movie_state_icon));
                userlikeMoviesViewHolder.setSwipeListLayout((SwipeListLayout)convertView.findViewById(R.id.swipe_list_item_id));
                userlikeMoviesViewHolder.setDelete((TextView)convertView.findViewById(R.id.user_like_delete));
                userlikeMoviesViewHolder.setFind((TextView)convertView.findViewById(R.id.user_like_find));
                convertView.setTag(userlikeMoviesViewHolder);
            }
            Log.e("ssss===>",mList.get(position).getMovie().getmName()+"");
            userlikeMoviesViewHolder = (UserLikeMoviesViewHolder) convertView.getTag();

            userlikeMoviesViewHolder.getUserLikeMovieName().setText(mList.get(position).getMovie().getmName());
            userlikeMoviesViewHolder.getUserLikeMovieContent().setText(mList.get(position).getMovie().getmDescription());
            //设置喜欢/不喜欢图标
            int likeState = mList.get(position).getUcState();
            if(likeState==1){
                userlikeMoviesViewHolder.getUserLikeMovieState().setImageResource(R.drawable.cinema_icon_film_detail_has_liked);
            }else if(likeState==-1){
                userlikeMoviesViewHolder.getUserLikeMovieState().setImageResource(R.drawable.cinema_icon_film_detail_has_disliked);
            }else{
                userlikeMoviesViewHolder.getUserLikeMovieState().setImageResource(R.drawable.cinema_icon_film_detail_like_p);
            }

            //设置电影图片
            String url = HttpUtil.MOVIE_LOGOG_IMG_HTTP_URL+mList.get(position).getMovie().getmPicture();
            LoadImg.getUrlImageByVolley(mContext,requestQueue,url,userlikeMoviesViewHolder.getUserLikeMovieImg());
            userlikeMoviesViewHolder.getSwipeListLayout().setOnSwipeStatusListener(new MySwipeStatusListener(userlikeMoviesViewHolder.getSwipeListLayout()));
            userlikeMoviesViewHolder.getFind().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserLikeActivity.this,MovieDetailActivity.class);
                    intent.putExtra("movie_info",list.get(position).getMovie());
                    startActivity(intent);
                }
            });
            userlikeMoviesViewHolder.getDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除数据（服务器）
                    new SweetAlertDialog(UserLikeActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("确定要删除吗？")
                            .setCancelText("取消")
                            .setConfirmText("确定")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sDialog) {
                                    mDialog = sDialog;
                                    //执行删除操作
                                    deleteData(position);
                                }
                            })
                            .show();
                }
            });
            return convertView;
        }
    }
    public void exitActivity(View v){
        finish();
    }
}
