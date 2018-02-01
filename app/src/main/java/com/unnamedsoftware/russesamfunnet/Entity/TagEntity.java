package com.unnamedsoftware.russesamfunnet.Entity;

import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class TagEntity implements Parcelable {

    private static final long serialVersionUID = 1L;
    private Integer tagId;
    private FeedEntity feedId;
    private RussEntity russId;

    public TagEntity() {
    }

    public TagEntity(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public FeedEntity getFeedId() {
        return feedId;
    }

    public void setFeedId(FeedEntity feedId) {
        this.feedId = feedId;
    }

    public RussEntity getRussId() {
        return russId;
    }

    public void setRussId(RussEntity russId) {
        this.russId = russId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tagId != null ? tagId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TagEntity)) {
            return false;
        }
        TagEntity other = (TagEntity) object;
        if ((this.tagId == null && other.tagId != null) || (this.tagId != null && !this.tagId.equals(other.tagId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "no.ntnu.unnamedsoftware.entity.Tags[ tagId=" + tagId + " ]";
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
    public static final Parcelable.Creator<TagEntity> CREATOR = new Parcelable.Creator<TagEntity>() {
        public TagEntity createFromParcel(Parcel in) {
            return new TagEntity(in);
        }

        public TagEntity[] newArray(int size) {
            return new TagEntity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private TagEntity(Parcel in) {
        mData = in.readInt();
    }
}
