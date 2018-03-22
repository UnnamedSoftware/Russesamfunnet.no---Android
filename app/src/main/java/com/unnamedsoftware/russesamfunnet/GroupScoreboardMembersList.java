package com.unnamedsoftware.russesamfunnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.RecyclerViewScoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all the members in the group in a scoreboard arrangement
 *
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupScoreboardMembersList extends AppCompatActivity
{
    private String groupName;
    private String url;

    private Long id = new Long(234);

    private List<ScoreboardEntity> scoreboardEntityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewScoreboard recyclerViewScoreboard;

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

        
        this.recyclerView = findViewById(R.id.recycler_view_scoreboard);
        this.recyclerViewScoreboard = new RecyclerViewScoreboard(scoreboardEntityList, id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerViewScoreboard);
    }

}
