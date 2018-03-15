package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

/**
 * The group hub, contains a preview of the top collectors (top 3/5) and a link to the complete list.
 * Also contains the group chat
 *
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupHub extends AppCompatActivity
{
    private String groupName;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_hub);

        this.groupName = getIntent().getExtras().getString("groupName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gruppe: " + groupName);

        this.floatingActionButton = findViewById(R.id.view_all_members);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Toast.makeText(GroupHub.this, "I've been clicked! :O", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GroupHub.this, GroupScoreboardMembersList.class);
                intent.putExtra("groupName",groupName);
                startActivity(intent);
            }
        });

        System.out.println(getSupportActionBar());
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}
