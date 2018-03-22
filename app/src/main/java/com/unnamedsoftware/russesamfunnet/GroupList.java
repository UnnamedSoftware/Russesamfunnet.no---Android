package com.unnamedsoftware.russesamfunnet;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.unnamedsoftware.russesamfunnet.Entity.GroupEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.GroupListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the list over the groups the user are a part of.
 * Can also create or join new groups here.
 * <p>
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupList extends AppCompatActivity
{
    private FloatingActionButton floatingActionButton;
    private List<GroupEntity> groupEntityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GroupListAdapter groupListAdapter;
    private String url;

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
                addNewGroup();
                //Toast.makeText(GroupList.this, "I've been clicked! :O", Toast.LENGTH_SHORT).show();
            }
        });

        System.out.println(getSupportActionBar());
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dummy();

        this.recyclerView = findViewById(R.id.glaGroup);
        this.groupListAdapter = new GroupListAdapter(groupEntityList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(groupListAdapter);
    }

    /**
     * Displays the dialog for joining or creating new groups
     */
    private void addNewGroup()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Grupper");
        dialog.setContentView(R.layout.group_list_dialog);

        Button addNewGroupButton = dialog.findViewById(R.id.addNewGroupButton);
        addNewGroupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void dummy()
    {
        GroupEntity group = new GroupEntity(1, "Data vg3");
        groupEntityList.add(group);
        group = new GroupEntity(2, "Cult of conflict");
        groupEntityList.add(group);
        group = new GroupEntity(3, "Gjengen");
        groupEntityList.add(group);
        group = new GroupEntity(4, "Sy klubben");
        groupEntityList.add(group);
        group = new GroupEntity(5, "Borgund elektro linje");
        groupEntityList.add(group);
    }

}
