package top.aezdd.www.utils;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import top.aezdd.www.ant_movies.AntLoginActivity;

public class CloudInfSMSUtils {
	//接口地址
	private final static String url = "http://www.etuocloud.com/gateway.action";
	//应用 app_key
	private final static String APP_KEY = "OtVTPBKRyV3I7nggseEyevQJJcCSxJAW";
	//应用 app_secret
	private final static String APP_SECRET = "2Lb6jcnFtexuQJgSbX5t8XlQh6kSyszzOOXHuxwKkY9y4gYR4VVXjkMSegClPzIn";
	//接口响应格式 json或xml
	private final static String FORMAT = "json";
	private static String smsCode;
	private static String returnMsg;
	
	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(System.in);
		System.out.println("请输入电话号码：");
		//String phone = scan.next();
		String phone = "15117088744";
		//sendSmsCode(phone);
		System.out.println("验证码发送成功，请稍等……");
		
		System.out.println("请输入获取到的验证码！");
		String code = scan.next();
		while(true){
			if(code.equals(smsCode)){
				System.out.println("验证码正确！");
				break;
			}else{
				System.out.println("验证码不正确，请重新输入！");
				code = scan.next();
				
			}
			
		}

	}
	/**
	 * 发生短信验证码
	 */
	public static String sendSmsCode(RequestQueue requestQueue,final String phone) throws Exception {
		smsCode = getSMSCode();
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				try{
					JSONObject jsonObject = new JSONObject(s);
					if(jsonObject.get("result").toString().equals("0")){
						returnMsg = smsCode;
						Log.e("smsCode---->",smsCode);
						//Toast.makeText(AntLoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
					}else{
						returnMsg = jsonObject.getString("msg")+"!";
					}

				}catch(Exception e){
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e("error---->",volleyError.getMessage());
				//returnMsg = "网络错误";
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = null;
				try{
					params = new HashMap<String, String>();
					params.put("app_key", APP_KEY);
					params.put("view", FORMAT);
					params.put("method", "cn.etuo.cloud.api.sms.simple");
					params.put("to", phone);
					params.put("template", "1");
					params.put("smscode", smsCode);
					params.put("sign", genSign(params));
				}catch(Exception e){
					e.printStackTrace();
				}

				return params;
			}
		};
		requestQueue.add(stringRequest);
		return returnMsg;
	}
	/**
	 * 随机生成短信验证码
	 * */
	public static String getSMSCode(){
		int a = (int) (Math.random()*10);
		int b = (int) (Math.random()*10);
		int c = (int) (Math.random()*10);
		int d = (int) (Math.random()*10);
		return a + "" + b + "" + c+ "" +d;
	}
		
	/**
	 * 发生模板短信
	 */
	public static void sendSmsTpl() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", APP_KEY);
		params.put("view", FORMAT);
		params.put("method", "cn.etuo.cloud.api.sms.template");
		params.put("out_trade_no", "商户订单号，可空");
		params.put("to", "接收模板短信的手机号");
		params.put("template", "模板短信模板ID");
		params.put("params", "模板中的参数，以英文逗号分隔");
		params.put("sign", genSign(params));
		String result = HttpClientUtils.httpPost(url, params);
		System.out.println(result);
	}

	/**
	 * 发送自定义短信
	 */
	public static void sendSmsCustom() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", APP_KEY);
		params.put("view", FORMAT);
		params.put("method", "cn.etuo.cloud.api.sms.advance");
		params.put("out_trade_no", "商户订单号，可空");
		params.put("to", "接收自定义短信的手机号");
		params.put("content", "自定义短信内容");
		params.put("sign", genSign(params));
		String result = HttpClientUtils.httpPost(url, params);
		System.out.println(result);
	}

	/**
	 * 发送语音验证码
	 */
	public static void sendVoiceCode() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", APP_KEY);
		params.put("method", "cn.etuo.cloud.api.voice.simple");
		params.put("out_trade_no", "商户订单号，可空");
		params.put("to", "接收验证码语音的手机号");
		params.put("template", "语音验证码模板ID");
		params.put("verifyCode", "验证码");
		params.put("sign", genSign(params));
		String result = HttpClientUtils.httpPost(url, params);
		System.out.println(result);
	}

	/**
	 * 发送语音通知
	 * 
	 * @throws IOException
	 */
	public static void sendVoiceNotice() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", APP_KEY);
		params.put("method", "cn.etuo.cloud.api.voice.message");
		params.put("out_trade_no", "商户订单号，可空");
		params.put("to", "接收语音通知的手机号");
		params.put("template", "语音通知模板ID");
		params.put("sign", genSign(params));
		String result = HttpClientUtils.httpPost(url, params);
		System.out.println(result);
	}
	
	/**
	 * 获取流量产品列表
	 */
	public static void getFlowProductList() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", APP_KEY);
		params.put("method", "cn.etuo.cloud.api.flow.list");
		params.put("sign", genSign(params));
		String result = HttpClientUtils.httpPost(url, params);
		System.out.println(result);
	}
	
	/**
	 * 充值流量
	 */
	public static void rechargeFlow() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_key", APP_KEY);
		params.put("method", "cn.etuo.cloud.api.flow.charge");
		params.put("out_trade_no", "商户订单号，可空");
		params.put("mobile", "被充值流量的手机号");
		params.put("flowcode", "流量产品编码");
		params.put("sign", genSign(params));
		String result = HttpClientUtils.httpPost(url, params);
		System.out.println(result);
	}

	/**
	 * 生成签名
	 * 
	 * 根据参数名称的ASCII码表的顺序排序。如：foo=1, bar=2, foo_bar=3,   foobar=4排序后的顺序是bar=2, foo=1, foo_bar=3, foobar=4
	 * 将排序好的参数名和参数值(以k1=v1&k2=v2...)方式拼装在一起，根据上面的示例得到的结果为：bar=2&foo=1&foo_bar=3&foobar=4
	 * 把拼装好的字符串采用utf-8编码，在拼装的字符串后面加上应用的app_secret后，再进行摘要，如：md5(bar=2&foo=1&foo_bar=3&foobar=4+app_secret)
	 * 将摘要得到的字节流结果使用十六进制表示，如：hex("helloworld".getBytes("utf-8")) = "68656c6c6f776f726c64"
	 */
	public static String genSign(Map<String, String> params)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//TreeMap 默认按key 升序
		Map<String,String> sortMap = new TreeMap<String,String>();
		sortMap.putAll(params);
		//以k1=v1&k2=v2...方式拼接参数
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, String> s : sortMap.entrySet()) {
			String k = s.getKey();
			String v = s.getValue();
			if(StringUtils.isBlank(v)){//过滤空值
				continue;
			}
			builder.append(k).append("=").append(v).append("&");
		}
		if (!sortMap.isEmpty()) {
			builder.deleteCharAt(builder.length() - 1);
		}
		//拼接应用的app_secret
		builder.append(APP_SECRET);
		//摘要
		MessageDigest instance = MessageDigest.getInstance("MD5");
		byte[] digest = instance.digest(builder.toString().getBytes("UTF-8"));
		//十六进制表示
		return new String(encodeHex(digest));
	}

	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	private static char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_LOWER[0x0F & data[i]];
		}
		return out;
	}
}
