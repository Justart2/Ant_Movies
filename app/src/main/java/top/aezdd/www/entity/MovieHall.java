package top.aezdd.www.entity;

import java.io.Serializable;

public class MovieHall implements Serializable{
    private Integer hId;

    private Integer cId;
    
    private String hName;

    private String hSeat;
    
    private MovieCity movieCity;

    public MovieCity getMovieCity() {
		return movieCity;
	}

	public void setMovieCity(MovieCity movieCity) {
		this.movieCity = movieCity;
	}

	public Integer gethId() {
        return hId;
    }

    public void sethId(Integer hId) {
        this.hId = hId;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName == null ? null : hName.trim();
    }

    public String gethSeat() {
        return hSeat;
    }

    public void sethSeat(String hSeat) {
        this.hSeat = hSeat == null ? null : hSeat.trim();
    }
}