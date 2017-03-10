package top.aezdd.www.entity;

import java.io.Serializable;
import java.util.Date;

public class MovieShow implements Serializable{
    private Integer sId;

    private Integer hId;

    private Integer mId;

    private String sTime;

    private Integer sOnSale;
    
    private MovieHall moviehall;
    
    private Movie movie;

    public MovieHall getMoviehall() {
		return moviehall;
	}

	public void setMoviehall(MovieHall moviehall) {
		this.moviehall = moviehall;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public Integer gethId() {
        return hId;
    }

    public void sethId(Integer hId) {
        this.hId = hId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public Integer getsOnSale() {
        return sOnSale;
    }

    public void setsOnSale(Integer sOnSale) {
        this.sOnSale = sOnSale;
    }
}