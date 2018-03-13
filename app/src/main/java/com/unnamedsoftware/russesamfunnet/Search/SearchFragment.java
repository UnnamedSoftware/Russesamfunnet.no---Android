package com.unnamedsoftware.russesamfunnet.Search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.unnamedsoftware.russesamfunnet.Entity.Entity;
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
 * https://github.com/codepath/android_guides/wiki/Creating-and-Using-Fragments
 *
 * Requires an intent containing the location, municipality, and the wanted data set to work
 *
 * Created by Alexander Eilert Berg on 19.02.2018.
 */

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener
{
    private String location;
    private String municipality;
    private String wantedDataSet;
    private String query;

    private ListView listView;
    private ListViewAdapter adapter;
    private SearchView searchView;

    JSONArray data = null;

    private List<Entity> dataSet;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.dataSet = new ArrayList<>();

        this.adapter = new ListViewAdapter(getActivity(), dataSet);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.location = getArguments().getString("location");
        this.municipality = getArguments().getString("municipality");
        this.wantedDataSet = getArguments().getString("wantedDataSet");
        return inflater.inflate(R.layout.search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getDataSet();

        this.listView = view.findViewById(R.id.searchListView);
        this.listView.setAdapter(adapter);

        this.searchView = view.findViewById(R.id.search);
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
        return true;
    }

    /**
     * Retrieves the needed data set from the server using the JSONparser. Needs an location, an municipality, and the wanted data set.
     */
    private void getDataSet()
    {
        String url = getString(R.string.url);
        System.out.println("Wanted data set: " + this.wantedDataSet);
        String wanted = this.wantedDataSet.substring(0, 1).toUpperCase() + this.wantedDataSet.substring(1);
        if (this.municipality == null && this.location == null) //Both are empty, get all data
        {
            String getArgument = "getAll" + wanted;
            url += getArgument;

        } else if (this.municipality.isEmpty() && !this.location.isEmpty()) //Only municipality is empty, get all data based on location
        {
            String getArgument = "getLocation" + wanted + "s";
            url += getArgument + "?" + "location=" + this.location;

        } else if (!this.municipality.isEmpty() && this.location.isEmpty()) //Only location is empty, get all data based on municipality
        {
            String getArgument = "getMunicipality" + wanted;
            url += getArgument + "?"+ "municipality=" + this.municipality;

        } else //Both municipality and location are given, retrieve data based on them.
        {
            String getArgument = "getLocation" + wanted + "s";
            url += getArgument + "?" + "location=" + this.location;
        }
        System.out.println(url);
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
                        System.out.println("Trying to add Entities");

                        JSONObject newDataJSONObject = data.getJSONObject(i);
                        System.out.println("u set as data.getJSONObject(" + i + ")");

//                        JSONObject newDataJSONObject = u.getJSONObject("schoolId");
  //                      System.out.println("newDataJSONObject set as u.getJSONObject(schoolId)");

                        Integer schoolId = Integer.valueOf(newDataJSONObject.getString("schoolId"));
                        System.out.println("Integer schoolId set as Integer.valueOf(newDataJSONObject.getString(schoolId))");

                        String schoolName = newDataJSONObject.getString("schoolName");
                        System.out.println("String schoolName set as newDataJSONObject.getString(schoolName)");

                        String schoolStatus = newDataJSONObject.getString("schoolStatus");
                        System.out.println("String schoolStatus set as newDataJSONObject.getString(schoolStatus)");

                        SchoolEntity school = new SchoolEntity(schoolId, schoolName, schoolStatus);
                        System.out.println("School Entity: " + school.getSearchName());

                        dataSet.add(school);
                    }
                    break;

                case "russ":
                    for (int i = 0; i < data.length(); i++)
                    {
                        JSONObject u = data.getJSONObject(i);
                        JSONObject newDataJSONObject = u.getJSONObject("russId");
                        Long russId = Long.valueOf(newDataJSONObject.getString("russId"));
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

    /**
     * Returns the SearchView for use in ViewSwitchers
     */
    public View getSearchView()
    {
        return searchView;
    }
}