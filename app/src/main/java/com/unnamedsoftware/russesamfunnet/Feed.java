package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.FeedPost;
import com.unnamedsoftware.russesamfunnet.RecyclerView.RecyclerViewFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

        url = getString(R.string.url) + "feed";

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet - Feed");

        drawerLayout = findViewById(R.id.navigationDrawer);
        drawerToggle = setUpDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);

        nav = findViewById(R.id.navList);
        setupDrawerContent(nav);
/*
        try
        {
            getFeed();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
*/
dummy();
        recyclerView = findViewById(R.id.recycler_view_feed);
        recyclerViewFeed = new RecyclerViewFeed(feedPosts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerViewFeed);

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
                intent = new Intent(this, Knot.class);
                intent.putExtra("knot_entity", new KnotEntity(10001, "Drink Beer", "Beer"));
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
    private void getFeed() throws IOException
    {
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = jsonParser.getJSONFromUrl(url);

        try
        {
            posts = jsonObject.getJSONArray(TAG_feed);

            for(int i = 0; i < posts.length(); i++)
            {
                JSONObject u = posts.getJSONObject(i);
                Integer russId = Integer.valueOf(u.getString(TAG_RUSSID));
                String firstName = u.getString(TAG_FIRSTNAME);
                String surname = u.getString(TAG_SURNAME);
                String post = u.getString(TAG_POST);

                FeedPost posts = new FeedPost(firstName,surname,russId,post);
                feedPosts.add(posts);
            }
            recyclerViewFeed.notifyDataSetChanged();
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void dummy()
    {
        FeedPost post = new FeedPost("Bob","Baker",34,"Donec euismod, tortor in rutrum dictum, mauris orci mattis nulla, vitae vestibulum nibh tortor eu mauris. Quisque interdum lacus vitae tellus tincidunt, sed egestas ex convallis. Sed non nibh nec urna fermentum luctus ut sit amet massa. Praesent cursus efficitur ex at massa nunc.");
        feedPosts.add(post);

        post = new FeedPost("Julia","Flowers",75,"Ut et semper quam. Ut pharetra tellus convallis libero venenatis, ut interdum ante facilisis. Aenean tempor sapien ut elit mollis, condimentum maximus velit euismod. Nunc efficitur lacus tellus, a sagittis odio placerat sed. Etiam et aliquet augue. Interdum et malesuada fames id.");
        feedPosts.add(post);

        post = new FeedPost("John","Smith",249,"Cras dictum feugiat vulputate. Vivamus sed suscipit lorem, et lobortis neque. Morbi nec pretium nisi. Aenean malesuada metus turpis, sed ullamcorper ex convallis eu. Proin purus mauris, pulvinar non interdum id, tempus vel magna. Vivamus consequat tortor tempor consectetur metus.");
        feedPosts.add(post);

        post = new FeedPost("Kristine","Krystal",758,"Morbi dictum nulla blandit massa pulvinar efficitur et id dolor. Cras iaculis accumsan enim quis placerat. Morbi mauris lectus, egestas sit amet viverra sit amet, laoreet sed quam. Morbi et porta nulla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere sed.");
        feedPosts.add(post);

        post = new FeedPost("Teddy","Fresh",13,"Cras dictum feugiat vulputate. Vivamus sed suscipit lorem, et lobortis neque. Morbi nec pretium nisi. Aenean malesuada metus turpis, sed ullamcorper ex convallis eu. Proin purus mauris, pulvinar non interdum id, tempus vel magna. Vivamus consequat tortor tempor consectetur metus.");
        feedPosts.add(post);

        post = new FeedPost("Lilly","Evens",567,"In finibus finibus mollis. Sed sed nisl at turpis lobortis sollicitudin eu a nisi. Integer rhoncus, arcu vel blandit euismod, est arcu mattis ex, sit amet accumsan sapien diam nec turpis. In sagittis odio sit amet neque venenatis ultricies. Donec ullamcorper interdum lacus metus.");
        feedPosts.add(post);

        post = new FeedPost("Kim","Jong-Un ",894,"Very nice!");
        feedPosts.add(post);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        setTitle("Russesamfunnet - Feed");
    }

    private ActionBarDrawerToggle setUpDrawerToggle()
    {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView nav)
    {
        nav.setNavigationItemSelectedListener
                (new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
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
