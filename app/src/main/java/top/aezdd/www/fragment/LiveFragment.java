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
 * Created by aezdd on 2016/8/20.
 */
public class LiveFragment extends Fragment{
    private LiveFragmentInterface liveFragmentInterface;
    private View mView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.frame_ant_live_index,container,false);
        }
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof LiveFragmentInterface){
            liveFragmentInterface = (LiveFragmentInterface)activity;
        }else{
            try {
                throw new Exception("类型转化异常");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        liveFragmentInterface.loadLiveData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    public interface LiveFragmentInterface{
        public void loadLiveData();
    }
}
