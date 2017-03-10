package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NowsDetailsActivity extends Activity {
    @ViewInject(R.id.nows_details)WebView webView;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nows_details);
        ViewUtils.inject(this);
        Intent intent = getIntent();
        url = intent.getStringExtra("web_nows_url");
        webView.loadUrl(url);
    }
    public void exitActicity(View v){
        finish();
    }

}
