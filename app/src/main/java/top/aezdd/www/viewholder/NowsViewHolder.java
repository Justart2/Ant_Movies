package top.aezdd.www.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aezdd on 2016/8/24.
 */
public class NowsViewHolder {
    private ImageView nowsImage;
    private TextView nowsTitle;
    private TextView nowsAuthor;
    private TextView nowsTime;
    private TextView nowsType;

    public TextView getNowsType() {
        return nowsType;
    }

    public void setNowsType(TextView nowsType) {
        this.nowsType = nowsType;
    }

    public ImageView getNowsImage() {
        return nowsImage;
    }

    public void setNowsImage(ImageView nowsImage) {
        this.nowsImage = nowsImage;
    }

    public TextView getNowsTitle() {
        return nowsTitle;
    }

    public void setNowsTitle(TextView nowsTitle) {
        this.nowsTitle = nowsTitle;
    }

    public TextView getNowsAuthor() {
        return nowsAuthor;
    }

    public void setNowsAuthor(TextView nowsAuthor) {
        this.nowsAuthor = nowsAuthor;
    }

    public TextView getNowsTime() {
        return nowsTime;
    }

    public void setNowsTime(TextView nowsTime) {
        this.nowsTime = nowsTime;
    }
}
