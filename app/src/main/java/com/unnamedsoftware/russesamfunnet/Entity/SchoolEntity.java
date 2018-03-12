package com.unnamedsoftware.russesamfunnet.Entity;

import java.io.Serializable;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class SchoolEntity extends Entity implements Serializable {

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
    public String getSearchName(){return getSchoolName();}

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

}
