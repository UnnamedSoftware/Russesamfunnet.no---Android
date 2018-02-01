package com.unnamedsoftware.russesamfunnet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class SchoolEntity implements Parcelable {

    private static final long serialVersionUID = 1L;
    private Integer schoolId;
    private String schoolName;
    private String schoolStatus;

    public SchoolEntity() {
    }

    public SchoolEntity(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public SchoolEntity(Integer schoolId, String schoolName, String schoolStatus) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.schoolStatus = schoolStatus;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(String schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (schoolId != null ? schoolId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SchoolEntity)) {
            return false;
        }
        SchoolEntity other = (SchoolEntity) object;
        if ((this.schoolId == null && other.schoolId != null) || (this.schoolId != null && !this.schoolId.equals(other.schoolId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "no.ntnu.unnamedsoftware.entity.School[ schoolId=" + schoolId + " ]";
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
    public static final Parcelable.Creator<SchoolEntity> CREATOR = new Parcelable.Creator<SchoolEntity>() {
        public SchoolEntity createFromParcel(Parcel in) {
            return new SchoolEntity(in);
        }

        public SchoolEntity[] newArray(int size) {
            return new SchoolEntity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private SchoolEntity(Parcel in) {
        mData = in.readInt();
    }
}
