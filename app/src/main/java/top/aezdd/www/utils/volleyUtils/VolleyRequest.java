package top.aezdd.www.utils.volleyUtils;

import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

public class VolleyRequest {
	public Context context;
	public StringRequest stringRequest;

	public void requestGet(Context mContext, String url,String tag,
			VolleyInterface vi) {
		MyApplication.getHttpQueue().cancelAll(tag);
		stringRequest = new StringRequest(Method.GET, url, vi.lodingListener(),
				vi.lodingErrorListener());
		stringRequest.setTag(tag);
		MyApplication.getHttpQueue().add(stringRequest);
	}

	public void requestPost(Context mContext, String url,String tag,
			VolleyInterface vi,final Map<String,String> params) {
		MyApplication.getHttpQueue().cancelAll(tag);
		stringRequest = new StringRequest(Method.POST, url, vi.lodingListener(),
				vi.lodingErrorListener()){
			@Override
			protected Map<String, String> getParams()
					throws AuthFailureError {
				return params;
			}
		};
		stringRequest.setTag(tag);
		MyApplication.getHttpQueue().add(stringRequest);
	}
}
