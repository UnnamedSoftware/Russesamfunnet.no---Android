package com.unnamedsoftware.russesamfunnet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class RussEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer russId;

    private String russStatus;
    private String firstName;

    private String lastName;

    private String email;
    private String russPassword;
    private String profilePicture;
    private String russCard;
    private String russRole;
    private int russYear;

    private SchoolEntity schoolId;

    public RussEntity() {
    }

    public RussEntity(Integer russId) {
        this.russId = russId;
    }

    public RussEntity(Integer russId, String russStatus, String firstName, String lastName, String email, String russPassword, String russRole, int russYear) {
        this.russId = russId;
        this.russStatus = russStatus;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.russPassword = russPassword;
        this.russRole = russRole;
        this.russYear = russYear;
    }

    public Integer getRussId() {
        return russId;
    }

    public void setRussId(Integer russId) {
        this.russId = russId;
    }

    public String getRussStatus() {
        return russStatus;
    }

    public void setRussStatus(String russStatus) {
        this.russStatus = russStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRussPassword() {
        return russPassword;
    }

    public void setRussPassword(String russPassword) {
        this.russPassword = russPassword;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRussCard() {
        return russCard;
    }

    public void setRussCard(String russCard) {
        this.russCard = russCard;
    }

    public String getRussRole() {
        return russRole;
    }

    public void setRussRole(String russRole) {
        this.russRole = russRole;
    }

    public int getRussYear() {
        return russYear;
    }

    public void setRussYear(int russYear) {
        this.russYear = russYear;
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
        hash += (russId != null ? russId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RussEntity)) {
            return false;
        }
        RussEntity other = (RussEntity) object;
        if ((this.russId == null && other.russId != null) || (this.russId != null && !this.russId.equals(other.russId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "no.ntnu.unnamedsoftware.entity.Russ[ russId=" + russId + " ]";
    }


}
