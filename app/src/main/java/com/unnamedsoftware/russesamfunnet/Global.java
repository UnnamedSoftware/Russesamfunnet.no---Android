package com.unnamedsoftware.russesamfunnet;

import android.app.Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Holds a collection of global variables.
 * Stores tokens used in server communication in the cache of the application
 *
 * Created by HallvardPC on 23.02.2018.
 * Version 2 (19.03.2018) by Alexander Eilert Berg
 */

public class Global extends Application
{
    //Used for debugging
    static final Boolean DEBUG = false;


    //--- Token ---
    private String accessToken;

    /**
     * Checks if the global variable is empty.
     * If it is, the token is retrieved from the cache, set as the global variable and returned.
     * if it is not, it returns the global variable.
     *
     * @return The access token
     */
    public String getAccessToken()
    {
        if (accessToken.isEmpty())
        {
            if(DEBUG) {System.out.println("Access token is empty");}
                try
            {
                FileReader fileReader = new FileReader(getCache("token"));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                accessToken = bufferedReader.readLine();
                if(DEBUG) {System.out.println("BufferReader line: " + accessToken);}
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return accessToken;
    }

    /**
     * Returns the token type from the cache.
     * @return The token type from the cache.
     */
    public String getTokenType()
    {
        String tokenType = "";
        try
        {
            FileReader fileReader = new FileReader(getCache("tokenType"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            tokenType = bufferedReader.readLine();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return tokenType;
    }

    /**
     * Sets the access token in as a global variable.
     * Saves the token as a cache file, along with the token type (facebook or russesamfunnet)
     *
     * @param accessToken
     * @param tokenType
     */
    public void setAccessToken(String accessToken, String tokenType)
    {
        FileWriter fileWriter;
        try
        {
            fileWriter = new FileWriter(getCache("token"));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(accessToken);
            bufferedWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            fileWriter = new FileWriter(getCache("tokenType"));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(tokenType);
            bufferedWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        this.accessToken = accessToken;
    }

    /**
     * Returns the cache based on given file name.
     * If the cache has been cleared and the file no longer exists
     * it will then create the file again and return it.
     *
     * @return The cache based on given file name.
     */
    private File getCache(String fileName)
    {
        File file = new File(getCacheDir(), fileName);

        if (file.exists() && file.length() > 0)
        {
            return file;
        } else
        {
            try
            {
                file = File.createTempFile(fileName, null, this.getCacheDir());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return file;
    }


    //--- Groups ---
    /**
     * Holds the current group name, for use in the toolbar in group hub and scoreboard.
     */
    private String groupName;

    /**
     * Uses the recived string to change the current group name
     *
     * @param newGroupName
     */
    public void setGroupName(String newGroupName)
    {
        this.groupName = newGroupName;
    }

    /**
     * Returns the current (last accessed) group name
     *
     * @return the current (last accessed) group name
     */
    public String getGroupName()
    {
        return groupName;
    }
}
