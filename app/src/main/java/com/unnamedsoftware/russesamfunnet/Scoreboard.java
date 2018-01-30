package com.unnamedsoftware.russesamfunnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.unnamedsoftware.russesamfunnet.RecyclerView.ListUser;
import com.unnamedsoftware.russesamfunnet.RecyclerView.RecyclerViewScoreboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Scoreboard extends AppCompatActivity
{
    private List<ListUser> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewScoreboard recyclerViewScoreboard;

    private String url = getString(R.string.url) + "scoreboardTop10";

    // JSON Node names
    private static final String TAG_RUSS = "russ";
    private static final String TAG_RUSSID = "russID";
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_SURNAME = "lastName";
    private static final String TAG_POSITION = "position";

    JSONArray users = null;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet - Scoreboard");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try
        {
            getRussScoreboard();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewScoreboard = new RecyclerViewScoreboard(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewScoreboard);


    }

    /**
     * Uses the JSONParser to request the scoreboard from the server.
     */
    private void getRussScoreboard() throws IOException
    {
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = jsonParser.getJSONFromUrl(url);

        try
        {
            users = jsonObject.getJSONArray(TAG_RUSS);

            for(int i = 0; i < users.length(); i++)
            {
                JSONObject u = users.getJSONObject(i);
                Integer russId = Integer.valueOf(u.getString(TAG_RUSSID));
                String firstName = u.getString(TAG_FIRSTNAME);
                String surname = u.getString(TAG_SURNAME);
                Integer position = Integer.valueOf(u.getString(TAG_POSITION));

                ListUser user = new ListUser(firstName,surname,russId,position);
                userList.add(user);
            }
            recyclerViewScoreboard.notifyDataSetChanged();
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    private void dummy()
    {
        ListUser user = new ListUser("Ken","Netland", 164535, 1);
        userList.add(user);

        user = new ListUser("Julia", "Aaby", 26454654, 2);
        userList.add(user);

        user = new ListUser("Gustav", "Buras", 6484,3);
        userList.add(user);

        user = new ListUser("Svenn", "Valen", 4468454,4);
        userList.add(user);

        user = new ListUser("Hallvard" ,"Roisum",84644 ,5);
        userList.add(user);

        user = new ListUser("Jostein", "Kringen", 98656,6);
        userList.add(user);

        user = new ListUser("Inga" ,"Estrem",846568 ,7);
        userList.add(user);

        user = new ListUser("Margrethe", "Svenningsen", 8846548,8);
        userList.add(user);

        user = new ListUser("Torbjørg" ,"Odegaard", 986,9);
        userList.add(user);

        user = new ListUser("Karoline", "Handal", 18650, 10);
        userList.add(user);

        user = new ListUser("Girts" ,"Strazdins ", 18549,19);
        userList.add(user);

        recyclerViewScoreboard.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
