package top.aezdd.www.entity;

public class User {
    private Integer uId;

    private Integer uSex;

    private Integer uAge;

    private String uPhone;

    private String uMail;

    private String uName;

    private String uPsw;

    private String uPicture;

    private String uRank;

    private Integer uScore;

    private String uLoc;

    private String uThirdPart;

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getuSex() {
        return uSex;
    }

    public void setuSex(Integer uSex) {
        this.uSex = uSex;
    }

    public Integer getuAge() {
        return uAge;
    }

    public void setuAge(Integer uAge) {
        this.uAge = uAge;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone == null ? null : uPhone.trim();
    }

    public String getuMail() {
        return uMail;
    }

    public void setuMail(String uMail) {
        this.uMail = uMail == null ? null : uMail.trim();
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName == null ? null : uName.trim();
    }

    public String getuPsw() {
        return uPsw;
    }

    public void setuPsw(String uPsw) {
        this.uPsw = uPsw == null ? null : uPsw.trim();
    }

    public String getuPicture() {
        return uPicture;
    }

    public void setuPicture(String uPicture) {
        this.uPicture = uPicture == null ? null : uPicture.trim();
    }

    public String getuRank() {
        return uRank;
    }

    public void setuRank(String uRank) {
        this.uRank = uRank == null ? null : uRank.trim();
    }

    public Integer getuScore() {
        return uScore;
    }

    public void setuScore(Integer uScore) {
        this.uScore = uScore;
    }

    public String getuLoc() {
        return uLoc;
    }

    public void setuLoc(String uLoc) {
        this.uLoc = uLoc == null ? null : uLoc.trim();
    }

    public String getuThirdPart() {
        return uThirdPart;
    }

    public void setuThirdPart(String uThirdPart) {
        this.uThirdPart = uThirdPart == null ? null : uThirdPart.trim();
    }
}