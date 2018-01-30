package com.unnamedsoftware.russesamfunnet.RecyclerView;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */

public class ListUser
{
    private String fullName;
    private String firstName;
    private String surname;
    private Integer russID;
    private Integer position;

    public ListUser(String firstName, String surname, Integer russID, Integer position)
    {
        this.firstName = firstName;
        this.surname = surname;
        this.russID = russID;
        this.position = position;

        this.fullName = firstName + " "  + surname;
    }

    public String getFirstName(){return this.firstName;}
    public String getSurname(){return this.surname;}
    public String getFullName(){return this.fullName;}

    public Integer getRussID(){return this.position;}
    public Integer getPosition()
    {
        return this.position;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
    public void setRussID(Integer russID){this.russID = russID;}
    public void setPosition(Integer position)
    {
        this.position = position;
    }
}