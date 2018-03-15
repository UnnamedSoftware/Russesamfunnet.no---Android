package com.unnamedsoftware.russesamfunnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Displays all the members in the group in a scoreboard arrangement
 *
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupScoreboardMembersList extends AppCompatActivity
{

    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_scoreboard);

        this.groupName = ((Global) this.getApplication()).getGroupName();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Scoreboard - " + groupName);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


}
