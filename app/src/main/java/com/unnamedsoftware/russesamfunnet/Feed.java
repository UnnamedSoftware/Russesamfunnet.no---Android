package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;



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

        drawerLayout = findViewById(R.id.navigationDrawer);
        drawerToggle = setUpDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);

        nav = findViewById(R.id.navList);
        setupDrawerContent(nav);
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

    public void selectDrawerItem(MenuItem menuItem)
    {
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId())
        {
            case R.id.profile:
                fragmentClass = UserProfile.class;
                break;
            case R.id.scoreboard:
                fragmentClass = Scoreboard.class;
                break;

            default:
                fragmentClass = Feed.class;
        }
        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.feedContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.scoreboard:
                Intent intent = new Intent(this, Scoreboard.class);
                this.startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);
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
