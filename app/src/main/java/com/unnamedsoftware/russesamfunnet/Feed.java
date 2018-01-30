package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;



public class Feed extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav;

    private ActionBarDrawerToggle drawerToggle;

    /**
     *  https://guides.codepath.com/android/fragment-navigation-drawer
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet - Feed");

        drawerLayout = findViewById(R.id.navigationDrawer);
        drawerToggle = setUpDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);

        nav = findViewById(R.id.navList);
        setupDrawerContent(nav);




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
