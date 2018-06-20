package top.aezdd.www.viewholder;

import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by jianzhou.liu on 2017/3/30.
 */

public class LiveViewHolder {
    private ImageView liveImage;
    private TextView liveSimName;
    private TextView liveAllName;
    private TextView liveOnline;
    private TextView liveTime;
    private TextView liveTimeLength;

    public TextView getLiveTimeLength() {
        return liveTimeLength;
    }

    public void setLiveTimeLength(TextView liveTimeLength) {
        this.liveTimeLength = liveTimeLength;
    }

    public TextView getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(TextView liveTime) {
        this.liveTime = liveTime;
    }

    public ImageView getLiveImage() {
        return liveImage;
    }

    public TextView getLiveAllName() {
        return liveAllName;
    }

    public TextView getLiveOnline() {
        return liveOnline;
    }

    public TextView getLiveSimName() {
        return liveSimName;
    }

    public void setLiveAllName(TextView liveAllName) {
        this.liveAllName = liveAllName;
    }

    public void setLiveImage(ImageView liveImage) {
        this.liveImage = liveImage;
    }

    public void setLiveOnline(TextView liveOnline) {
        this.liveOnline = liveOnline;
    }

    public void setLiveSimName(TextView liveSimName) {
        this.liveSimName = liveSimName;
    }
}
