package top.aezdd.www.loadData;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.BitmapUtils;

import top.aezdd.www.ant_movies.R;
import top.aezdd.www.utils.ImageCacheUtils;

/**
 * Created by jianzhou.liu on 2017/3/13.
 */
public class LoadImg {

    private Bitmap bitmap;

    public static void getMovieImage(ImageView imageView, String url,Context context) {
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        bitmapUtils.display(imageView, url);
    }

    public static void getUrlImageByVolley(Context context,RequestQueue requestQueue,String url,final ImageView imageView){

        ImageCacheUtils.imageCache(context,requestQueue,imageView,url);

    }

}
