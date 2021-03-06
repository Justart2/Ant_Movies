package top.aezdd.www.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.aezdd.www.ant_movies.R;

/**
 * Created by jianzhou.liu on 2017/3/24.
 */

public class LoginByPhoneFragment extends Fragment {
    private LoginByPhoneInterface loginByPhoneInterface;
    private View mView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.frame_login_by_phone,container,false);
        }

        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof LoginByPhoneInterface){
            loginByPhoneInterface = (LoginByPhoneInterface) activity;
        }else{
            try{
                throw new Exception("类型转化异常！");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loginByPhoneInterface.initLoginByPhoneView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    public interface LoginByPhoneInterface{
        void initLoginByPhoneView();
    }
}
