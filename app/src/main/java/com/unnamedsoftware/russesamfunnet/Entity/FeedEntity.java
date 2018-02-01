package com.unnamedsoftware.russesamfunnet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class FeedEntity implements Parcelable {

    private static final long serialVersionUID = 1L;
    private Integer feedId;
    private String type;
    private String zone;
    private SchoolEntity schoolId;
    private String message;
    private RussEntity russId;
    private List<TagEntity> tagsList;

    public FeedEntity() {
    }

    public FeedEntity(Integer feedId) {
        this.feedId = feedId;
    }

    public FeedEntity(Integer feedId, String message) {
        this.feedId = feedId;
        this.message = message;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(Integer feedId) {
        this.feedId = feedId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public SchoolEntity getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(SchoolEntity schoolId) {
        this.schoolId = schoolId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RussEntity getRussId() {
        return russId;
    }

    public void setRussId(RussEntity russId) {
        this.russId = russId;
    }

    public List<TagEntity> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<TagEntity> tagsList) {
        this.tagsList = tagsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (feedId != null ? feedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FeedEntity)) {
            return false;
        }
        FeedEntity other = (FeedEntity) object;
        if ((this.feedId == null && other.feedId != null) || (this.feedId != null && !this.feedId.equals(other.feedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "no.ntnu.unnamedsoftware.entity.Feed[ feedId=" + feedId + " ]";
    }

    private int mData;

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<FeedEntity> CREATOR = new Parcelable.Creator<FeedEntity>() {
        public FeedEntity createFromParcel(Parcel in) {
            return new FeedEntity(in);
        }

        public FeedEntity[] newArray(int size) {
            return new FeedEntity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private FeedEntity(Parcel in) {
        mData = in.readInt();
    }
}
