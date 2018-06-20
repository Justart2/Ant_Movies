package top.aezdd.www.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import top.aezdd.www.view.CircleImageView;


/**
 * Created by jianzhou.liu on 2017/4/6.
 */

public class MovieEvaluateViewHolder {
    private CircleImageView evaluateImg;
    private TextView evaluateName;
    private TextView evaluateContent;
    private TextView evaluateTime;

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
