package com.unnamedsoftware.russesamfunnet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Retrieves a JSONArray from the server, returns the JSONArray.
 * Created by Alexander Eilert Berg on 29.01.2018.
 */
public class JSONParser extends AsyncTask<URL, Void, JSONArray>
{
    static InputStream inputStream = null;
    static JSONArray jsonArray = null;
    static String json = "";

    public interface OnPostExecute {
        void onPostExecute(JSONArray jsonArray);
    }
    OnPostExecute callback;
    public JSONParser(OnPostExecute callback)
    {
        this.callback = callback;
    }

    @Override
    protected JSONArray doInBackground(URL... urls) {

        HttpURLConnection urlConnection = null;


        try
        {
            urlConnection = (HttpURLConnection) urls[0].openConnection();
            inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
/**
            int data = inputStreamReader.read();
            while (data != -1)
            {
                char current = (char) data;
                data = inputStreamReader.read();
            }
 */
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
            e.printStackTrace();
        } finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
/**
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

            System.out.println("String builder: " + stringBuilder .toString());
            json = stringBuilder.toString();

            System.out.println("\n line 5 \n");
        } catch (Exception e)
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 */

        try
        {
            jsonArray = new JSONArray(json);
            System.out.println("\n line 6 \n");
        } catch (JSONException e)
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        if (jsonArray == null) System.out.println("JSONParser DEADBEEF");
        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray)
    {
        if (callback != null)
        {
            callback.onPostExecute(jsonArray);
        }
    }

}
