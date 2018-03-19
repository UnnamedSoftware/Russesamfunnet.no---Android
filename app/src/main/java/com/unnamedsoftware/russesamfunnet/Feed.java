package com.unnamedsoftware.russesamfunnet;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.RecyclerView.FeedPost;
import com.unnamedsoftware.russesamfunnet.RecyclerView.RecyclerViewFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class Feed extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav;

    private ActionBarDrawerToggle drawerToggle;

    private List<FeedPost> feedPosts = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewFeed recyclerViewFeed;
    private JSONArray jsonArray = null;

    private String url;

    // JSON Node names
    private static final String TAG_feed = "feed";
    private static final String TAG_RUSSID = "russID";
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_SURNAME = "lastName";
    private static final String TAG_POST = "post";

    JSONArray posts = null;

    /**
     *  https://guides.codepath.com/android/fragment-navigation-drawer
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (getString(R.string.url) + "schoolFeed?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        }else {
            System.out.println(((MyApplication) this.getApplication()).getAccessToken());
            url = getString(R.string.url) + "schoolFeed?accessToken=" + ((MyApplication) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet - Feed");

        drawerLayout = findViewById(R.id.navigationDrawer);
        drawerToggle = setUpDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);


        nav = findViewById(R.id.navList);
        setupDrawerContent(nav);
        drawerLayout.requestLayout();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try
        {
            getFeed();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        recyclerView = findViewById(R.id.recycler_view_feed);
        recyclerViewFeed = new RecyclerViewFeed(feedPosts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerViewFeed);


        Button button = (Button) findViewById(R.id.button_chatbox_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage((EditText) findViewById(R.id.edittext_chatbox));
            }
        });


    }

    public void sendMessage(EditText editText)
    {
        try {
            String message = editText.getText().toString();
            System.out.println(message);
            String urlSend;
            if (AccessToken.getCurrentAccessToken() != null)
            {
                System.out.println(AccessToken.getCurrentAccessToken().getToken());
                urlSend = (getString(R.string.url)
                        + "postFeedToSchool?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                        + "&type=facebook"
                        + "&message=" + message;
            }else {
                System.out.println(((MyApplication) this.getApplication()).getAccessToken());
                urlSend = getString(R.string.url)
                        + "postFeedToSchool?accessToken=" + ((MyApplication) this.getApplication()).getAccessToken()
                        + "&type=russesamfunnet"
                        + "&message=" + message;
            }
            editText.setText("");

            try {
                new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                    @Override
                    public void onPostExecute(JSONObject jsonObject) {
                        if(jsonObject != null)
                        {

                            try {
                                recyclerViewFeed.clear();
                                getFeed();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }).execute(new URL(urlSend));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        } catch (Exception e)
        {
            System.out.println(e.fillInStackTrace());
        }
    }




    /**
     * This method designates what happens when a menu item are selected in the navigation drawer.
     * @param menuItem
     */
    public void selectDrawerItem(MenuItem menuItem)
    {
        Intent intent;
        switch (menuItem.getItemId())
        {
            case R.id.profile:
                intent = new Intent(this, UserProfile.class);
                this.startActivity(intent);
                break;

            case R.id.scoreboard:
                intent = new Intent(this, Scoreboard.class);
                this.startActivity(intent);
                break;

            case R.id.knotList:
                intent = new Intent(this, KnotList.class);
                this.startActivity(intent);
                break;

            case R.id.settings:
                intent = new Intent(this, Settings.class);
                this.startActivity(intent);
                break;
            default:

        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }


    /**
     * Uses the JSONParser to request the feed from the server.
     */
    private void getFeed() throws IOException {
        try {
            new JSONParser(new JSONParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONArray jsonArray) {
                    fillFeed(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param jsonArray
     */
        public void fillFeed(JSONArray jsonArray)
    {
        try
        {
            posts = jsonArray;

            for(int i = 0; i < posts.length(); i++)
            {
                JSONObject u = posts.getJSONObject(i);
                JSONObject newRussObject = u.getJSONObject("russId");
                Long russId = Long.valueOf(newRussObject.getString("russId"));
                String firstName = newRussObject.getString("firstName");
                String surname = newRussObject.getString("lastName");
                String post = u.getString("message");

                FeedPost posts = new FeedPost(firstName,surname,russId,post);
                feedPosts.add(posts);
            }
            recyclerViewFeed.notifyDataSetChanged();

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        setTitle("Russesamfunnet - Feed");
    }

    private ActionBarDrawerToggle setUpDrawerToggle()
    {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        //toolbar.bringToFront();
        //drawerLayout.requestLayout();
        return drawerToggle;
    }

    private void setupDrawerContent(NavigationView nav)
    {
        System.out.println("NAV");
        nav.bringToFront();
        nav.setNavigationItemSelectedListener
                (new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        System.out.println("Selected");
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }




}
