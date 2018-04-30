package com.unnamedsoftware.russesamfunnet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.unnamedsoftware.russesamfunnet.Entity.FeedEntity;
import com.unnamedsoftware.russesamfunnet.Entity.GroupEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.FeedAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * https://guides.codepath.com/android/fragment-navigation-drawer
 */

public class Feed extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav;

    private ActionBarDrawerToggle drawerToggle;

    private List<FeedEntity> feedPosts = new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private JSONArray jsonArray = null;
    private Bitmap userImage;
    private HashMap<FeedEntity, Bitmap> feedMap = new HashMap<>();
    private HashMap<String, Bitmap> images = new HashMap<>();

    private String url;
    private EditText chatBox;


    // JSON Node names
    private static final String TAG_feed = "feed";
    private static final String TAG_RUSSID = "russID";
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_SURNAME = "lastName";
    private static final String TAG_POST = "post";

    JSONArray posts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_feed);
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (getString(R.string.url) + "schoolFeed?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        } else
        {
            System.out.println(((Global) this.getApplication()).getAccessToken());
            url = getString(R.string.url) + "schoolFeed?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Russesamfunnet");
/**
 try
 {
 System.out.println("Trying to get RussID");
 getRussId();
 } catch (IOException e)
 {
 System.out.println("***Something went wrong***");
 e.printStackTrace();
 }
 */
        drawerLayout = findViewById(R.id.navigationDrawer);
        drawerToggle = setUpDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);

        nav = findViewById(R.id.navList);
        setupDrawerContent(nav);
        drawerLayout.requestLayout();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view_feed);
        feedAdapter = new FeedAdapter(feedPosts,this,getDeleteURL(),((Global)this.getApplication()).getRussId(),((Global)this.getApplication()).getImageLoader(), feedMap);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(feedAdapter);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (recyclerViewReadyCallback != null) {
                    recyclerViewReadyCallback.onLayoutReady();
                }
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
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
                TextView charCount = findViewById(R.id.charCount);
                charCount.setText(String.valueOf(currentLength));
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


        // SwipeRefreshLayout
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
    }
    private RecyclerViewReadyCallback recyclerViewReadyCallback;

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
            feedAdapter.clear();
            getFeed();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        swipeRefreshLayout.setRefreshing(false);
    }


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
                        + "postFeedToSchool?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                        + "&type=facebook"
                        + "&message=" + message;
            } else
            {
                System.out.println(((Global) this.getApplication()).getAccessToken());
                urlSend = getString(R.string.url)
                        + "postFeedToSchool?accessToken=" + ((Global) this.getApplication()).getAccessToken()
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


    /**
     * This method designates what happens when a menu item are selected in the navigation drawer.
     *
     * @param menuItem
     */
    public void selectDrawerItem(MenuItem menuItem)
    {
        Intent intent;
        switch (menuItem.getItemId())
        {
            case R.id.profile:
                intent = new Intent(this, UserProfile.class);
                this.startActivity(intent);
                break;

            case R.id.scoreboard:
                intent = new Intent(this, Scoreboard.class);
                this.startActivity(intent);
                break;

            case R.id.knotList:
                intent = new Intent(this, KnotList.class);
                this.startActivity(intent);
                break;

            case R.id.group:
                startActivity(new Intent(this, GroupList.class));
                break;

         /*   case R.id.event:
                startActivity(new Intent(this, Events.class));
                break;
*/
            case R.id.bugReport:
                reportPost();
                break;

            case R.id.logout:
                LoginManager.getInstance().logOut();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                System.out.println(((Global) this.getApplication()).deleteCache("token"));
                System.out.println(((Global) this.getApplication()).deleteCache("tokenType"));
                finish();
                startActivity(i);
                break;
            default:

        }
        menuItem.setChecked(false);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }


    /**
     * Uses the JSONParser to request the feed from the server.
     */
    private void getFeed() throws IOException
    {
        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray)
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
     * @param jsonArray
     */
    public void fillFeed(JSONArray jsonArray)
    {
        try
        {
            posts = jsonArray;

            for (int i = 0; i < posts.length(); i++)
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
                JSONObject newSchoolObject = newRussObject.getJSONObject("schoolId");
                Integer schoolId = Integer.valueOf(newSchoolObject.getString("schoolId"));
                String schoolName = newSchoolObject.getString("schoolName");
                String schoolStatus = newSchoolObject.getString("schoolStatus");

                SchoolEntity school = new SchoolEntity(schoolId, schoolName, schoolStatus);
                RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear, profilePicture, russCard);
                String message = u.getString("message");
                Long feedId = u.getLong("feedId");
                if (u.getString("type").equals("School"))
                {
                    FeedEntity posts = new FeedEntity(feedId, message, russ);
                    feedPosts.add(0,posts);
                    setProfilePicture(posts);
                }
            }
            feedAdapter.notifyDataSetChanged();

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void setProfilePicture(final FeedEntity feedEntity)
    {
            final String url = feedEntity.getRussId().getProfilePicture();
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
                        System.out.println("TRUE");
                        images.put(url, userImage);
                        feedMap.put(feedEntity, userImage);
                        feedAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                userImage = null;
                images.put(url,null);
                    feedMap.put(feedEntity, userImage);
                    feedAdapter.notifyDataSetChanged();
            }


        }


    @Override
    protected void onResume()
    {
        super.onResume();
        setTitle("Russesamfunnet - Feed");
    }

    private ActionBarDrawerToggle setUpDrawerToggle()
    {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        //toolbar.bringToFront();
        //drawerLayout.requestLayout();
        return drawerToggle;
    }

    private void setupDrawerContent(NavigationView nav)
    {
        System.out.println("NAV");
        nav.bringToFront();
        nav.setNavigationItemSelectedListener
                (new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        System.out.println("Selected");
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {

        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void getRussId() throws IOException
    {
        String newUrl;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            newUrl = (getString(R.string.url) + "userRuss?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        } else
        {
            newUrl = getString(R.string.url) + "userRuss?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }
        try
        {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {
                    try
                    {
                        System.out.println("Russ id: " + jsonObject.getLong("russId"));
                        setRussID(jsonObject.getLong("russId"));

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(newUrl));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    private void setRussID(Long id)
    {
        ((Global) this.getApplication()).setRussId(id);
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

    private void reportPost()
    {
        final Long userID = ((Global) this.getApplication()).getRussId();
        final Context context = this;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.report_bug_dialog);
        final EditText editText = dialog.findViewById(R.id.reportMessage);
        Button submitButton = dialog.findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Report report = new Report();
                report.reportBug(editText.getText().toString(), userID, context);
                dialog.cancel();
            }
        });
        dialog.show();
    }
}

