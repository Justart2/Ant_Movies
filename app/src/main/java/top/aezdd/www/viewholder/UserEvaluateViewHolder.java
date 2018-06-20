package top.aezdd.www.viewholder;

import android.widget.TextView;

import top.aezdd.www.view.CircleImageView;
import top.aezdd.www.view.SwipeListLayout;

/**
 * Created by jianzhou.liu on 2017/4/11.
 */

public class UserEvaluateViewHolder {
    private CircleImageView evaluateImg;
    private TextView evaluateName;
    private TextView evaluateContent;
    private TextView evaluateTime;
    private SwipeListLayout swipeListLayout;
    private TextView delete;

    public SwipeListLayout getSwipeListLayout() {
        return swipeListLayout;
    }

    public void setDelete(TextView delete) {
        this.delete = delete;
    }

    public TextView getDelete() {
        return delete;
    }

    public void setSwipeListLayout(SwipeListLayout swipeListLayout) {
        this.swipeListLayout = swipeListLayout;
    }

    public CircleImageView getEvaluateImg() {
        return evaluateImg;
    }

    public TextView getEvaluateTime() {
        return evaluateTime;
    }

    public TextView getEvaluateContent() {
        return evaluateContent;
    }

    public TextView getEvaluateName() {
        return evaluateName;
    }

    public void setEvaluateTime(TextView evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public void setEvaluateContent(TextView evaluteContent) {
        this.evaluateContent = evaluteContent;
    }

    public void setEvaluateImg(CircleImageView evaluteImg) {
        this.evaluateImg = evaluteImg;
    }

    public void setEvaluateName(TextView evaluteName) {
        this.evaluateName = evaluteName;
    }
}
