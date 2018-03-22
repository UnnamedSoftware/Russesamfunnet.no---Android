package com.unnamedsoftware.russesamfunnet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.security.acl.Group;
import java.util.List;

/**
 * Created by HallvardPC on 01.02.2018.
 */

public class FeedEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String poster;
    private Long feedId;
    private String type;
    private GroupEntity groupId;
    private SchoolEntity schoolId;
    private String message;
    private RussEntity russId;
    private List<TagEntity> tagsList;

    public FeedEntity() {
    }

    public FeedEntity(Long feedId) {
        this.feedId = feedId;
    }

    public FeedEntity(Long feedId, String message, RussEntity russId) {
        this.feedId = feedId;
        this.message = message;
        this.russId = russId;
        this.poster = russId.getFirstName() + " " + russId.getLastName();
    }

    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GroupEntity getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupEntity groupId) {
        this.groupId = groupId;
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
    public String getPoster(){return this.poster;}

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

}
