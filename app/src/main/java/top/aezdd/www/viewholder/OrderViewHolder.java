package top.aezdd.www.viewholder;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jianzhou.liu on 2017/4/17.
 */

public class OrderViewHolder {
    private ImageView movieLogo;
    private TextView movieName;

    private TextView movieShowCityHaLL;
    private TextView movieShowTime;
    private TextView movieShowSeat;

    private TextView orderCode;
    private TextView orderState;
    private TextView orderAccount;
    private ImageView payStateIcon;
    private TextView payText;
    private TextView delete;

    public TextView getDelete() {
        return delete;
    }

    public TextView getPayText() {
        return payText;
    }

    public void setDelete(TextView delete) {
        this.delete = delete;
    }

    public void setPayText(TextView payText) {
        this.payText = payText;
    }

    public ImageView getPayStateIcon() {
        return payStateIcon;
    }

    public void setPayStateIcon(ImageView payStateIcon) {
        this.payStateIcon = payStateIcon;
    }

    public ImageView getMovieLogo() {
        return movieLogo;
    }

    public TextView getMovieName() {
        return movieName;
    }

    public TextView getMovieShowCityHaLL() {
        return movieShowCityHaLL;
    }

    public TextView getMovieShowSeat() {
        return movieShowSeat;
    }

    public TextView getMovieShowTime() {
        return movieShowTime;
    }

    public TextView getOrderAccount() {
        return orderAccount;
    }

    public TextView getOrderCode() {
        return orderCode;
    }

    public TextView getOrderState() {
        return orderState;
    }

    public void setMovieLogo(ImageView movieLogo) {
        this.movieLogo = movieLogo;
    }

    public void setMovieName(TextView movieName) {
        this.movieName = movieName;
    }

    public void setMovieShowCityHaLL(TextView movieShowCityHaLL) {
        this.movieShowCityHaLL = movieShowCityHaLL;
    }

    public void setMovieShowSeat(TextView movieShowSeat) {
        this.movieShowSeat = movieShowSeat;
    }

    public void setMovieShowTime(TextView movieShowTime) {
        this.movieShowTime = movieShowTime;
    }

    public void setOrderAccount(TextView orderAccount) {
        this.orderAccount = orderAccount;
    }

    public void setOrderCode(TextView orderCode) {
        this.orderCode = orderCode;
    }

    public void setOrderState(TextView orderState) {
        this.orderState = orderState;
    }

}
