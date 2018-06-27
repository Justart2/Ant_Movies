package top.aezdd.www.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import top.aezdd.www.ant_movies.R;

/**
 * Created by aezdd on 2016/8/20.
 */
public class MovieListFragment extends Fragment {
    private MovieListFragmentInterface movieListFragmentInterface;
    private View mView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_movie_list,container,false);
        }
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MovieListFragmentInterface){
            movieListFragmentInterface = (MovieListFragmentInterface)activity;
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
        movieListFragmentInterface.loadMovieListData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    public interface MovieListFragmentInterface {
        public void loadMovieListData();
    }
}
