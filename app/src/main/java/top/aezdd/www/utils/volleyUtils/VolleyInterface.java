package top.aezdd.www.utils.volleyUtils;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public abstract class VolleyInterface {
	public Context mContext;
	public Listener<String> mListener;
	public ErrorListener mError;

	public VolleyInterface(Context mContext) {
		this.mContext = mContext;
	}

	public abstract void onSucceed(String result);

	public abstract void onError(VolleyError error);

	public Listener<String> lodingListener(){
		mListener = new Listener<String>() {
			@Override
			public void onResponse(String arg0) {

				onSucceed(arg0);
			}
		};
		return mListener;
	}
	public ErrorListener lodingErrorListener(){
		mError = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				onError(arg0);

			}
		};
			
		return mError;
	}
}
