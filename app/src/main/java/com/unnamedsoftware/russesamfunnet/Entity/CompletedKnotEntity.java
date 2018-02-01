package com.unnamedsoftware.russesamfunnet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class CompletedKnotEntity implements Parcelable{

    private static final long serialVersionUID = 1L;

    private Integer completedId;
    private KnotEntity knotId;
    private RussEntity russId;
    private RussEntity witnessId1;
    private RussEntity witnessId2;

    public CompletedKnotEntity() {
    }

    public CompletedKnotEntity(Integer completedId) {
        this.completedId = completedId;
    }

    public Integer getCompletedId() {
        return completedId;
    }

    public void setCompletedId(Integer completedId) {
        this.completedId = completedId;
    }

    public KnotEntity getKnotId() {
        return knotId;
    }

    public void setKnotId(KnotEntity knotId) {
        this.knotId = knotId;
    }

    public RussEntity getRussId() {
        return russId;
    }

    public void setRussId(RussEntity russId) {
        this.russId = russId;
    }

    public RussEntity getWitnessId1() {
        return witnessId1;
    }

    public void setWitnessId1(RussEntity witnessId1) {
        this.witnessId1 = witnessId1;
    }

    public RussEntity getWitnessId2() {
        return witnessId2;
    }

    public void setWitnessId2(RussEntity witnessId2) {
        this.witnessId2 = witnessId2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (completedId != null ? completedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompletedKnotEntity)) {
            return false;
        }
        CompletedKnotEntity other = (CompletedKnotEntity) object;
        if ((this.completedId == null && other.completedId != null) || (this.completedId != null && !this.completedId.equals(other.completedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "no.ntnu.unnamedsoftware.entity.Completed[ completedId=" + completedId + " ]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private int mData;

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<CompletedKnotEntity> CREATOR = new Parcelable.Creator<CompletedKnotEntity>() {
        public CompletedKnotEntity createFromParcel(Parcel in) {
            return new CompletedKnotEntity(in);
        }

        public CompletedKnotEntity[] newArray(int size) {
            return new CompletedKnotEntity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private CompletedKnotEntity(Parcel in) {
        mData = in.readInt();
    }
}
