package top.aezdd.www.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by aezdd on 2016/8/18.
 */
public class MoviesViewHolder {
    private ImageView movieLogo;
    private ImageView movieRateIcon;
    private TextView movieName;
    private TextView movieDesc;
    private TextView movieType;
    private TextView movieVersion;
    private TextView movieRate;
    private TextView movieDirector;
    private TextView movieActor;
    private TextView movieCountry;
    private TextView movieTimeLength;
    private TextView movieReleaseTime;
    private TextView moviePrice;
    private ImageView[] moviePictures;
    private TextView MovieClickBtn;

    public TextView getMovieClickBtn() {
        return MovieClickBtn;
    }

    public void setMovieClickBtn(TextView movieClickBtn) {
        MovieClickBtn = movieClickBtn;
    }

    public ImageView getMovieRateIcon() {
        return movieRateIcon;
    }

    public void setMovieRateIcon(ImageView movieRateIcon) {
        this.movieRateIcon = movieRateIcon;
    }

    public ImageView[] getMoviePictures() {
        return moviePictures;
    }

    public void setMoviePictures(ImageView[] moviePictures) {
        this.moviePictures = moviePictures;
    }

    public TextView getMoviePrice() {
        return moviePrice;
    }

    public void setMoviePrice(TextView moviePrice) {
        this.moviePrice = moviePrice;
    }

    public TextView getMovieReleaseTime() {
        return movieReleaseTime;
    }

    public void setMovieReleaseTime(TextView movieReleaseTime) {
        this.movieReleaseTime = movieReleaseTime;
    }

    public TextView getMovieTimeLength() {
        return movieTimeLength;
    }

    public void setMovieTimeLength(TextView movieTimeLength) {
        this.movieTimeLength = movieTimeLength;
    }

    public TextView getMovieCountry() {
        return movieCountry;
    }

    public void setMovieCountry(TextView movieCountry) {
        this.movieCountry = movieCountry;
    }

    public TextView getMovieActor() {
        return movieActor;
    }

    public void setMovieActor(TextView movieActor) {
        this.movieActor = movieActor;
    }

    public TextView getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(TextView movieDirector) {
        this.movieDirector = movieDirector;
    }

    public TextView getMovieRate() {
        return movieRate;
    }

    public void setMovieRate(TextView movieRate) {
        this.movieRate = movieRate;
    }

    public TextView getMovieVersion() {
        return movieVersion;
    }

    public void setMovieVersion(TextView movieVersion) {
        this.movieVersion = movieVersion;
    }

    public TextView getMovieType() {
        return movieType;
    }

    public void setMovieType(TextView movieType) {
        this.movieType = movieType;
    }

    public TextView getMovieDesc() {
        return movieDesc;
    }

    public void setMovieDesc(TextView movieDesc) {
        this.movieDesc = movieDesc;
    }

    public TextView getMovieName() {
        return movieName;
    }

    public void setMovieName(TextView movieName) {
        this.movieName = movieName;
    }

    public ImageView getMovieLogo() {
        return movieLogo;
    }

    public void setMovieLogo(ImageView movieLogo) {
        this.movieLogo = movieLogo;
    }
}
