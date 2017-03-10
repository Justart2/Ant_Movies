package top.aezdd.www.entity;

public class Manager {
    private Integer aId;

    private String aName;

    private String aPsw;

    private Integer aSex;

    private Integer aAge;

    private String aGrant;

    private String aPhone;

    private String aMail;

    private String aAddress;

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName == null ? null : aName.trim();
    }

    public String getaPsw() {
        return aPsw;
    }

    public void setaPsw(String aPsw) {
        this.aPsw = aPsw == null ? null : aPsw.trim();
    }

    public Integer getaSex() {
        return aSex;
    }

    public void setaSex(Integer aSex) {
        this.aSex = aSex;
    }

    public Integer getaAge() {
        return aAge;
    }

    public void setaAge(Integer aAge) {
        this.aAge = aAge;
    }

    public String getaGrant() {
        return aGrant;
    }

    public void setaGrant(String aGrant) {
        this.aGrant = aGrant == null ? null : aGrant.trim();
    }

    public String getaPhone() {
        return aPhone;
    }

    public void setaPhone(String aPhone) {
        this.aPhone = aPhone == null ? null : aPhone.trim();
    }

    public String getaMail() {
        return aMail;
    }

    public void setaMail(String aMail) {
        this.aMail = aMail == null ? null : aMail.trim();
    }

    public String getaAddress() {
        return aAddress;
    }

    public void setaAddress(String aAddress) {
        this.aAddress = aAddress == null ? null : aAddress.trim();
    }
}