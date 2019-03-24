package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.antlistview.view.XListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.adapter.LiveItemAdapter;
import top.aezdd.www.adapter.MovieIndexAdapter;
import top.aezdd.www.adapter.MoviesAdapter;
import top.aezdd.www.adapter.MoviesGridAdapter;
import top.aezdd.www.adapter.NowsAdapter;
import top.aezdd.www.entity.Movie;
import top.aezdd.www.entity.NowsEntity;
import top.aezdd.www.fragment.LiveFragment;
import top.aezdd.www.fragment.MovieListFragment;
import top.aezdd.www.fragment.NowsFragment;
import top.aezdd.www.fragment.UserCoreFragment;
import top.aezdd.www.loadData.LoadData;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.utils.LoginUtil;
import top.aezdd.www.view.CircleImageView;

public class MainActivity extends Activity implements LiveFragment.LiveFragmentInterface, MovieListFragment.MovieListFragmentInterface, NowsFragment.NowsFragmentInterface, UserCoreFragment.UserFragmentInterface,MovieIndexAdapter.OnItemCLickListener {

    private final static String TAG = "MainActivity";
    public final static int RECYCLE_LINEAR = 1;//线性布局
    public final static int RECYCLE_GRID = 2;//网格布局
    public final static int RECYCLE_STAG = 3;//瀑布流

    public static int DEFAULT_LAYOUT_TYPE = RECYCLE_LINEAR;


    public final static int MOVIE_SHOWING = 4;//正在上映标签
    public final static int MOVIE_WILL_SHOWING = 5;//即将上映标签
    public static int CURRENT_MOVIE_TYPE = MOVIE_SHOWING;//当前选中标签

    RecyclerView recyclerView;

    private String tag = "now_movies_info";
    String url;
    private int page = 1;
    FragmentManager fragmentManager;
    LoadData loadMoviesListData;
    long exitTime = 0;
    /*设置索引栏图标*/
    @ViewInject(R.id.index_1)
    ImageView index1;
    @ViewInject(R.id.index_2)
    ImageView index2;
    @ViewInject(R.id.index_3)
    ImageView index3;
    @ViewInject(R.id.index_4)
    ImageView index4;


    TextView showingButton;
    TextView willingButton;
    ListView movies_now_list;
    GridView movies_now_grid;
    ListView nows_list;
    PullToRefreshView mPullToRefreshView;
    TextView fragMovieShowName;
    private RequestQueue requestQueue;
    public static List<Movie> moviesList = new ArrayList<Movie>();
    private List<NowsEntity> nowsList = new ArrayList<NowsEntity>();
    private List<Map<String, String>> liveListData;
    private TextView movieCity;
    private SharedPreferences s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        requestQueue = Volley.newRequestQueue(this);
        //sweet-alert-dialog插件

