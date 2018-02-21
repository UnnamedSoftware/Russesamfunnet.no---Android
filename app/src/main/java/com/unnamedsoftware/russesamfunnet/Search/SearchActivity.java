package com.unnamedsoftware.russesamfunnet.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SearchView;

import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.JSONParser;
import com.unnamedsoftware.russesamfunnet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The search activity TODO:PLease add documentation :(
 *
 * http://abhiandroid.com/ui/searchview
 *
 * Requires an intent containing the location, municipality, and the wanted data set to work
 *
 * Created by Alexander Eilert Berg on 19.02.2018.
 */

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    private String location;
    private String municipality;
    private String wantedDataSet;

    private ListView listView;
    private ListViewAdapter adapter;
    private SearchView searchView;

    JSONArray data = null;

    private List<Object> dataSet;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        this.dataSet = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            this.location = (String) bundle.get("location");
            this.municipality = (String) bundle.get("municipality");
            this.wantedDataSet = (String) bundle.get("wantedDataSet");
        }

        getDataSet();

        this.adapter = new ListViewAdapter(this, dataSet);
        this.listView.setAdapter(adapter);

        this.searchView = findViewById(R.id.search);
        this.searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    /**
     * Retrieves the needed data set from the server using the JSONparser. Needs an location, an municipality, and the wanted data set.
     */
    private void getDataSet()
    {
        String url = getString(R.string.url) + this.wantedDataSet + "?" + this.wantedDataSet + this.location;

        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray)
                {
                    fillDataSet(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Takes the given jsonArray and depending on the wanted data set chooses one of the cases
     * in the switch.
     *
     * @param jsonArray
     */
    private void fillDataSet(JSONArray jsonArray)
    {
        try
        {
            data = jsonArray;

            switch (wantedDataSet)
            {
                case "school":
                    for (int i = 0; i < data.length(); i++)
                    {

                        JSONObject u = data.getJSONObject(i);
                        JSONObject newDataJSONObject = u.getJSONObject("schoolId");
                        Integer schoolId = Integer.valueOf(newDataJSONObject.getString("schoolId"));
                        String schoolName = newDataJSONObject.getString("schoolName");
                        String schoolStatus = newDataJSONObject.getString("schoolStatus");

                        SchoolEntity school = new SchoolEntity(schoolId, schoolName, schoolStatus);
                        dataSet.add(school);
                    }
                    break;

                case "russ":
                    for (int i = 0; i < data.length(); i++)
                    {
                        JSONObject u = data.getJSONObject(i);
                        JSONObject newDataJSONObject = u.getJSONObject("russId");
                        Integer russId = Integer.valueOf(newDataJSONObject.getString("russId"));
                        String russStatus = newDataJSONObject.getString("russStatus");
                        String firstName = newDataJSONObject.getString("firstName");
                        String lastName = newDataJSONObject.getString("lastName");
                        String email = newDataJSONObject.getString("email");
                        String russPassword = newDataJSONObject.getString("russPassword");
                        String profilePicture = newDataJSONObject.getString("profilePicture");
                        String russCard = newDataJSONObject.getString("russCard");
                        String russRole = newDataJSONObject.getString("russRole");
                        Integer russYear = Integer.valueOf(newDataJSONObject.getString("russYear"));

                        RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear);
                        dataSet.add(russ);
                    }
                    break;

                case "knotList":
                    for (int i = 0; i < data.length(); i++)
                    {
                        JSONObject u = data.getJSONObject(i);
                        JSONObject newDataJSONObject = u.getJSONObject("");

                        Integer knotId = newDataJSONObject.getInt("knotId");
                        String details = newDataJSONObject.getString("knotDetails");
                        String title = newDataJSONObject.getString("knotName");

                        KnotEntity knot = new KnotEntity(knotId, details, title);
                        dataSet.add(knot);
                    }
                    break;
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}