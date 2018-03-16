package com.unnamedsoftware.russesamfunnet.Entity;

import java.io.Serializable;

/**
 * Created by Alexander Eilert Berg on 15.03.2018.
 */

public class EventEntity extends Entity implements Serializable
{

    private String eventName;
    private Integer eventID;
    private String eventDescription;

    public EventEntity(String eventName, Integer eventID, String eventDescription)
    {
        this.eventName = eventName;
        this.eventID = eventID;
        this.eventDescription = eventDescription;
    }

    @Override
    public String getSearchName() {return this.eventName;}

    public  String getEventName(){return this.eventName;}

    public Integer getEventID(){return this.eventID;}

    public String getEventDescription(){return this.eventDescription;}

    public void setEventName(String eventName){this.eventName = eventName;}

    public void setEventDescription(String eventDescription){this.eventDescription = eventDescription;}
}
