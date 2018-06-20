package top.aezdd.www.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import top.aezdd.www.view.CircleImageView;
import top.aezdd.www.view.SwipeListLayout;

/**
 * Created by jianzhou.liu on 2017/4/12.
 */

public class UserLikeMoviesViewHolder {
    private CircleImageView userLikeMovieImg;
    private TextView userLikeMovieName;
    private TextView userLikeMovieContent;
    private ImageView userLikeMovieState;
    private SwipeListLayout swipeListLayout;
    private TextView delete;
    private TextView find;

    public TextView getFind() {
        return find;
    }

    public void setFind(TextView find) {
        this.find = find;
    }

    public CircleImageView getUserLikeMovieImg() {
        return userLikeMovieImg;
    }

    public ImageView getUserLikeMovieState() {
        return userLikeMovieState;
    }

    public SwipeListLayout getSwipeListLayout() {
        return swipeListLayout;
    }

    public TextView getDelete() {
        return delete;
    }

    public TextView getUserLikeMovieContent() {
        return userLikeMovieContent;
    }

    public TextView getUserLikeMovieName() {
        return userLikeMovieName;
    }

    public void setDelete(TextView delete) {
        this.delete = delete;
    }

    public void setSwipeListLayout(SwipeListLayout swipeListLayout) {
        this.swipeListLayout = swipeListLayout;
    }

    public void setUserLikeMovieContent(TextView userLikeMovieContent) {
        this.userLikeMovieContent = userLikeMovieContent;
    }

    public void setUserLikeMovieImg(CircleImageView userLikeMovieImg) {
        this.userLikeMovieImg = userLikeMovieImg;
    }

    public void setUserLikeMovieName(TextView userLikeMovieName) {
        this.userLikeMovieName = userLikeMovieName;
    }

    public void setUserLikeMovieState(ImageView userLikeMovieState) {
        this.userLikeMovieState = userLikeMovieState;
    }
}
