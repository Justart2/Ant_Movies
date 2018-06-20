package top.aezdd.www.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.aezdd.www.ant_movies.AntLoginActivity;

/**
 * Created by jianzhou.liu on 2017/3/27.
 */

public class LoginUtil {
    static SharedPreferences s = null;
    public static boolean checkLogin(Context context){
        s = context.getSharedPreferences("ant_user_info", context.MODE_PRIVATE);
        int userName = s.getInt("user_id",0);
        if(userName!=0){
            return true;
        }
        return false;
    }
    public static int getUserId(Context context){
        if(checkLogin(context)){
            int userId = s.getInt("user_id",0);
            return userId;
        }
        return -1;
    }


    public static void toLogin(final Context context){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("login？")
                .setContentText("您还没有登录，登录后才能继续操作^‧^")
                .setCancelText("走错路了")
                .setConfirmText("go,去登录")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent();
                        intent.setClass(context,AntLoginActivity.class);
                        context.startActivity(intent);
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
    public static void loginOut(final Context context){

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("login out？")
                .setContentText("您确定要退出登录吗？")
                .setCancelText("点错了")
                .setConfirmText("login out")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        SharedPreferences s = context.getSharedPreferences("ant_user_info", context.MODE_PRIVATE);
                        s.edit().clear().commit();
                        sDialog.cancel();

                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        return;
                    }
                })
                .show();

    }
}
