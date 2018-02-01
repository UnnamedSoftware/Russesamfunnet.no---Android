package com.unnamedsoftware.russesamfunnet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class ScoreboardEntity implements Parcelable {

    private static final long serialVersionUID = 1L;
    private Integer scoreboardId;
    private int points;
    private RussEntity russId;

    public ScoreboardEntity() {
    }

    public ScoreboardEntity(Integer scoreboardId) {
        this.scoreboardId = scoreboardId;
    }

    public ScoreboardEntity(Integer scoreboardId, int points) {
        this.scoreboardId = scoreboardId;
        this.points = points;
    }

    public Integer getScoreboardId() {
        return scoreboardId;
    }

    public void setScoreboardId(Integer scoreboardId) {
        this.scoreboardId = scoreboardId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
        hash += (scoreboardId != null ? scoreboardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScoreboardEntity)) {
            return false;
        }
        ScoreboardEntity other = (ScoreboardEntity) object;
        if ((this.scoreboardId == null && other.scoreboardId != null) || (this.scoreboardId != null && !this.scoreboardId.equals(other.scoreboardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "no.ntnu.unnamedsoftware.entity.Scoreboard[ scoreboardId=" + scoreboardId + " ]";
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
    public static final Parcelable.Creator<ScoreboardEntity> CREATOR = new Parcelable.Creator<ScoreboardEntity>() {
        public ScoreboardEntity createFromParcel(Parcel in) {
            return new ScoreboardEntity(in);
        }

        public ScoreboardEntity[] newArray(int size) {
            return new ScoreboardEntity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private ScoreboardEntity(Parcel in) {
        mData = in.readInt();
    }
}
