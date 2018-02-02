package com.unnamedsoftware.russesamfunnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.RecyclerViewScoreboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Scoreboard extends AppCompatActivity
{
    private List<ScoreboardEntity> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewScoreboard recyclerViewScoreboard;
    private JSONObject jsonObject = null;

    private String url;

    // JSON Node names
    private static final String TAG_SCOREBOARDID = "scoreboardId";
    private static final String TAG_POINTS = "points";
    private static final String TAG_POSITION = "position";
    private static final String TAG_RUSS_ID = "russId";



    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        //getString(R.string.url)
        url =  "http://localhost:8080/scoreboardTop10?theRussId=1";

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

        this.recyclerView = findViewById(R.id.recycler_view_scoreboard);
        this.recyclerViewScoreboard = new RecyclerViewScoreboard(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerViewScoreboard);
    }

    private void setJsonObject(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    /**
     * Uses the JSONParser to request the scoreboard from the server.
     */
    private void getRussScoreboard() throws IOException
    {
        try {
            new JSONParser(new JSONParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    System.out.println("Inside onPostExecute");
                    setJsonObject(jsonObject);
                }
            }).execute(new URL(url));
        }   catch (MalformedURLException e)
        {
            e.printStackTrace();
        }



        JSONArray users = null;
        try {
            users = jsonObject.toJSONArray(jsonObject.names());
        }catch (Exception e)
        {
            System.out.println(e.fillInStackTrace());
        }

        try
        {
            System.out.println("Hi?");
            System.out.println(users.length());
            for(int i = 1; i < users.length(); i++)
            {
                JSONObject u = users.getJSONObject(i);

                Integer scoreboardId = Integer.valueOf(u.getString(TAG_SCOREBOARDID));
                Integer points = Integer.valueOf(u.getString(TAG_POINTS));
                Integer position = Integer.valueOf(u.getString(TAG_POSITION));

                Integer russId = Integer.valueOf(u.getString(TAG_RUSS_ID));
                String russStatus = u.getString("russStatus");
                String firstName = u.getString("firstName");
                String lastName = u.getString("lastName");
                String email = u.getString("email");
                String russPassword = u.getString("russPassword");
                String profilePicture = u.getString("profilePicture");
                String russCard = u.getString("russCard");
                String russRole = u.getString("russRole");
                Integer russYear = Integer.valueOf(u.getString("russYear"));
                Integer schoolId = Integer.valueOf(u.getString("schoolId"));
                String schoolName = u.getString("schoolName");
                String schoolStatus = u.getString("schoolStatus");

                SchoolEntity school = new SchoolEntity(schoolId, schoolName, schoolStatus);
                RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear);
                System.out.println(russId);
                ScoreboardEntity user = new ScoreboardEntity(scoreboardId, points, position, russ);
                userList.add(user);
            }
            recyclerViewScoreboard.notifyDataSetChanged();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

/**
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

        user = new ListUser("Girts" ,"Strazdins ", 13,19);
        userList.add(user);
    }
*/
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
