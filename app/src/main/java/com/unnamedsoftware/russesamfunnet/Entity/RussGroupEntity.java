package com.unnamedsoftware.russesamfunnet.Entity;

/**
 * Created by HallvardPC on 21.03.2018.
 */

public class RussGroupEntity {
    private Long russGroupId;
    private GroupEntity groupId;
    private RussEntity russId;

    public Long getRussGroupId() {
        return russGroupId;
    }

    public void setRussGroupId(Long russGroupId) {
        this.russGroupId = russGroupId;
    }

    public GroupEntity getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupEntity groupId) {
        this.groupId = groupId;
    }

    public RussEntity getRussId() {
        return russId;
    }

    public void setRussId(RussEntity russId) {
        this.russId = russId;
    }

    public RussGroupEntity(){

    }

    public RussGroupEntity(GroupEntity groupId, RussEntity russId){
        this.groupId = groupId;
        this.russId = russId;
    }
}
