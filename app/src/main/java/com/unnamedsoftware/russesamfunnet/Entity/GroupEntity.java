package com.unnamedsoftware.russesamfunnet.Entity;

import java.io.Serializable;

/**
 *  The local representation of the entity on the database
 *
 * Created by Alexander Eilert Berg on 15.03.2018.
 */

public class GroupEntity extends Entity implements Serializable
{
    private Integer groupID;
    private String groupName;



    @Override
    public String getSearchName()
    {
        return this.groupName;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
