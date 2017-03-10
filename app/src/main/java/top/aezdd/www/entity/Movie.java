package top.aezdd.www.entity;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable{
    private Integer mId;

    private String mName;

    private String mType;

    private String mDirector;

    private String mActor;

    private String mCountry;

    private String mVersion;

    private String mTimeLength;

    private String mDescription;

    private String mReleaseTime;

    private Float mPrice;

    private String mRate;

    private String mPicture;

    private String mStagePhotos;

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName == null ? null : mName.trim();
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType == null ? null : mType.trim();
    }

    public String getmDirector() {
        return mDirector;
    }

    public void setmDirector(String mDirector) {
        this.mDirector = mDirector == null ? null : mDirector.trim();
    }

    public String getmActor() {
        return mActor;
    }

    public void setmActor(String mActor) {
        this.mActor = mActor == null ? null : mActor.trim();
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry == null ? null : mCountry.trim();
    }

    public String getmVersion() {
        return mVersion;
    }

    public void setmVersion(String mVersion) {
        this.mVersion = mVersion == null ? null : mVersion.trim();
    }

    public String getmTimeLength() {
        return mTimeLength;
    }

    public void setmTimeLength(String mTimeLength) {
        this.mTimeLength = mTimeLength == null ? null : mTimeLength.trim();
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription == null ? null : mDescription.trim();
    }

    public String getmReleaseTime() {
        return mReleaseTime;
    }

    public void setmReleaseTime(String mReleaseTime) {
        this.mReleaseTime = mReleaseTime;
    }

    public Float getmPrice() {
        return mPrice;
    }

    public void setmPrice(Float mPrice) {
        this.mPrice = mPrice;
    }

    public String getmRate() {
        return mRate;
    }

    public void setmRate(String mRate) {
        this.mRate = mRate == null ? null : mRate.trim();
    }

    public String getmPicture() {
        return mPicture;
    }

    public void setmPicture(String mPicture) {
        this.mPicture = mPicture == null ? null : mPicture.trim();
    }

    public String getmStagePhotos() {
        return mStagePhotos;
    }

    public void setmStagePhotos(String mStagePhotos) {
        this.mStagePhotos = mStagePhotos == null ? null : mStagePhotos.trim();
    }
}