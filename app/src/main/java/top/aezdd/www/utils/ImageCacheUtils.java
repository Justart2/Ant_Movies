package top.aezdd.www.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import top.aezdd.www.ant_movies.R;

/**
 * Created by jianzhou.liu on 2017/3/28.
 */

public class ImageCacheUtils {


    public static void imageCache(Context context,RequestQueue requestQueue,ImageView imageView,String url){
        //RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //1.实例化ImageLoader
        ImageLoader loader = new ImageLoader(requestQueue, new BitmapCache());
        //2.设置监听器
        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(imageView, 0, R.drawable.ant_logo);
        //3.获取图片
        loader.get(url, listener);
    }

    static class BitmapCache implements ImageLoader.ImageCache {
        //LruCache对象
        private LruCache<String, Bitmap> lruCache ;
        //设置最大缓存为10Mb，大于这个值会启动自动回收
        private int max = 10*1024*1024;

        public BitmapCache(){
            //初始化 LruCache
            lruCache = new LruCache<String, Bitmap>(max){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes()*value.getHeight();
                }
            };
        }
        @Override
        public Bitmap getBitmap(String url) {
            return lruCache.get(url);
        }
        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            lruCache.put(url, bitmap);
        }
    }
}
