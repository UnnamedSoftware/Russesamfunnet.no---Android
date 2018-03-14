package com.unnamedsoftware.russesamfunnet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

/**
 * Contains the list over the groups the user are a part of.
 * Can also create or join new groups here.
 * <p>
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupList extends AppCompatActivity
{
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gruppe liste");

        this.floatingActionButton = findViewById(R.id.add_group);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(GroupList.this, "I've been clicked! :O", Toast.LENGTH_SHORT).show();
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
