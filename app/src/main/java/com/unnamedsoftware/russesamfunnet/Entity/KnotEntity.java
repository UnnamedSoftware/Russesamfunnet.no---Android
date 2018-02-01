package com.unnamedsoftware.russesamfunnet.Entity;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class KnotEntity {

    private static final long serialVersionUID = 1L;
    private Integer knotId;
    private String details;
    private SchoolEntity schoolId;


    //private String status;

    public KnotEntity() {
    }

    public KnotEntity(Integer knotId) {
        this.knotId = knotId;
    }

    public KnotEntity(Integer knotId, String details) {
        this.knotId = knotId;
        this.details = details;
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
}
