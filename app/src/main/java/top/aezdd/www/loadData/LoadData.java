package top.aezdd.www.loadData;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.antlistview.view.XListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.aezdd.www.adapter.LiveItemAdapter;
import top.aezdd.www.adapter.MovieIndexAdapter;
import top.aezdd.www.adapter.MoviesAdapter;
import top.aezdd.www.adapter.MoviesGridAdapter;
import top.aezdd.www.adapter.NowsAdapter;
import top.aezdd.www.ant_movies.MainActivity;
import top.aezdd.www.entity.Movie;
import top.aezdd.www.entity.NowsEntity;

/**
 * Created by jianzhou.liu on 2017/3/13.
 */
public class LoadData {
    private String TAG = "volley_ant_movies_tag";
    private List list ;
    private Context mContext;
    private ListView mListView;
    private XListView xListView;
    private GridView mGridView;
    //add recycleview
    private RecyclerView mRecycleView;

    /**
     * @获取聚合数据新闻api
     */
    public List<NowsEntity> getNowsInternetData(Context context, ListView listView, RequestQueue requestQueue,String tag) {
        mListView = listView;
        mContext = context;
        list = new ArrayList<>();
        String url = "http://v.juhe.cn/toutiao/index";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        NowsEntity nows = new NowsEntity();
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        nows.setAuthor_name(jsonObject3.getString("author_name"));
                        nows.setTitle(jsonObject3.getString("title"));
                        nows.setDate(jsonObject3.getString("date"));
                        nows.setUrl(jsonObject3.getString("url"));
                        nows.setCategory(jsonObject3.getString("category"));
                        nows.setThumbnail_pic_s(jsonObject3.getString("thumbnail_pic_s"));
                        //nows.setThumbnail_pic_s02(jsonObject3.getString("thumbnail_pic_s02"));
                        //nows.setThumbnail_pic_s03(jsonObject3.getString("thumbnail_pic_s03"));
                        list.add(nows);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mListView.setAdapter(new NowsAdapter(mContext, list));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", "top");
                map.put("key", "1ebfe70323925ba4e80dfbe84f0efa47");
                return map;
            }
        };
        stringRequest.setTag(tag);
        requestQueue.add(stringRequest);
        return list;
    }

    /**
     * volley框架加载文本数据
     */
    public List<Movie> getMoviesListInternetData(Context context, ListView  listView, RequestQueue requestQueue,String url,String tag) {
        mListView = listView;
        mContext = context;
        list = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Toast.makeText(MainActivity.this,s, Toast.LENGTH_LONG).show();

                try {
                    list.removeAll(list);
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setmId((Integer) jsonObject.get("mId"));
                        movie.setmActor(jsonObject.getString("mActor"));
                        movie.setmDirector(jsonObject.getString("mDirector"));
                        movie.setmCountry(jsonObject.getString("mCountry"));
                        movie.setmDescription(jsonObject.getString("mDescription"));
                        movie.setmName(jsonObject.getString("mName"));
                        movie.setmPicture(jsonObject.getString("mPicture"));
                        movie.setmPrice(Float.parseFloat(jsonObject.get("mPrice") + ""));
                        movie.setmRate(jsonObject.getString("mRate"));
                        movie.setmReleaseTime(jsonObject.getString("mReleaseTime"));
                        movie.setmStagePhotos(jsonObject.getString("mStagePhotos"));
                        movie.setmTimeLength(jsonObject.getString("mTimeLength"));
                        movie.setmType(jsonObject.getString("mType"));
                        movie.setmVersion(jsonObject.getString("mVersion"));
                        list.add(movie);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mListView.setAdapter(new MoviesAdapter(mContext,list));
                    //MoviesAdapter(this,moviesList)

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag(tag);
        requestQueue.add(stringRequest);
        return list;
    }

    public List<Movie> getMoviesListInternetData(Context context, GridView gridView, RequestQueue requestQueue,String url,String tag) {
        mGridView = gridView;
        mContext = context;
        list = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Toast.makeText(MainActivity.this,s, Toast.LENGTH_LONG).show();

                try {
                    list.removeAll(list);
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setmId((Integer) jsonObject.get("mId"));
                        movie.setmActor(jsonObject.getString("mActor"));
                        movie.setmDirector(jsonObject.getString("mDirector"));
                        movie.setmCountry(jsonObject.getString("mCountry"));
                        movie.setmDescription(jsonObject.getString("mDescription"));
                        movie.setmName(jsonObject.getString("mName"));
                        movie.setmPicture(jsonObject.getString("mPicture"));
                        movie.setmPrice(Float.parseFloat(jsonObject.get("mPrice") + ""));
                        movie.setmRate(jsonObject.getString("mRate"));
                        movie.setmReleaseTime(jsonObject.getString("mReleaseTime"));
                        movie.setmStagePhotos(jsonObject.getString("mStagePhotos"));
                        movie.setmTimeLength(jsonObject.getString("mTimeLength"));
                        movie.setmType(jsonObject.getString("mType"));
                        movie.setmVersion(jsonObject.getString("mVersion"));
                        list.add(movie);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mGridView.setAdapter(new MoviesGridAdapter(mContext,list));
                    //MoviesAdapter(this,moviesList)

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag(tag);
        requestQueue.add(stringRequest);
        return list;
    }

    /*
    * add recycle view adpater by justart at 2019-03-03
    * */
    public List<Movie> getMoviesListInternetData(Context context, final MovieIndexAdapter.OnItemCLickListener listener, final RecyclerView recycleView, RequestQueue requestQueue, String url, String tag) {
        mRecycleView = recycleView;
        mRecycleView.removeAllViews();
        mContext = context;
        list = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Toast.makeText(MainActivity.this,s, Toast.LENGTH_LONG).show();

                try {
                    list.removeAll(list);
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setmId((Integer) jsonObject.get("mId"));
                        movie.setmActor(jsonObject.getString("mActor"));
                        movie.setmDirector(jsonObject.getString("mDirector"));
                        movie.setmCountry(jsonObject.getString("mCountry"));
                        movie.setmDescription(jsonObject.getString("mDescription"));
                        String movieName = jsonObject.getString("mName");
                        String movieNewName = "";
                        if(movieName.length()>10){
                            for (int t = 0;t<10;t++){
                                movieNewName += movieName.charAt(t);
                            }
                            movieNewName += "...";
                        }else{
                            movieNewName = movieName;
                        }
                        //movie.setmName(jsonObject.getString("mName"));
                        movie.setmName(movieNewName);
                        movie.setmPicture(jsonObject.getString("mPicture"));
                        movie.setmPrice(Float.parseFloat(jsonObject.get("mPrice") + ""));
                        movie.setmRate(jsonObject.getString("mRate"));
                        movie.setmReleaseTime(jsonObject.getString("mReleaseTime"));
                        movie.setmStagePhotos(jsonObject.getString("mStagePhotos"));
                        movie.setmTimeLength(jsonObject.getString("mTimeLength"));
                        movie.setmType(jsonObject.getString("mType"));
                        movie.setmVersion(jsonObject.getString("mVersion"));
                        list.add(movie);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MainActivity.moviesList = list;
                    Log.d("ljz",list.size() + "");
                    mRecycleView.swapAdapter(new MovieIndexAdapter(list, MainActivity.DEFAULT_LAYOUT_TYPE).setOnClickListener(listener),false);
                    //MoviesAdapter(this,moviesList)
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag(tag);
        requestQueue.add(stringRequest);
        return list;
    }


    /**
     * volley框架加载ant_Live数据
     * url:http://open.douyucdn.cn/api/RoomApi/live
     */
    public List<Map<String,String>> getLiveListInternetData(Context context, XListView gridView, final RequestQueue requestQueue, String tag, int page) {
        xListView = gridView;
        mContext = context;
        list = new ArrayList<>();
        String url = "https://api.youku.com/quality/video/by/categories.json?client_id=a4e66b54004f1773&category=搞笑&period=week&order_by=like_count&page="+page;
        //
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Toast.makeText(MainActivity.this,s, Toast.LENGTH_LONG).show();

                try {
                    list.removeAll(list);
                    JSONObject result = new JSONObject(s);
                    JSONArray jsonArray = result.getJSONArray("data");

                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Map<String,String> map = new HashMap<>();
                        /*
                        douyu.com
                        map.put("room_id",jsonObject.getString("room_id"));
                        map.put("room_src",jsonObject.getString("room_src"));
                        map.put("cate_id",jsonObject.getString("cate_id"));
                        map.put("room_name",jsonObject.getString("room_name"));
                        map.put("nickname",jsonObject.getString("nickname"));
                        map.put("online",jsonObject.getString("online"));
                        map.put("url",jsonObject.getString("url"));
                        map.put("game_name",jsonObject.getString("game_name"));*/

                        map.put("id",jsonObject.getString("id"));
                        map.put("title",jsonObject.getString("title"));
                        map.put("thumbnail",jsonObject.getString("thumbnail"));
                        map.put("owner_name",jsonObject.getString("owner_name"));
                        map.put("published",jsonObject.getString("published"));
                        map.put("category",jsonObject.getString("category"));
                        map.put("link",jsonObject.getString("link"));
                        map.put("quality",jsonObject.getString("quality"));
                        map.put("seconds",jsonObject.getString("seconds"));
                        map.put("view_count",jsonObject.getInt("view_count")+"");
                        list.add(map);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    xListView.setAdapter(new LiveItemAdapter(mContext,list,requestQueue));
                    xListView.stopRefresh();
                    xListView.stopLoadMore();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
                xListView.stopRefresh();
                xListView.stopLoadMore();
            }
        });
        stringRequest.setTag(tag);
        requestQueue.add(stringRequest);
        return list;
    }


}