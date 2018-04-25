package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.ScoreboardAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Scoreboard extends AppCompatActivity
{
    private List<ScoreboardEntity> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ScoreboardAdapter scoreboardAdapter;
    private JSONArray jsonArray = null;

    private String url;

    // JSON Node names
    private static final String TAG_SCOREBOARDID = "scoreboardId";
    private static final String TAG_POINTS = "points";
    private static final String TAG_POSITION = "position";
    private static final String TAG_RUSS_ID = "russId";


    /**
     * @param savedInstanceState
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        //getString(R.string.url)
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (getString(R.string.url) + "scoreboardTop10?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        } else
        {
            url = getString(R.string.url) + "scoreboardTop10?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }

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
        this.scoreboardAdapter = new ScoreboardAdapter(userList, ((Global) this.getApplication()).getRussId());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(scoreboardAdapter);
/**
 //Swipe func.
 ConstraintLayout constraintLayout = findViewById(R.id.KnotLayout);
 constraintLayout.setOnTouchListener(new OnSwipeTouchListener(Knot.this)
 {
 public void onSwipeRight()
 {
 onBackPressed();
 }
 });
 */
    }

    /**
     * Uses the JSONParser to request the scoreboard from the server.
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void getRussScoreboard() throws IOException
    {
        System.out.println(url);
        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray)
                {
                    if (jsonArray == null) System.out.println("\n Inside onPostExecute \n");

                    fillScoreboard(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public void fillScoreboard(JSONArray jsonArray)
    {

        JSONArray users = null;
        try
        {
            users = jsonArray;
            if (jsonArray == null) System.out.println("Scoreboard DEADBEEF");
            System.out.println(users.length());
            int length = users.length();
            for (int i = 0; i < length; i++)
            {
                JSONObject u = users.getJSONObject(i);

                Integer scoreboardId = Integer.valueOf(u.getString("scoreboardId"));
                Integer points = Integer.valueOf(u.getString("points"));
                Integer position = Integer.valueOf(u.getString("position"));


                JSONObject newRussObject = u.getJSONObject("russId");

                Long russId = Long.valueOf(newRussObject.getString("russId"));
                String russStatus = newRussObject.getString("russStatus");
                String firstName = newRussObject.getString("firstName");
                String lastName = newRussObject.getString("lastName");
                String email = newRussObject.getString("email");
                String russPassword = newRussObject.getString("russPassword");
                String profilePicture = newRussObject.getString("profilePicture");
                String russCard = newRussObject.getString("russCard");
                String russRole = newRussObject.getString("russRole");
                Integer russYear = Integer.valueOf(newRussObject.getString("russYear"));
                JSONObject newSchoolObject = newRussObject.getJSONObject("schoolId");
                Integer schoolId = Integer.valueOf(newSchoolObject.getString("schoolId"));
                String schoolName = newSchoolObject.getString("schoolName");
                String schoolStatus = newSchoolObject.getString("schoolStatus");

                SchoolEntity school = new SchoolEntity(schoolId, schoolName, schoolStatus);
                RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear,profilePicture,russCard);
                System.out.println(russId);
                ScoreboardEntity user = new ScoreboardEntity(scoreboardId, points, position, russ);
                userList.add(user);
            }
            scoreboardAdapter.notifyDataSetChanged();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

}
