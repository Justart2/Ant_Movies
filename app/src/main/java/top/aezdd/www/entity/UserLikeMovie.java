package top.aezdd.www.entity;

public class UserLikeMovie {
    private Integer ucId;

    private Integer mId;

    private Integer uId;
    
    private Integer ucState;
    
    private User user;
    
    private Movie movie;
    
    public Movie getMovie() {
		return movie;
	}
    public User getUser() {
		return user;
	}
    public void setMovie(Movie movie) {
		this.movie = movie;
	}
    public void setUser(User user) {
		this.user = user;
	}
    
    public Integer getUcState() {
		return ucState;
	}
    
    public void setUcState(Integer ucState) {
		this.ucState = ucState;
	}
    
    public Integer getUcId() {
        return ucId;
    }

    public void setUcId(Integer ucId) {
        this.ucId = ucId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }
}