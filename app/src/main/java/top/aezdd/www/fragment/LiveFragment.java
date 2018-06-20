package top.aezdd.www.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.aezdd.www.ant_movies.R;

/**
 * Created by aezdd on 2016/8/20.
 */
public class LiveFragment extends Fragment{
    private LiveFragmentInterface liveFragmentInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_ant_live_index,container,false);
        return view;
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
    public void onResume() {
        super.onResume();
        liveFragmentInterface.loadLiveData();
    }

    public interface LiveFragmentInterface{
        public void loadLiveData();
    }
}
