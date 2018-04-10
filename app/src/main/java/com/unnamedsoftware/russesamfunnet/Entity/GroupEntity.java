package com.unnamedsoftware.russesamfunnet.Entity;

import java.io.Serializable;

/**
 *  The local representation of the entity on the database
 *
 * Created by Alexander Eilert Berg on 15.03.2018.
 */

public class GroupEntity extends Entity implements Serializable
{
    private Long groupID;
    private String groupName;

    public GroupEntity(Long groupID, String groupName)
    {
        this.groupID = groupID;
        this.groupName = groupName;
    }

    @Override
    public String getSearchName()
    {
        return this.groupName;
    }

    public Long getGroupID(){return this.groupID;}

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
