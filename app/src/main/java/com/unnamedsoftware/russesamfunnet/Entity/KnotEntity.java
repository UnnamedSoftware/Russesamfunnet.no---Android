package com.unnamedsoftware.russesamfunnet.Entity;

import java.io.Serializable;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class KnotEntity extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    private int mData;
    private Long knotId;
    private String details;
    private SchoolEntity schoolId;
    private String title;
    private boolean completed;


    //private String status;

    public KnotEntity() {
    }

    public KnotEntity(Long knotId) {
        this.knotId = knotId;
    }

    public KnotEntity(Long knotId, String title, String details, Boolean completed) {
        this.knotId = knotId;
        this.details = details;
        this.title = title;
        this.completed = completed;
    }

    public KnotEntity(Long knotId, String title, String details) {
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

    public Long getKnotId() {
        return knotId;
    }

    public void setKnotId(Long knotId) {
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

    public boolean getCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
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

    @Override
    public String getSearchName()
    {
        return getTitle();
    }
}
