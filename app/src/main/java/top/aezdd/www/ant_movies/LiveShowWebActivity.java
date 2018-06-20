package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import top.aezdd.www.view.VideoEnabledWebChromeClient;
import top.aezdd.www.view.VideoEnabledWebView;


public class LiveShowWebActivity extends Activity {
    String iframeStr = "";

    @ViewInject(R.id.ant_movie_video_window)LinearLayout videoWindow;
    @ViewInject(R.id.ant_movie_video_all_screen)FrameLayout videoAllScreen;
    @ViewInject(R.id.ant_live_show_web_view)VideoEnabledWebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_show_web);
        ViewUtils.inject(this);
        //QbSdk.initX5Environment(this, null);
        //网页中的视频，上屏幕的时候，可能出现闪烁的情况
        //getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Intent intent = getIntent();
        String id = intent.getStringExtra("live_video_id");
        initView();
        webView.setWebChromeClient(new VideoEnabledWebChromeClient(this, videoWindow, videoAllScreen));
        //webView.loadUrl(getIntent().getStringExtra("url"));
        iframeStr = "<iframe height=600 width=100% src='http://player.youku.com/embed/"+id+"' frameborder=0 allowfullscreen></iframe>";
        webView.loadDataWithBaseURL("about:blank", iframeStr, "text/html", "utf-8", null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.destroy();
    }

    public void initView() {
        webView.setDrawingCacheEnabled(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启支持视频
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setGeolocationEnabled(true);
        // 开启DOM缓存。
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(getApplicationContext().getCacheDir()
                .getAbsolutePath());
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getApplicationContext().getCacheDir()
                .getAbsolutePath());
        webSettings.setAppCacheMaxSize(Integer.MAX_VALUE);
        webView.requestFocus();
    }

    public void exitActivity(View v) {
        finish();
    }
}
