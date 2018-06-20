package top.aezdd.www.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by jianzhou.liu on 2017/4/19.
 */

public class DynamicPermission {
    public final static int REQUEST_CODE = 1;
    public static boolean  checkPermission(Activity activity,String permission){
        //检查权限
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                //已经禁止提示了
                Toast.makeText(activity, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();

            }else{
                ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
            }
            return false;
        }
        return true;
    }
    public static boolean  checkPermissions(Activity activity,String[] permission){
        //检查权限
        for(int i = 0;i<permission.length;i++){
            if (ContextCompat.checkSelfPermission(activity, permission[i])
                    != PackageManager.PERMISSION_GRANTED) {
                //进入到这里代表没有权限.
                if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission[i])){
                    //已经禁止提示了
                    Toast.makeText(activity, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();

                }else{
                    ActivityCompat.requestPermissions(activity, permission, REQUEST_CODE);
                }
                return false;
            }
            return true;
        }
        return true;
    }
    //动态权限申请结束
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length >0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    Toast.makeText(this, "您已授权，请重新选择！", Toast.LENGTH_SHORT).show();
                }else{
                    //用户拒绝授权
                    Toast.makeText(this, "您拒绝访问！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }*/
}
