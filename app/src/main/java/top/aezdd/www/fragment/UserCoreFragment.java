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
 * Created by aezdd on 2016/9/24.
 */
public class UserCoreFragment extends Fragment{
    UserFragmentInterface userFragmentInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_user_core,container,false);
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof UserFragmentInterface){
            userFragmentInterface = (UserFragmentInterface)activity;
        }else{
            try{
                throw new Exception("类型转化异常");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userFragmentInterface.getUserData();
    }
    public interface UserFragmentInterface{
        public void getUserData();
    }
}
