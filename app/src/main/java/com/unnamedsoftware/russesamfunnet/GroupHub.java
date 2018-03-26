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
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.FeedEntity;
import com.unnamedsoftware.russesamfunnet.Entity.GroupEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.FeedAdapter;
import com.unnamedsoftware.russesamfunnet.RecyclerView.GroupHubUserListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The group hub, contains a preview of the top collectors (top 3/5) and a link to the complete list.
 * Also contains the group chat
 * <p>
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupHub extends AppCompatActivity
{
    private String groupName;
    private Integer groupID;
    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerViewRuss;
    private RecyclerView recyclerViewFeed;

    private List<RussEntity> russEntityList = new ArrayList<>();
    private List<FeedEntity> feedEntityList = new ArrayList<>();
    private GroupHubUserListAdapter groupHubUserListAdapter;
    private FeedAdapter feedAdapter;

    private String url;
    JSONArray posts = null;

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
            this.groupID = (Integer) bundle.get("groupID");
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            try
            {
                this.groupName = ((Global) this.getApplication()).getGroupName();
                this.groupID = ((Global) this.getApplication()).getGroupID();
            } catch (NullPointerException e1)
            {
                e1.printStackTrace();
            }
        }

        ((Global) this.getApplication()).setGroupName(this.groupName);
        ((Global) this.getApplication()).setGroupID(this.groupID);

        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (getString(R.string.url) + "groupFeed?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook" + groupID);
        }else {
            System.out.println(((Global) this.getApplication()).getAccessToken());
            url = (getString(R.string.url) + "groupFeed?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet" + groupID);
        }


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
                startActivity(new Intent(GroupHub.this, GroupScoreboardMembersList.class));
            }
        });

        dummyChat();
        dummyTop3();

        System.out.println(getSupportActionBar());
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.recyclerViewRuss = findViewById(R.id.ghuUsers);
        this.groupHubUserListAdapter = new GroupHubUserListAdapter(russEntityList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GroupHub.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRuss.setLayoutManager(horizontalLayoutManager);
        recyclerViewRuss.setItemAnimator(new DefaultItemAnimator());
        //recyclerViewRuss.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerViewRuss.setAdapter(groupHubUserListAdapter);
/*
        this.recyclerViewFeed = findViewById(R.id.ghuFeed);
        this.feedAdapter = new FeedAdapter(feedEntityList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewFeed.setLayoutManager(layoutManager);
        recyclerViewFeed.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFeed.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewFeed.setAdapter(feedAdapter);
*/
        Button button = findViewById(R.id.group_button_chatbox_send);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendMessage((EditText) findViewById(R.id.group_edittext_chatbox));
            }
        });
    }

    /**
     * Sends text in given EditText to the server.
     *
     * @param editText
     */
    public void sendMessage(EditText editText)
    {
        try
        {
            String message = editText.getText().toString();
            System.out.println(message);
            String urlSend;
            if (AccessToken.getCurrentAccessToken() != null)
            {
                System.out.println(AccessToken.getCurrentAccessToken().getToken());
                urlSend = (getString(R.string.url)
                        + "postFeedToGroup?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                        + "&type=facebook"
                        + "&message=" + message;
            } else
            {
                System.out.println(((Global) this.getApplication()).getAccessToken());
                urlSend = getString(R.string.url)
                        + "postFeedToGroup?accessToken=" + ((Global) this.getApplication()).getAccessToken()
                        + "&type=russesamfunnet"
                        + "&message=" + message;
            }
            editText.setText("");

            try
            {
                new JSONObjectParser(new JSONObjectParser.OnPostExecute()
                {
                    @Override
                    public void onPostExecute(JSONObject jsonObject)
                    {
                        if (jsonObject != null)
                        {
                            try
                            {
                                feedAdapter.clear();
                                getFeed();
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }).execute(new URL(urlSend));
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

        } catch (Exception e)
        {
            System.out.println(e.fillInStackTrace());
        }
    }

    private void getFeed() throws IOException
    {
        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray) throws JSONException
                {
                    fillFeed(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param jsonArray
     */
    public void fillFeed(JSONArray jsonArray)
    {
        try
        {
            posts = jsonArray;

            for(int i = 0; i < posts.length(); i++)
            {
                JSONObject u = posts.getJSONObject(i);
                JSONObject newRussObject = u.getJSONObject("russId");

                Long russId = Long.valueOf(newRussObject.getString("russId"));
                String russStatus = newRussObject.getString("russStatus");
                String firstName = newRussObject.getString("firstName");
                String lastName = newRussObject.getString("lastName");
                String email = newRussObject.getString("email");
                String russPassword = newRussObject.getString("russPassword");
                String profilePicture = newRussObject.getString("profilePicture");
                String russCard = newRussObject.getString("russCard");
                String russRole = newRussObject.getString("russRole");
                Integer russYear = Integer.valueOf(newRussObject.getString("russYear"));

                JSONObject jsonObject = u.getJSONObject("groupId");
                Integer groupID = Integer.valueOf(jsonObject.getString("groupId"));
                String groupName = jsonObject.getString("groupName");

                GroupEntity groupEntity = new GroupEntity(groupID, groupName);
                RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear);
                String message = u.getString("message");
                Long feedId = u.getLong("feedId");
                if(u.getString("type").equals("School")) {
                    FeedEntity posts = new FeedEntity(feedId, message, russ);
                    feedEntityList.add(0, posts);
                }
            }
            feedAdapter.notifyDataSetChanged();

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Long russId, String russStatus, String firstName,
     * String lastName, String email, String russPassword, String russRole, int russYear
     */
    private void dummyTop3()
    {
        Long id = new Long(8846548);
        RussEntity russEntity = new RussEntity(id, "russ", "Girts", "Strazdins", "jenw", "lkjh", "efef", 1222);
        russEntityList.add(russEntity);

        russEntity = new RussEntity(id, "russ", "Svenn", "Valen", "jenw", "lkjh", "efef", 1222);
        russEntityList.add(russEntity);

        russEntity = new RussEntity(id, "russ", "Julia", "Flowers", "jenw", "lkjh", "efef", 1222);
        russEntityList.add(russEntity);
    }

    private void dummyChat()
    {

    }
}
