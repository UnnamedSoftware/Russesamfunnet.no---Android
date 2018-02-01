package com.unnamedsoftware.russesamfunnet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class KnotEntity implements Parcelable {

    private static final long serialVersionUID = 1L;
    private Integer knotId;
    private String details;
    private SchoolEntity schoolId;
    private String title;


    //private String status;

    public KnotEntity() {
    }

    public KnotEntity(Integer knotId) {
        this.knotId = knotId;
    }

    public KnotEntity(Integer knotId, String details, String title) {
        this.knotId = knotId;
        this.details = details;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getKnotId() {
        return knotId;
    }

    public void setKnotId(Integer knotId) {
        this.knotId = knotId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public SchoolEntity getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(SchoolEntity schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (knotId != null ? knotId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnotEntity)) {
            return false;
        }
        KnotEntity other = (KnotEntity) object;
        if ((this.knotId == null && other.knotId != null) || (this.knotId != null && !this.knotId.equals(other.knotId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "no.ntnu.unnamedsoftware.entity.Knots[ knotId=" + knotId + " ]";
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
    public static final Parcelable.Creator<KnotEntity> CREATOR = new Parcelable.Creator<KnotEntity>() {
        public KnotEntity createFromParcel(Parcel in) {
            return new KnotEntity(in);
        }

        public KnotEntity[] newArray(int size) {
            return new KnotEntity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private KnotEntity(Parcel in) {
        mData = in.readInt();
    }

}
