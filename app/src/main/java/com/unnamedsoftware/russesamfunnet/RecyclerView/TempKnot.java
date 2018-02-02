package com.unnamedsoftware.russesamfunnet.RecyclerView;

/**
 * Created by Alexander Eilert Berg on 30.01.2018.
 */

public class TempKnot
{
    private String title;
    private Integer knotID;
    private String description;

    public TempKnot(String title,String description, Integer knotID)
    {
        this.title = title;
        this.knotID = knotID;
        this.description = description;
    }

    public String getDescription(){return this.description;}
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getTitle(){return this.title;}
    public Integer getKnotID(){return this.knotID;}
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setKnotID(Integer knotID){this.knotID = knotID;}
}

