package top.aezdd.www.utils;

import android.app.AlertDialog;
import android.content.Context;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jianzhou.liu on 2017/3/13.
 */
public class AlertDialogUtil {
    private String  []list;
    private static boolean flag;
    public static void getlistDialog(Context context,List list){

        new AlertDialog.Builder(context)
        .setTitle("城市列表")
        .setItems(tranListToArray(list), null)
        .setNegativeButton("确定", null)
        .show();

    }
    public static String[] tranListToArray(List<String> list){
        String []objects = new String[list.size()];
        for(int i = 0;i<list.size();i++){
            objects[i] = list.get(i);
        }
        return objects;
    }
    public static boolean isDeleteDialog(Context context){

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("确定要删除吗？")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        sDialog.cancel();


                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        sDialog.setTitleText("Deleted!")
                                .setContentText("删除成功!")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();
        return flag;
    }
}
