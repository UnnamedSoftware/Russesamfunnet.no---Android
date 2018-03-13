package com.unnamedsoftware.russesamfunnet.RecyclerView;

/**
 *
 * Created by Alexander Eilert Berg on 30.01.2018.
 */

public class FeedPost
{
    private String poster;
    private String message;
    private String firstName;
    private String surname;
    private Long russID;

    public FeedPost(String firstName, String surname, Long russID,String post)
    {
        this.firstName = firstName;
        this.surname = surname;
        this.russID = russID;
        this.message = post;
        this.poster = firstName + " "  + surname;
    }


    public String getFirstName(){return this.firstName;}
    public String getSurname(){return this.surname;}
    public String getPoster(){return this.poster;}
    public String getPost(){return this.message;}

    public Long getRussID(){return this.russID;}

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    public void setRussID(Long russID){this.russID = russID;}
    public void setPoster(String poster){this.poster = poster;}
    public void setPost(String post){this.message  =post;}
}
