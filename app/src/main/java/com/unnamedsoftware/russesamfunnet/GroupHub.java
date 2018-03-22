package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.unnamedsoftware.russesamfunnet.Entity.FeedEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.GroupHubUserListAdapter;

import java.util.ArrayList;
import java.util.List;

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

    private RecyclerView recyclerViewRuss;
    private RecyclerView recyclerViewFeed;

    private List<RussEntity> russEntityList = new ArrayList<>();
    private List<FeedEntity> feedEntityList = new ArrayList<>();
    private GroupHubUserListAdapter groupHubUserListAdapter;
    // private GroupHubChat


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_hub);

        try
        {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            this.groupName = (String) bundle.get("groupName");
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            try
            {
                this.groupName = ((Global) this.getApplication()).getGroupName();
            }catch (NullPointerException e1)
            {
                e1.printStackTrace();
            }
        }

        ((Global) this.getApplication()).setGroupName(this.groupName);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gruppe: " + groupName);

        this.floatingActionButton = findViewById(R.id.view_all_members);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //Toast.makeText(GroupHub.this, "I've been clicked! :O", Toast.LENGTH_SHORT).show();
                startActivity( new Intent(GroupHub.this, GroupScoreboardMembersList.class));
            }
        });

        dummyChat();
        dummyTop3();

        System.out.println(getSupportActionBar());
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.recyclerViewRuss = findViewById(R.id.ghuUsers);
        this.groupHubUserListAdapter  = new GroupHubUserListAdapter(russEntityList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GroupHub.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRuss.setLayoutManager(horizontalLayoutManager);
        recyclerViewRuss.setItemAnimator(new DefaultItemAnimator());
        //recyclerViewRuss.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerViewRuss.setAdapter(groupHubUserListAdapter);




    }

    /**
     * Long russId, String russStatus, String firstName,
     * String lastName, String email, String russPassword, String russRole, int russYear
     */
    private void dummyTop3()
    {
        Long id = new Long(8846548);
        RussEntity russEntity = new RussEntity(id,"russ", "Girts" ,"Strazdins", "jenw", "lkjh", "efef", 1222);
        russEntityList.add(russEntity);

        russEntity = new RussEntity(id,"russ", "Svenn" ,"Valen", "jenw", "lkjh", "efef", 1222);
        russEntityList.add(russEntity);

        russEntity = new RussEntity(id,"russ", "Julia" ,"Flowers", "jenw", "lkjh", "efef", 1222);
        russEntityList.add(russEntity);
    }

    private void dummyChat()
    {

    }
}
