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
 * Created by aezdd on 2016/8/24.
 */
public class NowsFragment extends Fragment {
    NowsFragmentInterface nowsFragmentInterface;
    private View mView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_nows_list,container,false);
        }

        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof NowsFragmentInterface){
                nowsFragmentInterface = (NowsFragmentInterface)activity;
        }else{
            try{
                throw new Exception("类型转化异常");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nowsFragmentInterface.getNowsData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    public interface NowsFragmentInterface{
        public void getNowsData();
    }
}
