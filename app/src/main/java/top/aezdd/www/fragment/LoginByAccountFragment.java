package top.aezdd.www.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.aezdd.www.ant_movies.R;

/**
 * Created by jianzhou.liu on 2017/3/24.
 */

public class LoginByAccountFragment extends Fragment{
    private LoginByAccountInterface loginByAccountInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_login_by_ant_account,container,false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof LoginByAccountInterface){
            loginByAccountInterface = (LoginByAccountInterface) activity;
        }else{
            try {
                throw new Exception("类型转化异常");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loginByAccountInterface.initLoginAccountView();
    }
    public interface LoginByAccountInterface{
         void initLoginAccountView();
    }
}
