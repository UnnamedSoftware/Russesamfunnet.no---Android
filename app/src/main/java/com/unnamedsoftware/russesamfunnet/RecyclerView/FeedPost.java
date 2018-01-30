package com.unnamedsoftware.russesamfunnet.RecyclerView;

/**
 *
 * Created by Alexander Eilert Berg on 30.01.2018.
 */

public class FeedPost
{
    private String poster;
    private String post;

    public FeedPost(String poster,String post)
    {
        this.poster = poster;
        this.post = post;
    }


    public String getPoster(){return this.poster;}
    public String getPost(){return this.post;}

    public void setPoster(String poster){this.poster = poster;}
    public void setPost(String post){this.post  =post;}
}
