package com.unnamedsoftware.russesamfunnet.RecyclerView;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */

public class ListUser
{
    private String name;
    private Integer position;

    public ListUser(String name, Integer position)
    {
        this.name = name;
        this.position = position;
    }

    public String getName()
    {
        return this.name;
    }

    public Integer getPosition()
    {
        return this.position;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }
}
