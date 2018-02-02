package com.unnamedsoftware.russesamfunnet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
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

public class JSONParser extends AsyncTask<URL, Void, JSONObject>
{
    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";

    public interface OnPostExecute {
        void onPostExecute(JSONObject jsonObject);
    }
    OnPostExecute callback;
    public JSONParser(OnPostExecute callback)
    {
        this.callback = callback;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {

        HttpURLConnection urlConnection = null;


        try
        {
            urlConnection = (HttpURLConnection) urls[0].openConnection();
            inputStream = urlConnection.getInputStream();
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
            System.out.println("\n line 1 \n");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            System.out.println("\n line 2 \n");
            StringBuilder stringBuilder = new StringBuilder();
            System.out.println("\n line 3 \n");
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");

            }
            inputStream.close();
            System.out.println("\n line 4 \n");

            System.out.println("String builder: " + stringBuilder.toString());
            json = stringBuilder.toString();

            System.out.println("\n line 5 \n");
        } catch (Exception e)
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try
        {
            jsonObject = new JSONObject(json);
            System.out.println("\n line 6 \n");
        } catch (JSONException e)
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject)
    {
        if (callback != null)
        {
            callback.onPostExecute(jsonObject);
        }
    }

}
