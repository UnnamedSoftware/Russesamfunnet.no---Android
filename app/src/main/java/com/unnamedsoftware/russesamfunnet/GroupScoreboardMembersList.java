package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
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
import java.util.HashMap;
import java.util.List;

/**
 * Displays all the members in the group in a scoreboard arrangement
 *
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupScoreboardMembersList extends AppCompatActivity
{
    private String groupName;
    private HashMap<ScoreboardEntity, Bitmap> scoreboardMap = new HashMap<>();
    private Bitmap userImage;
    private HashMap<String, Bitmap> images = new HashMap<>();
    private String url;

    private List<ScoreboardEntity> scoreboardEntityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ScoreboardAdapter scoreboardAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_scoreboard);

        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (getString(R.string.url) + "groupScoreboard?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&groupId=" + ((Global) this.getApplication()).getGroupID());
        } else
        {
            url = getString(R.string.url) + "groupScoreboard?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet&groupId=" + ((Global) this.getApplication()).getGroupID();
        }

        this.groupName = ((Global) this.getApplication()).getGroupName();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Scoreboard - " + groupName);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        try
        {
            getGroupScoreboard();
            getRussId();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        this.recyclerView = findViewById(R.id.recycler_view_scoreboard);
        this.scoreboardAdapter = new ScoreboardAdapter(scoreboardEntityList, this, scoreboardMap);
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
 */    }

    /**
     * Uses the JSONParser to request the scoreboard from the server.
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void getGroupScoreboard() throws IOException {
        try {
            new JSONParser(new JSONParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONArray jsonArray) {
                    if (jsonArray == null) System.out.println("\n Inside onPostExecute \n");

                    fillScoreboard(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void getRussId() throws IOException {
        String newUrl;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            newUrl = (getString(R.string.url) + "userRuss?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        }else {
            newUrl = getString(R.string.url) + "userRuss?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    try {
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(newUrl));
        } catch (MalformedURLException e) {
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
            System.out.println("Hi?");
            System.out.println(users.length());
            int length = users.length();
            for(int i = 0; i < length; i++)
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
                scoreboardEntityList.add(user);
                setProfilePicture(user);
            }
            scoreboardAdapter.notifyDataSetChanged();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setProfilePicture(final ScoreboardEntity scoreboardEntity)
    {
        final String url = scoreboardEntity.getRussId().getProfilePicture();
        System.out.println(url);
        String userImageURI = "http://158.38.101.162:8080/files/" + url;

        if (!url.equals("null")) {
            if(images.containsKey(url))
            {
                scoreboardMap.put(scoreboardEntity, images.get(url));
                scoreboardAdapter.notifyDataSetChanged();
            }
            ((Global) this.getApplication()).getImageLoader().loadImage(userImageURI, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    userImage = loadedImage;
                    System.out.println("TRUE");
                    images.put(url, userImage);
                    scoreboardMap.put(scoreboardEntity, userImage);
                    scoreboardAdapter.notifyDataSetChanged();
                }
            });
        } else {
            userImage = null;
            images.put(url,null);
            scoreboardMap.put(scoreboardEntity, userImage);
            scoreboardAdapter.notifyDataSetChanged();
        }


    }

}
