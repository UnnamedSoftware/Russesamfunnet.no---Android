package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.unnamedsoftware.russesamfunnet.Entity.FeedEntity;
import com.unnamedsoftware.russesamfunnet.Entity.GroupEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.FeedAdapter;
import com.unnamedsoftware.russesamfunnet.RecyclerView.GroupHubUserListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The group hub, contains a preview of the top collectors (top 3/5) and a link to the complete list.
 * Also contains the group chat
 * <p>
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupHub extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    private String groupName;
    private Long groupID;
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText chatBox;

    private RecyclerView recyclerViewRuss;
    private RecyclerView recyclerViewFeed;
    private List<ScoreboardEntity> scoreboardEntityList = new ArrayList<>();
    private List<RussEntity> russEntityList = new ArrayList<>();
    private List<FeedEntity> feedEntityList = new ArrayList<>();
    private GroupHubUserListAdapter groupHubUserListAdapter;
    private FeedAdapter feedAdapter;
    private HashMap<FeedEntity, Bitmap> feedMap = new HashMap<>();
    private Bitmap userImage;
    private HashMap<String, Bitmap> images = new HashMap<>();

    private String url;


    @SuppressLint("ClickableViewAccessibility")
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
            this.groupID = (Long) bundle.get("groupID");
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
            url = (getString(R.string.url) + "groupFeed?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&groupId=" + groupID);
        }else {
            System.out.println(((Global) this.getApplication()).getAccessToken());
            url = (getString(R.string.url) + "groupFeed?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet&groupId=" + groupID);
        }
        System.out.println(url);
        try {
            System.out.println("_______________________________________________________________________feed1");
            getFeed();
            System.out.println("_______________________________________________________________________feed2");
        }  catch (Exception e)
        {
            e.printStackTrace();
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

      //  dummyChat();
        getTop3();

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
        Long russId = ((Global)this.getApplication()).getRussId();
        System.out.println("--------------------------------------------------------" + russId);
        this.recyclerViewFeed = findViewById(R.id.recycler_view_feed);
        this.feedAdapter = new FeedAdapter(feedEntityList,this,getDeleteURL(),russId, groupID, getRemoveUrl(),(((Global) this.getApplication()).getImageLoader()), feedMap);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewFeed.setLayoutManager(layoutManager);
        recyclerViewFeed.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFeed.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewFeed.setAdapter(feedAdapter);

        recyclerViewFeed.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (recyclerViewReadyCallback != null) {
                    recyclerViewReadyCallback.onLayoutReady();
                }
                recyclerViewFeed.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        recyclerViewReadyCallback = new Feed.RecyclerViewReadyCallback() {
            @Override
            public void onLayoutReady() {
                loadRecyclerViewData();
            }
        };


        this.chatBox = findViewById(R.id.edittext_chatbox);
        chatBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable)
            {
                String currentText = editable.toString();
                int currentLength = currentText.length();
                TextView groupCharCount = findViewById(R.id.groupCharCount);
                groupCharCount.setText(String.valueOf(currentLength));
            }
        });

        Button button = findViewById(R.id.button_chatbox_send);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendMessage(chatBox);
            }
        });


        swipeRefreshLayout = this.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable()
        {

            @Override
            public void run()
            {

                swipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                loadRecyclerViewData();
            }
        });


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
    }
    private Feed.RecyclerViewReadyCallback recyclerViewReadyCallback;

    public interface RecyclerViewReadyCallback {
        void onLayoutReady();
    }

    @Override
    public void onRefresh()
    {
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData()
    {
        swipeRefreshLayout.setRefreshing(true);
        try
        {
            getFeed();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        swipeRefreshLayout.setRefreshing(false);
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
                        + "&message=" + message
                        + "&groupId=" + groupID;
            } else
            {
                System.out.println(((Global) this.getApplication()).getAccessToken());
                urlSend = getString(R.string.url)
                        + "postFeedToGroup?accessToken=" + ((Global) this.getApplication()).getAccessToken()
                        + "&type=russesamfunnet"
                        + "&message=" + message
                        + "&groupId=" + groupID;
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
        JSONArray posts;
        try
        {
            feedEntityList.clear();
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
                Long groupID = Long.valueOf(jsonObject.getString("groupId"));
                String groupName = jsonObject.getString("groupName");

                GroupEntity groupEntity = new GroupEntity(groupID, groupName);
                RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear,profilePicture,russCard);
                String message = u.getString("message");
                Long feedId = u.getLong("feedId");
                if(u.getString("type").equals("Group")) {
                    FeedEntity post = new FeedEntity(feedId, message, russ);
                    feedEntityList.add(0, post);
                    setProfilePicture(post);
                }
            }
            feedAdapter.notifyDataSetChanged();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void setProfilePicture(final FeedEntity feedEntity)
    {
        String url = feedEntity.getRussId().getProfilePicture();
        System.out.println(url);
        String userImageURI = "http://158.38.101.162:8080/files/" + url;

        if (!url.equals("null")) {
            if(images.containsKey(url))
            {
                feedMap.put(feedEntity, images.get(url));
                feedAdapter.notifyDataSetChanged();
            }
            ((Global) this.getApplication()).getImageLoader().loadImage(userImageURI, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    userImage = loadedImage;
                    try {
                        feedMap.put(feedEntity, userImage);
                        feedAdapter.notifyDataSetChanged();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
            });

        } else {
            userImage = null;
            try {
                feedMap.put(feedEntity, userImage);
                feedAdapter.notifyDataSetChanged();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }

    private void getTop3()
        {
           String scoreboardurl;
            if (AccessToken.getCurrentAccessToken() != null)
            {
                System.out.println(AccessToken.getCurrentAccessToken().getToken());
                scoreboardurl = (getString(R.string.url) + "scoreboardGroupTop3?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&groupId=" + groupID);
            }else {
                System.out.println(((Global) this.getApplication()).getAccessToken());
                scoreboardurl = (getString(R.string.url) + "scoreboardGroupTop3?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet&groupId=" + groupID);
            }

            try
            {
                new JSONParser(new JSONParser.OnPostExecute()
                {
                    @Override
                    public void onPostExecute(JSONArray jsonArray) throws JSONException
                    {
                        addScoreboardToList(jsonArray);
                    }
                }).execute(new URL(scoreboardurl));
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }

        private void addScoreboardToList(JSONArray jsonArray){

            JSONArray users = null;
            try
            {
                users = jsonArray;
                if (jsonArray == null) System.out.println("Scoreboard DEADBEEF");
                System.out.println("Hi?");
                System.out.println(users.length());
                int length = users.length();
                for(int i = 0; i < length && i<= 2; i++)
                {
                    JSONObject u = users.getJSONObject(i);

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
                    JSONObject newSchoolObject = newRussObject.getJSONObject("schoolId");
                    Integer schoolId = Integer.valueOf(newSchoolObject.getString("schoolId"));
                    String schoolName = newSchoolObject.getString("schoolName");
                    String schoolStatus = newSchoolObject.getString("schoolStatus");

                    SchoolEntity school = new SchoolEntity(schoolId, schoolName, schoolStatus);
                    RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear,profilePicture,russCard);
                    System.out.println(russId);
                    ScoreboardEntity user = new ScoreboardEntity(scoreboardId, points, position, russ);
                    scoreboardEntityList.add(user);
                    russEntityList.add(russ);

                }
                groupHubUserListAdapter.notifyDataSetChanged();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    private String getDeleteURL()
    {
        String newUrl;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            newUrl = (getString(R.string.url) + "deleteMessage?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&feedId=");

        } else
        {
            newUrl = getString(R.string.url) + "deleteMessage?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet&feedId=";
        }
        return newUrl;
    }

    private String getRemoveUrl()
    {
        String newUrl;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            newUrl = (getString(R.string.url) + "removeGroupMember?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&groupId=" + groupID + "&russId=");

        } else
        {
            newUrl = getString(R.string.url) + "removeGroupMember?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet&groupId=" + groupID +"&russId=";
        }
        return newUrl;
    }
}
