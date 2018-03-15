package com.unnamedsoftware.russesamfunnet;

import android.app.Application;

/**
 * Holds a collection of global variables
 *
 * Created by HallvardPC on 23.02.2018.
 */

public class Global extends Application {

    //Token
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    //Groups
    /**
     * Holds the current group name, for use in the toolbar in group hub and scoreboard.
     */
    private String groupName;

    /**
     * Uses the recived string to change the current group name
     *
     * @param newGroupName
     */
    public void setGroupName(String newGroupName) {this.groupName = newGroupName;}

    /**
     * Returns the current (last accessed) group name
     *
     * @return the current (last accessed) group name
     */
    public String getGroupName(){return groupName;}
}
