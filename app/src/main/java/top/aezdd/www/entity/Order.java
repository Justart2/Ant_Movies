package top.aezdd.www.entity;

public class Order {
    private String oId;

    private Integer uId;

    private Integer sId;

    private Integer oTicketCount;

    private Double oPrice;

    private String oSeatInfo;

    private String oTime;

    private String oState;
    
    private MovieShow movieShow;
    
    private User user;

    public MovieShow getMovieShow() {
		return movieShow;
	}

	public void setMovieShow(MovieShow movieShow) {
		this.movieShow = movieShow;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public Integer getoTicketCount() {
        return oTicketCount;
    }

    public void setoTicketCount(Integer oTicketCount) {
        this.oTicketCount = oTicketCount;
    }

    public Double getoPrice() {
        return oPrice;
    }

    public void setoPrice(Double oPrice) {
        this.oPrice = oPrice;
    }

    public String getoSeatInfo() {
        return oSeatInfo;
    }

    public void setoSeatInfo(String oSeatInfo) {
        this.oSeatInfo = oSeatInfo == null ? null : oSeatInfo.trim();
    }

    public String getoTime() {
        return oTime;
    }

    public void setoTime(String oTime) {
        this.oTime = oTime;
    }

    public String getoState() {
        return oState;
    }

    public void setoState(String oState) {
        this.oState = oState == null ? null : oState.trim();
    }
}