package com.unnamedsoftware.russesamfunnet.RecyclerView;

/**
 * Created by Alexander Eilert Berg on 30.01.2018.
 */

public class TempKnot
{
    private String title;
    private Integer knotID;

    public TempKnot(String title, Integer knotID)
    {
        this.title = title;
        this.knotID = knotID;
    }

    public String getTitle(){return this.title;}
    public Integer getKnotID(){return this.knotID;}
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setKnotID(Integer knotID){this.knotID = knotID;}
}

