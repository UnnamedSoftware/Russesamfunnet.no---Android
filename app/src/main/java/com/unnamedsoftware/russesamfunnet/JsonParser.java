package com.unnamedsoftware.russesamfunnet;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */

public class JSONParser
{
    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";

    public JSONObject getJSONFromUrl(String urlString) throws IOException
    {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int data = inputStreamReader.read();
            while (data != -1)
            {
                char current = (char) data;
                data = inputStreamReader.read();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            json = stringBuilder.toString();
        }catch (Exception e)
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try
        {
            jsonObject = new JSONObject(json);
        }catch (JSONException e)
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jsonObject;
    }
}