        //加载主页的fragment
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new MovieListFragment(), "MovieListFragment").commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //还原标题栏目
        /*
        showingButton.setBackgroundColor(0xFFEEEEEE);
        willingButton.setBackgroundColor(0xFF888888);
        willingButton.setTextColor(0xFFFFFFFF);
        showingButton.setTextColor(0xFF555555);
        */
        //加载影院信息
        s = getSharedPreferences("ant_movies_city", MODE_PRIVATE);
        String mCity = s.getString("movie_city_name", "");
        if (!mCity.equals("")) {
            movieCity.setText(mCity.substring(2, 4) + "店");
        } else {
            movieCity.setText("影院->");
        }

    }

    /**
     * volley与activity生命周期的联动
     */
    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll(tag);
        requestQueue.cancelAll("nows_volley");
    }

    /**
     * 标题栏点击事件
     */
    public void toShowingMovies(View v) {
        showingButton.setBackgroundColor(0xFFEEEEEE);
        willingButton.setBackgroundColor(0xFF888888);
        willingButton.setTextColor(0xFFFFFFFF);
        showingButton.setTextColor(0xFF555555);
        CURRENT_MOVIE_TYPE = MOVIE_SHOWING;
        url = HttpUtil.HttpUrl + "/movies/now_movies_android.do";
        LoadData l = new LoadData();
        if(DEFAULT_LAYOUT_TYPE == RECYCLE_LINEAR){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else{
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        l.getMoviesListInternetData(MainActivity.this,this, recyclerView, requestQueue, url, tag);
    }

    public void toWillingMovies(View v) {
        willingButton.setBackgroundColor(0xFFEEEEEE);
        showingButton.setBackgroundColor(0xFF888888);
        showingButton.setTextColor(0xFFFFFFFF);
        willingButton.setTextColor(0xFF555555);
        CURRENT_MOVIE_TYPE = MOVIE_WILL_SHOWING;
        url = HttpUtil.HttpUrl + "/movies/will_movies_android.do";

        if(DEFAULT_LAYOUT_TYPE == RECYCLE_LINEAR){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else{
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
        LoadData l = new LoadData();
        l.getMoviesListInternetData(MainActivity.this,this, recyclerView, requestQueue, url, tag);
    }

    /*跳转选择城市Activity*/
    public void toChoseMovieCity(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ChoseMovieCityActivity.class);
        startActivity(intent);
    }

    /**
     * @设置索引栏
     */
    public void toIndex(View v) {
        index1.setImageResource(R.drawable.cinema_home_tab_ticket_p);
        index2.setImageResource(R.drawable.cinema_home_tab_wallet_n);
        index3.setImageResource(R.drawable.cinema_home_tab_invite_n);
        index4.setImageResource(R.drawable.cinema_home_tab_member_n);
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new MovieListFragment(), "MovieListFragment").commit();
    }

    public void toMovieShow(View v) {
        index1.setImageResource(R.drawable.cinema_home_tab_ticket_n);
        index2.setImageResource(R.drawable.cinema_home_tab_wallet_p);
        index3.setImageResource(R.drawable.cinema_home_tab_invite_n);
        index4.setImageResource(R.drawable.cinema_home_tab_member_n);
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new LiveFragment(), "LiveFragment").commit();
    }

    public void toOther(View v) {
        index1.setImageResource(R.drawable.cinema_home_tab_ticket_n);
        index2.setImageResource(R.drawable.cinema_home_tab_wallet_n);
        index3.setImageResource(R.drawable.cinema_home_tab_invite_p);
        index4.setImageResource(R.drawable.cinema_home_tab_member_n);
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new NowsFragment(), "NowsFragment").commit();
    }

    public void toUser(View v) {
        index1.setImageResource(R.drawable.cinema_home_tab_ticket_n);
        index2.setImageResource(R.drawable.cinema_home_tab_wallet_n);
        index3.setImageResource(R.drawable.cinema_home_tab_invite_n);
        index4.setImageResource(R.drawable.cinema_home_tab_member_p);
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new UserCoreFragment(), "UserCoreFragment").commit();
    }

    public void changeLayout() {
        if (DEFAULT_LAYOUT_TYPE == RECYCLE_LINEAR) {
            Log.d(TAG, "switch to recycle_linear");
            switchLayoutType(RECYCLE_STAG);
        } else {
            Log.d(TAG, "switch to recycle_stag");
            switchLayoutType(RECYCLE_LINEAR);
        }
    }

    public void switchLayoutType(int switchCode) {
        switch (switchCode) {
            case RECYCLE_GRID:
                //recyclerView.setAdapter(new MovieListAdapter(movieList,RECYCLE_GRID));
                DEFAULT_LAYOUT_TYPE = RECYCLE_GRID;
                break;
            case RECYCLE_LINEAR:
                DEFAULT_LAYOUT_TYPE = RECYCLE_LINEAR;
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                if (moviesList.size() > 0) {
                    recyclerView.removeAllViews();
                    recyclerView.swapAdapter(new MovieIndexAdapter(moviesList, RECYCLE_LINEAR).setOnClickListener(this), true);
                }
                //loadMoviesListData.getMoviesListInternetData(MainActivity.this,MainActivity.this, recyclerView, requestQueue, url, tag);
                break;
            case RECYCLE_STAG:
                DEFAULT_LAYOUT_TYPE = RECYCLE_STAG;
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                if (moviesList.size() > 0) {
                    recyclerView.removeAllViews();
                    recyclerView.swapAdapter(new MovieIndexAdapter(moviesList, RECYCLE_STAG).setOnClickListener(this), true);
                }
                //loadMoviesListData.getMoviesListInternetData(MainActivity.this,MainActivity.this, recyclerView, requestQueue, url, tag);
                break;
            default:
                Log.d(TAG, "layout code error");

        }
    }

    /**
     * @实现Fragment方法
     */
    /*首页电影fragment布局控件初始化和数据加载*/
    @Override
    public void loadMovieListData() {
        moviesList.removeAll(moviesList);
        url = HttpUtil.HttpUrl + "/movies/now_movies_android.do";

        Fragment fragment = fragmentManager.findFragmentByTag("MovieListFragment");
        movieCity = (TextView) fragment.getView().findViewById(R.id.text_movie_city_name);
        /*加载SharedPreference中存的影院信息*//*
        s = getSharedPreferences("ant_movies_city", MODE_PRIVATE);
        String mCity = s.getString("movie_city_name", "");
        if(!"".equals(mCity) && mCity != null){
            movieCity.setText(mCity.substring(2, 4)+"店");
        }else{
            movieCity.setText("影院->");
        }*/
        s = getSharedPreferences("ant_movies_city", MODE_PRIVATE);
        String mCity = s.getString("movie_city_name", "");
        if (!mCity.equals("")) {
            movieCity.setText(mCity.substring(2, 4) + "店");
        } else {
            movieCity.setText("影院->");
        }
        /*pull-refresh-listview-插件*/
        showingButton = (TextView) fragment.getView().findViewById(R.id.test_movie_showing);
        willingButton = (TextView) fragment.getView().findViewById(R.id.test_movie_willing);
        //change listview to recycleview
        //movies_now_list = (ListView) fragment.getView().findViewById(R.id.movie_now_list);
        recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.movie_now_recycle_view);
        if(DEFAULT_LAYOUT_TYPE==RECYCLE_LINEAR){
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); //线性布局
        }else{
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)); //瀑布流
        }


        //movies_now_grid = (GridView)fragment.getView().findViewById(R.id.movie_now_grid);
        loadMoviesListData = new LoadData();

        loadMoviesListData.getMoviesListInternetData(MainActivity.this,MainActivity.this, recyclerView, requestQueue, url, tag);


        final ImageView switchLayoutBtn = (ImageView)fragment.getView().findViewById(R.id.switch_main_recycleview_layout_icon);
        if(DEFAULT_LAYOUT_TYPE==RECYCLE_LINEAR){
            switchLayoutBtn.setImageResource(R.drawable.tb_icon_navibar_default_right);
        }else{
            switchLayoutBtn.setImageResource(R.drawable.tb_icon_home_qrcode_normal);
        }
        switchLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DEFAULT_LAYOUT_TYPE==RECYCLE_STAG){
                    switchLayoutBtn.setImageResource(R.drawable.tb_icon_navibar_default_right);
                }else{
                    switchLayoutBtn.setImageResource(R.drawable.tb_icon_home_qrcode_normal);
                }
                changeLayout();
            }
        });
        mPullToRefreshView = (PullToRefreshView) fragment.getView().findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moviesList.removeAll(moviesList);
                        if(DEFAULT_LAYOUT_TYPE == RECYCLE_LINEAR){
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        }else{
                            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        }
                        loadMoviesListData.getMoviesListInternetData(MainActivity.this,MainActivity.this, recyclerView, requestQueue, url, tag);
                        //moviesList = loadMoviesListData.getMoviesListInternetData(MainActivity.this, movies_now_grid, requestQueue, url, tag);
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        /*设置listview的item单机事件*/
        //movies_now_grid
        /*recyclerView.set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie_info", moviesList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/
    }

    /*首页直播fragment布局控件初始化和数据加载*/
    @Override
    public void loadLiveData() {
        Fragment fragment = fragmentManager.findFragmentByTag("LiveFragment");
        View view = fragment.getView();
        final XListView xliveView = (XListView) findViewById(R.id.ant_movie_video_list_view);
        LoadData loadData = new LoadData();
        int page = (int)(Math.random()*100);
        if(liveListData==null){
            liveListData = loadData.getLiveListInternetData(MainActivity.this, xliveView, requestQueue, tag,page);
        }else{
            xliveView.setAdapter(new LiveItemAdapter(MainActivity.this,liveListData,requestQueue));
        }
        xliveView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, LiveShowWebActivity.class);
                intent.putExtra("live_video_id", liveListData.get(position-1).get("id"));
                startActivity(intent);
            }
        });
        xliveView.setPullRefreshEnable(true);
        xliveView.setPullLoadEnable(false);

        xliveView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                final LoadData loadData = new LoadData();
                if(liveListData!=null){
                    liveListData.removeAll(liveListData);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int page = (int)(Math.random()*100);
                        liveListData = loadData.getLiveListInternetData(MainActivity.this, xliveView, requestQueue, tag,page);
                    }
                },800);
            }
            @Override
            public void onLoadMore() {
            }
        });
    }

    /*首页新闻fragment布局控件初始化和数据加载*/
    @Override
    public void getNowsData() {
        Fragment fragment = fragmentManager.findFragmentByTag("NowsFragment");
        nows_list = (ListView) fragment.getView().findViewById(R.id.nows_list);
        //nows_list.setAdapter(new NowsAdapter(MainActivity.this,nowsList));
        if(nowsList.size()==0){
            nowsList = loadMoviesListData.getNowsInternetData(MainActivity.this, nows_list, requestQueue, tag);
        }else{
            nows_list.setAdapter(new NowsAdapter(MainActivity.this, nowsList));
        }
        /*设置下拉刷新插件*/
        mPullToRefreshView = (PullToRefreshView) fragment.getView().findViewById(R.id.pull_to_refresh_nows);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(nowsList!=null){
                            nowsList.removeAll(nowsList);
                        }
                        nowsList = loadMoviesListData.getNowsInternetData(MainActivity.this, nows_list, requestQueue, "nows_volley");
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        /*设置nows_list的item点击事件*/
        nows_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NowsDetailsActivity.class);
                intent.putExtra("web_nows_url", nowsList.get(position).getUrl());
                startActivity(intent);
            }
        });
    }

    /*首页个人中心fragment布局控件初始化和数据加载*/
    @Override
    public void getUserData() {
        Fragment fragment = fragmentManager.findFragmentByTag("UserCoreFragment");
        final View view = fragment.getView();
        /*获取fragment中的组件对象*/
        final RelativeLayout loginBefore = (RelativeLayout) view.findViewById(R.id.login_before_layout);
        final RelativeLayout loginAfter = (RelativeLayout) view.findViewById(R.id.login_after_layout);
        RelativeLayout userAboutAntLayout = (RelativeLayout) view.findViewById(R.id.user_about_ant_click);
        RelativeLayout usereinfoLayout = (RelativeLayout) view.findViewById(R.id.user_info_click);
        RelativeLayout userOrderLayout = (RelativeLayout) view.findViewById(R.id.user_order_click);
        RelativeLayout userChangePswLayout = (RelativeLayout) view.findViewById(R.id.user_change_psw_click);
        RelativeLayout userEvaluateLayout = (RelativeLayout) view.findViewById(R.id.user_evaluate_click);
        RelativeLayout userLikeLayout = (RelativeLayout) view.findViewById(R.id.user_like_click);

        final Button loginOutBtn = (Button) view.findViewById(R.id.user_login_out);
        final Button loginBtn = (Button) view.findViewById(R.id.ant_user_to_login_btn);
        CircleImageView userImg = (CircleImageView) view.findViewById(R.id.ant_user_core_img);
        TextView userName = (TextView) view.findViewById(R.id.ant_user_core_name);

        //判断是否登录，加载相应的布局
        if (LoginUtil.checkLogin(MainActivity.this)) {
            loginOutBtn.setVisibility(View.VISIBLE);
            loginBefore.setVisibility(View.GONE);
            loginAfter.setVisibility(View.VISIBLE);
            /*进入用户个人中心界面加载页面数据*/
            SharedPreferences s = getSharedPreferences("ant_user_info", MODE_PRIVATE);
            String userImgStr = s.getString("user_picture", "");
            String userNameStr = s.getString("user_name", "");
            if (userImgStr.equals("") || userImgStr == null) {
                userImg.setImageResource(R.drawable.ant_logo);
            } else {
                String url = HttpUtil.USER_IMG_HTTP_URL + userImgStr;
                LoadImg.getUrlImageByVolley(MainActivity.this, requestQueue, url, userImg);
            }
            userName.setText(userNameStr);
        } else {
            loginOutBtn.setVisibility(View.GONE);
            loginAfter.setVisibility(View.GONE);
            loginBefore.setVisibility(View.VISIBLE);
        }

        /*设置订单查询按钮点击事件*/
        userOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtil.checkLogin(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, UserOrderActivity.class);
                    startActivity(intent);
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }
            }
        });

        /*设置用户信息按钮点击事件*/
        usereinfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtil.checkLogin(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }
            }
        });

        /*设置用户评价按钮点击事件*/
        userEvaluateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtil.checkLogin(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, UserEvaluateActivity.class);
                    startActivity(intent);
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }           }
        });

        /*设置用户喜欢按钮点击事件*/
        userLikeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtil.checkLogin(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, UserLikeActivity.class);
                    startActivity(intent);
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }
            }
        });


        /*设置用户修改密码按钮点击事件*/
        userChangePswLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtil.checkLogin(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, UserChangePswActivity.class);
                    startActivity(intent);
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }
            }
        });

        /*设置关于我们按钮点击事件*/
        userAboutAntLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AboutAntActivity.class);
                startActivity(intent);

            }
        });
        /*设置登录按钮点击事件*/
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AntLoginActivity.class);
                startActivity(intent);
            }
        });
        /*设置退出登录按钮点击事件*/
        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("login out？")
                        .setContentText("您确定要退出登录吗？")
                        .setCancelText("点错了")
                        .setConfirmText("login out")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                SharedPreferences s = getSharedPreferences("ant_user_info", MODE_PRIVATE);
                                s.edit().clear().commit();
                                sDialog.cancel();
                                loginOutBtn.setVisibility(View.GONE);
                                loginAfter.setVisibility(View.GONE);
                                loginBefore.setVisibility(View.VISIBLE);

                            }
                        })
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //recycle view onItem click
    @Override
    public void onItemClick(List<Movie> moviesList, int position) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie_info", moviesList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
