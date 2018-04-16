package com.unnamedsoftware.russesamfunnet;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
 * <p>
 * Created by HallvardPC on 23.02.2018.
 * Version 2 (19.03.2018) by Alexander Eilert Berg
 */

public class Global extends Application
{
    //Used for debugging
    static final Boolean DEBUG = false;


    //--- Russ ID ---
    private Long russId;

    /**
     * Sets the russ ID
     *
     * @param russId
     */
    public void setRussId(Long russId)
    {
        this.russId = russId;
    }

    /**
     * Returns the russ ID
     *
     * @return The russ ID
     */
    public Long getRussId()
    {
        return this.russId;
    }


    //--- Image Loader ---

    private ImageLoader imageLoader;

    public ImageLoader getImageLoader(){return this.imageLoader;}


    public void createInitialImageLoader()
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        this.imageLoader = ImageLoader.getInstance();
    }


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
        if (accessToken == null)
        {
            if (DEBUG)
            {
                System.out.println("Access token is empty");
            }
            try
            {
                FileReader fileReader = new FileReader(getCache("token"));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                accessToken = bufferedReader.readLine();
                if (DEBUG)
                {
                    System.out.println("BufferReader line: " + accessToken);
                }
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
     *
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
     * Deletes the file with the given name
     */
    public String deleteCache(String fileName)
    {
        File file = new File(getCacheDir(), fileName);
        accessToken = null;
        if (file.exists())
        {
            Boolean successCheck = file.delete();

            if (successCheck == true)
            {
                return "File successfully deleted.";
            }
        } else
        {
            return "There is no file with that name.";
        }
        return "The file was not deleted.";
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
    private Long groupID;

    /**
     * Uses the received string to change the current group id
     *
     * @param newGroupID
     */
    public void setGroupID(Long newGroupID)
    {
        this.groupID = newGroupID;
    }

    /**
     * Returns the current (last accessed) group id
     *
     * @return the current (last accessed) group id
     */
    public Long getGroupID()
    {
        return groupID;
    }

    /**
     * Uses the received string to change the current group name
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
