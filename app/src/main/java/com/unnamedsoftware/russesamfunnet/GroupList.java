package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.unnamedsoftware.russesamfunnet.Entity.GroupEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.GroupListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    Bitmap userImage;
    private HashMap<String, Bitmap> images = new HashMap<>();
    private GroupListAdapter groupListAdapter;
    private HashMap<GroupEntity, List<ScoreboardEntity>> scoreboardMap = new HashMap<>();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gruppeliste");

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
        this.groupListAdapter = new GroupListAdapter(groupEntityList, this, ((Global) this.getApplication()).getRussId(), removeUserUrl(), scoreboardMap, images);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(groupListAdapter);
    }
/**
 //Swipe func.
 ConstraintLayout constraintLayout = findViewById(R.id.KnotLayout);
 constraintLayout.setOnTouchListener(new OnSwipeTouchListener(Knot.this)
 {
 public void onSwipeRight()
 {
 onBackPressed();
 }
 });
 */


    /**
     * Displays the dialog for joining or creating new groups
     */
    private void addNewGroup()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Grupper");
        dialog.setContentView(R.layout.group_list_dialog);
        final EditText editText = (EditText) dialog.findViewById(R.id.group);
        Button addNewGroupButton = dialog.findViewById(R.id.addNewGroupButton);
        addNewGroupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //EditText editText = findViewById(R.id.group);

                String groupName = editText.getText().toString();
                String url = "";
                if (AccessToken.getCurrentAccessToken() != null)
                {
                    System.out.println(AccessToken.getCurrentAccessToken().getToken());
                    url = (getString(R.string.url) + "createGroup?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&groupName=" + groupName);
                } else
                {
                    url = getString(R.string.url) + "createGroup?accessToken=" + ((Global) getApplication()).getAccessToken() + "&type=russesamfunnet&groupName=" + groupName;
                }
                System.out.println(url);
                sendCreateGroup(url);
                groupEntityList.clear();
                dialog.cancel();
                getGroups();

            }
        });

        dialog.show();
    }

    private void sendCreateGroup(String url)
    {
        try
        {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {
                    try
                    {
                        if (jsonObject.getString("response").equals("true"))
                        {
                            getGroups();
                        } else
                        {
                            System.out.println(jsonObject.getString("response"));
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    private void getGroups()
    {
        String url = "";
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (getString(R.string.url) + "groupsWithScoreboard?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        } else
        {
            url = getString(R.string.url) + "groupsWithScoreboard?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }
        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray)
                {
                    try
                    {
                        fillGroupList(jsonArray);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    private void fillGroupList(JSONArray jsonArray)
    {
        try
        {

            for (int i = 0; i < jsonArray.length(); i++)
            {
                List<ScoreboardEntity> result = new ArrayList<>();
                JSONObject group = jsonArray.getJSONObject(i).getJSONObject("group");
                Long groupId = group.getLong("groupId");
                String groupName = group.getString("groupName");
                GroupEntity groupEntity = new GroupEntity(groupId, groupName);
                try {
                for(int t = 0; t <= 2; t++) {
                    JSONObject u = jsonArray.getJSONObject(i).getJSONArray("scoreboard").getJSONObject(t);
                    Integer scoreboardId = Integer.valueOf(u.getString("scoreboardId"));
                    Integer points = Integer.valueOf(u.getString("points"));
                    Integer position = Integer.valueOf(u.getString("position"));

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

                    RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear, profilePicture, russCard);
                    System.out.println(russId);
                    ScoreboardEntity user = new ScoreboardEntity(scoreboardId, points, position, russ);
                    result.add(user);
                    setProfilePicture(user.getRussId());
                }
                }catch (Exception e) {
                    }
                groupEntityList.add(groupEntity);
                scoreboardMap.put(groupEntity, result);
                Iterator it = result.iterator();
            }
            this.groupListAdapter.notifyDataSetChanged();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    private void setProfilePicture(RussEntity russEntity)
    {
            final String url = russEntity.getProfilePicture();
            System.out.println(url);
            String userImageURI = "http://158.38.101.162:8080/files/" + url;

            if (!url.equals("null")) {
                if(images.containsKey(url))
                {
                } else {
                ((Global) this.getApplication()).getImageLoader().loadImage(userImageURI, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        userImage = loadedImage;
                        System.out.println("TRUE");
                        images.put(url, loadedImage);
                        groupListAdapter.notifyDataSetChanged();
                    }
                });}
            } else {
                userImage = null;
                try {
                    images.put(url,null);
                    groupListAdapter.notifyDataSetChanged();
                } catch (Exception e)
                {
                }
            }
    }

    private String removeUserUrl()
    {
        String newUrl;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            newUrl = (getString(R.string.url) + "removeGroupMember?accessToken=" + AccessToken.getCurrentAccessToken().getToken());

        } else
        {
            newUrl = getString(R.string.url) + "removeGroupMember?accessToken=" + ((Global) this.getApplication()).getAccessToken();
            System.out.println("New url: " + newUrl);
        }
        return newUrl;
    }
}
