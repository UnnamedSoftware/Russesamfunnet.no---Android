package com.unnamedsoftware.russesamfunnet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.KnotListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class UserProfile extends AppCompatActivity
{
    JSONObject user = null;
    String url = null;
    String completedURL = null;
    RussEntity russ;

    private KnotListAdapter knotListAdapter;
    private JSONArray jsonArray = null;
    private RecyclerView recyclerView;
    private List<KnotEntity> knotEntities = new ArrayList<>();

    private CircularImageView userImage;
    private ImageView russCard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.russCard = findViewById(R.id.russCard);
        this.russCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //userRussCardClicked(view);
            }
        });

        this.userImage = findViewById(R.id.userProfilePicture);
        this.userImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userProfilePictureClicked(view);
            }
        });
        userImage.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                userProfilePicturePressed(view);
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try
        {
            Long russId = bundle.getLong("russ_entity");
            if (russId != 0)
            {
                if (AccessToken.getCurrentAccessToken() != null)
                {
                    url = getString(R.string.url)
                            + "getOtherRuss?accessToken=" + AccessToken.getCurrentAccessToken().getToken()
                            + "&type=facebook"
                            + "&russId=" + russId;
                } else if (((Global) this.getApplication()).getAccessToken() != null)
                {
                    url = getString(R.string.url)
                            + "getOtherRuss?accessToken=" + ((Global) this.getApplication()).getAccessToken()
                            + "&type=russesamfunnet"
                            + "&russId=" + russId;
                }
            } else if (AccessToken.getCurrentAccessToken() != null)
            {
                System.out.println(AccessToken.getCurrentAccessToken().getToken());
                url = (getString(R.string.url) + "userRuss?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
            } else if (((Global) this.getApplication()).getAccessToken() != null)
            {
                System.out.println("User id: ");
                System.out.println(((Global) this.getApplication()).getAccessToken());
                url = getString(R.string.url) + "userRuss?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
            }
            System.out.println(url);
        } catch (Exception e)
        {
            if (AccessToken.getCurrentAccessToken() != null)
            {
                System.out.println(AccessToken.getCurrentAccessToken().getToken());
                url = (getString(R.string.url) + "userRuss?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
            } else if (((Global) this.getApplication()).getAccessToken() != null)
            {
                System.out.println("User id: ");
                System.out.println(((Global) this.getApplication()).getAccessToken());
                url = getString(R.string.url) + "userRuss?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
            }
        }

        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            completedURL = (getString(R.string.url) + "completedKnots?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        } else
        {
            completedURL = getString(R.string.url) + "completedKnots?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }

        try
        {
            getUserRuss();
            getCompletedKnots();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        this.recyclerView = findViewById(R.id.recycler_view_user_knot_list);
        this.knotListAdapter = new KnotListAdapter(knotEntities);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(knotListAdapter);
    }


    /**
     * Enlarges and rotates the russ card when clicked.
     */
    private void userRussCardClicked(View view)
    {
        System.out.println("I've been pressed");
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.user_russ_card_clicked_dialog);
        ImageView userRussCardEnlarged = dialog.findViewById(R.id.userRussCardEnlarged);
        try
        {
            userRussCardEnlarged.setImageResource(R.drawable.russ_card);
            userRussCardEnlarged.setRotation(270);

        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        userRussCardEnlarged.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Enlarges the user profile picture.
     */
    private void userProfilePictureClicked(View view)
    {
        System.out.println("I've been pressed");
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.user_profile_picture_clicked_dialog);
        ImageView userProfileEnlarged = dialog.findViewById(R.id.userProfilePictureEnlarged);
        try
        {
            userProfileEnlarged.setImageResource(R.drawable.default_user);

        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        userProfileEnlarged.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * Displays an dialog with options for changing the image (through camera or gallery)
     */
    private void userProfilePicturePressed(View view)
    {
        startActivity(new Intent(UserProfile.this, CropImage.class));
    }



    /**
     * Retrieves with the JSONParser, the russ information a list over knots that the russ have completed
     */
    private void getCompletedKnots() throws IOException
    {
        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray)
                {
                    fillCompletedKnotList(jsonArray);
                }
            }).execute(new URL(completedURL));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Files an array list with russ information from a jsonArray
     *
     * @param givenJsonArray
     */
    private void fillCompletedKnotList(JSONArray givenJsonArray)
    {
        try
        {
            this.jsonArray = givenJsonArray;

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject knotsJSONObject = jsonArray.getJSONObject(i);
                Long knotID = knotsJSONObject.getLong("knotId");
                String title = knotsJSONObject.getString("knotName");
                String description = knotsJSONObject.getString("knotDetails");

                KnotEntity knot = new KnotEntity(knotID, title, description);
                knotEntities.add(knot);
            }
            this.knotListAdapter.notifyDataSetChanged();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the russ information
     *
     * @throws IOException
     */
    private void getUserRuss() throws IOException
    {
        try
        {
            System.out.println(url);
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {
                    fillProfile(jsonObject);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Files an array list with russ information from a jsonObject
     *
     * @param jsonObject
     */
    public void fillProfile(JSONObject jsonObject)
    {
        try
        {
            user = jsonObject;

            Long russId = Long.valueOf(user.getString("russId"));
            String russStatus = user.getString("russStatus");
            String firstName = user.getString("firstName");
            String lastName = user.getString("lastName");
            String email = user.getString("email");
            String russPassword = user.getString("russPassword");
            String russRole = user.getString("russRole");
            int russYear = Integer.valueOf(user.getString("russYear"));

            russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear);

            getSupportActionBar().setTitle(russ.getFirstName() + " " + russ.getLastName());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
