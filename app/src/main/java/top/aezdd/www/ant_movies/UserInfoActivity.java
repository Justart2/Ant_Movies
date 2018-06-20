package top.aezdd.www.ant_movies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.loadData.LoadImg;
import top.aezdd.www.utils.DynamicPermission;
import top.aezdd.www.utils.HttpUtil;
import top.aezdd.www.utils.MultipartRequest;
import top.aezdd.www.utils.OtherUtils;
import top.aezdd.www.view.CircleImageView;

public class UserInfoActivity extends Activity implements View.OnClickListener{
    private String tag = "change_user_info";
    private String userNameData;
    private String userPhoneData;
    private String userImgData;
    private String userEmailData;
    private Dialog imgDialog;
    private AlertDialog dialog;
    private SharedPreferences s;
    private RequestQueue requestQueue;
    private SweetAlertDialog pDialog;
    /**
     * 上传图片部分
     */

    /* 头像文件 */
    private String imageFileName = "ant_temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),200 X 200的正方形。（生成bitmap貌似有时要报错？可试下把大小弄小点）
    private static int output_X = 200;
    private static int output_Y = 200;


    //XUtils初始化组件
    @ViewInject(R.id.user_info_img)CircleImageView userImgView;
    @ViewInject(R.id.user_info_name)TextView userNameView;
    @ViewInject(R.id.user_info_phone)TextView userPhoneView;
    @ViewInject(R.id.user_info_email)TextView userEmailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ViewUtils.inject(this);
        requestQueue = Volley.newRequestQueue(this);
        s = getSharedPreferences("ant_user_info",MODE_PRIVATE);
        initTextData();
        initImg();
    }
    //初始化组件，设置data
    public void initTextData(){
        userNameData = s.getString("user_name","");
        userEmailData = s.getString("user_email","");
        userPhoneData = s.getString("user_phone","");
        userNameView.setText(userNameData);
        userPhoneView.setText(userPhoneData);
        userEmailView.setText(userEmailData);
    }
    public void initImg(){
        //设置头像
        userImgData = s.getString("user_picture","");
        LoadImg.getUrlImageByVolley(this,requestQueue,HttpUtil.IMGHTTPURL+userImgData,userImgView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll(tag);
    }

    /*访问服务器进行业务操作*/
    public void goInternet(){
        String url = HttpUtil.HttpUrl+"/user/android_change_user_info.do";
        StringRequest changeUserInfo = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("goInternet--->",userNameData);
                SharedPreferences.Editor ss = getSharedPreferences("ant_user_info",MODE_PRIVATE).edit();
                ss.putString("user_name",userNameData);
                ss.putString("user_phone",userPhoneData);
                ss.putString("user_email",userEmailData);
                ss.commit();
                initTextData();
                dialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UserInfoActivity.this, "网络开小差了！", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("user_name",userNameData);
                map.put("user_phone",userPhoneData);
                map.put("user_email",userEmailData);
                map.put("user_id",s.getInt("user_id",-1)+"");
                return map;
            }
        };
        changeUserInfo.setTag(tag);
        requestQueue.add(changeUserInfo);
    }

    /*上传图片到服务器*/
    public void uploadImgTOInternet(String imgFileAbsoultPath){
        //构造参数列表
        List<Part> partList = new ArrayList<Part>();
        partList.add(new StringPart("user_id", s.getInt("user_id",-1)+""));
        try {
            partList.add(new FilePart("user_img", new File(imgFileAbsoultPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String url = HttpUtil.HttpUrl+"/user/modifyUserPhoto.do";
        //生成请求
        MultipartRequest profileUpdateRequest = new MultipartRequest(url, partList.toArray(new Part[partList.size()]), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //处理成功返回信息
                //修改Sharedpreference中的图片信息
                if(response.equals("error")){
                    Toast.makeText(getApplication(), "上传失败", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor ss = s.edit();
                    ss.putString("user_picture",response);
                    ss.commit();
                }
                pDialog.cancel();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //处理失败错误信息
                Log.e("MultipartRequest", error.getMessage(), error);
                Toast.makeText(getApplication(), "上传失败", Toast.LENGTH_SHORT).show();
                pDialog.cancel();
                pDialog.dismiss();
            }
        });
        //将请求加入队列
        requestQueue.add(profileUpdateRequest);
    }

    /*修改用户信息的点击事件*/
    public void changeImage(View v){
        changeUserImgDialog();
    }
    public void changeName(View v){
        changeUserInfoDialog("user_name");
    }
    public void changePhone(View v){
        changeUserInfoDialog("user_phone");
    }
    public void changeEmail(View v){
        changeUserInfoDialog("user_email");
    }

    /*修改用户信息的dialog*/
    public void changeUserInfoDialog(final String flag){
        String textFlag = "";
        switch(flag){
            case "user_name":
                textFlag = "修改用户名";
                break;
            case "user_phone":
                textFlag = "修改手机号";
                break;
            case "user_email":
                textFlag = "修改邮箱地址";
                break;
            default:
        }
        //自定义dialog组件
        final EditText et = new EditText(this);
        et.setWidth(1000);
        et.setHeight(120);
        et.setPadding(20,0,0,0);
        LinearLayout l = new LinearLayout(this);
        l.addView(et);
        l.setPadding(80,50,80,0);
        et.setBackgroundResource(R.drawable.style_find_city_edit_text);
        switch(flag){
            case "user_name":
                et.setText(userNameData);
                break;
            case "user_phone":
                et.setText(userPhoneData);
                break;
            case "user_email":
                et.setText(userEmailData);
                break;
            default:
        }
        dialog =new AlertDialog.Builder(this).setTitle(textFlag)
                .setView(l)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(flag){
                            case "user_name":
                                userNameData = et.getText().toString().trim();
                                break;
                            case "user_phone":
                                userPhoneData = et.getText().toString().trim();
                                break;
                            case "user_email":
                                userEmailData = et.getText().toString().trim();
                                break;
                            default:
                        }

                        if (userNameData.equals("")) {
                            Toast.makeText(getApplicationContext(), "用户名不能为空！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            //访问服务器修改用户信息
                            goInternet();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void changeUserImgDialog(){
        //申请动态权限
        if(DynamicPermission.checkPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE})){
            openUpdateImageDialog();
        }

    }
    public void openUpdateImageDialog(){
        imgDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_change_user_img_layout, null);
        Button choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        Button takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        cancel.setOnClickListener(this);
        imgDialog.setContentView(inflate);
        Window dialogWindow = imgDialog.getWindow();
        dialogWindow.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        imgDialog.show();
    }
    //动态权限申请结束
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length >0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    //Toast.makeText(this, "您已授权，请重新选择！", Toast.LENGTH_SHORT).show();
                    openUpdateImageDialog();
                }else{
                    //用户拒绝授权
                    Toast.makeText(this, "您拒绝访问！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /*修改用户头像的list-dialog的点击事件*/
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.choosePhoto:
                //从相册选择上传图片
                choseHeadImageFromGallery();
                imgDialog.dismiss();
                break;
            case R.id.takePhoto:
                //选择拍照上传图片
                choseHeadImageFromCameraCapture();
                imgDialog.dismiss();
                break;

            case R.id.btn_cancel:
                //取消上传头像操作
                imgDialog.dismiss();
                break;

        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        //setUploadImgName();
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        //setUploadImgName();
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), imageFileName)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                Log.e("img----file_path",intent.getData().toString());
                cropRawPhoto(intent.getData());
                //Toast.makeText(getApplication(), "去裁剪", Toast.LENGTH_LONG).show();
                break;

            case CODE_CAMERA_REQUEST:

                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            imageFileName);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("outputFormat", "JPG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.e("path---bitmap",photo.getConfig().toString());
            String filePath = OtherUtils.saveBitmap(this,photo);

            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("上传中····");
            pDialog.setCancelable(false);
            pDialog.show ();
           uploadImgTOInternet(filePath);

            userImgView.setImageBitmap(photo);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }




    public void exitActivity(View c){
        finish();
    }

}
