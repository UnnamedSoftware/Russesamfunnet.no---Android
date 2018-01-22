package com.unnamedsoftware.russesamfunnet;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Feed extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArrayAdapter<String> drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigationDrawer);
        drawerList = (ListView) findViewById(R.id.navList);
        activityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();




    }

    /**
     * Populates the navigation menu
     */
    private void addDrawerItems()
    {
        String[] optionsArray = {"Profil", "Scoreboard", "Knute liste", "Instillinger", "Logout"};
        drawerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, optionsArray);
        drawerList.setAdapter(drawerAdapter);


        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    private void setupDrawer()
    {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
