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
import android.widget.EditText;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.GroupEntity;
import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.GroupListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
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
        getGroups();

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
                EditText editText = findViewById(R.id.group);
                String groupName = editText.getText().toString();
                String url = "";
                if (AccessToken.getCurrentAccessToken() != null)
                {
                    System.out.println(AccessToken.getCurrentAccessToken().getToken());
                    url = (getString(R.string.url) + "createGroup?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&groupName="+groupName);
                }else {
                    url = getString(R.string.url) + "createGroup?accessToken=" + ((Global) getApplication()).getAccessToken() + "&type=russesamfunnet&groupName="+groupName;
                }
                System.out.println(url);
                sendCreateGroup(url);

            }
        });

        dialog.show();

    }

    private void sendCreateGroup(String url)
    {
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    try{
                        if(jsonObject.getString("response").equals("true")) {
                            getGroups();
                        } else {
                            System.out.println(jsonObject.getString("response"));
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void getGroups()
    {
        String url = "";
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (getString(R.string.url) + "groups?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        }else {
            url = getString(R.string.url) + "groups?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }
        try {
            new JSONParser(new JSONParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONArray jsonArray) {
                    try{
                        fillGroupList(jsonArray);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void fillGroupList(JSONArray jsonArray)
    {
        try
        {

            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject group = jsonArray.getJSONObject(i).getJSONObject("groupId");
                Long groupId = group.getLong("groupId");
                String groupName = group.getString("groupName");

                groupEntityList.add(new GroupEntity(groupId, groupName));
            }
            this.groupListAdapter.notifyDataSetChanged();
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


}
