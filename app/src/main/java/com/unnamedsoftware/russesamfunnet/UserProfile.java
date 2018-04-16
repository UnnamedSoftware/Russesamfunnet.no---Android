package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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

import java.io.File;
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
    private File userImageFile = new File("/storage/emulated/0/Android/data/com.unnamedsoftware.russesamfunnet/files/Pictures/russesamfunnetProfilePicture.jpg");
    private File russCardFile = new File("/storage/emulated/0/Android/data/com.unnamedsoftware.russesamfunnet/files/Pictures/russesamfunnetRussCard.jpg");

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.toolbar).bringToFront();

        boolean hasImageOnServer = false;
        this.russCard = findViewById(R.id.russCard);

        if (russCardFile.exists())
        {
            Bitmap russCardBitmap = BitmapFactory.decodeFile(russCardFile.getAbsolutePath());
            this.russCard.setImageBitmap(russCardBitmap);
        } else if (hasImageOnServer)
        {
            new LoadImage(this, russCard).execute("http://russesamfunnet.no/logos/logo.png");
        }

        this.russCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userRussCardClicked(view);
            }
        });
        this.russCard.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                userRussCardPressed(view);
                return true;
            }
        });

        this.userImage = findViewById(R.id.userProfilePicture);
        if (userImageFile.exists())
        {
            Bitmap userImageBitmap = BitmapFactory.decodeFile(userImageFile.getAbsolutePath());
            this.userImage.setImageBitmap(userImageBitmap);
        } else if (hasImageOnServer)
        {
            new LoadImage(this, userImage).execute("http://russesamfunnet.no/logos/logo.png");
        }

        this.userImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userProfilePictureClicked(view);
            }
        });
        this.userImage.setOnLongClickListener(new View.OnLongClickListener()
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
            System.out.println(russId);
            if(russId != 0) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    System.out.println(AccessToken.getCurrentAccessToken().getToken());
                    completedURL = (getString(R.string.url) + "completedKnotsOtherRuss?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook&russId=" + russId);
                } else {
                    completedURL = getString(R.string.url) + "completedKnotsOtherRuss?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet&russId=" + russId;
                }
            } else {
                if (AccessToken.getCurrentAccessToken() != null) {
                    System.out.println(AccessToken.getCurrentAccessToken().getToken());
                    completedURL = (getString(R.string.url) + "completedKnots?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
                } else {
                    completedURL = getString(R.string.url) + "completedKnots?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
                }
            }
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


    /**
     * Enlarges and rotates the russ card when clicked.
     */
    private void userRussCardClicked(View view)
    {
        System.out.println("I've been pressed");
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.user_russ_card_clicked_dialog);
        ImageView userProfileEnlarged = dialog.findViewById(R.id.userRussCardEnlarged);
        try
        {
            if (russCardFile.exists())
            {
                System.out.println("Found image");
                Bitmap bitmap = BitmapFactory.decodeFile(russCardFile.getAbsolutePath());
                userProfileEnlarged.setImageBitmap(bitmap);
            } else
            {
                System.out.println("Could not find image");
                userProfileEnlarged.setImageResource(R.drawable.russ_card);
            }
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
            if (userImageFile.exists())
            {
                System.out.println("Found image");
                Bitmap bitmap = BitmapFactory.decodeFile(userImageFile.getAbsolutePath());
                userProfileEnlarged.setImageBitmap(bitmap);
            } else
            {
                System.out.println("Could not find image");
                userProfileEnlarged.setImageResource(R.drawable.default_user);
            }
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
        if (((Global) this.getApplication()).getRussId() == russ.getRussId())
        {
            Intent intent = new Intent(this, CropImage.class);
            startActivityForResult(intent, 1);
        }
    }

    private void userRussCardPressed(View view)
    {
        if (((Global) this.getApplication()).getRussId() == russ.getRussId())
        {
            startActivityForResult(new Intent(this, RussCardCamera.class), 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
                finish();
                startActivity(getIntent());
            }
        }
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
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject knotsJSONObject = jsonObject.getJSONObject("knotId");
                Long knotID = knotsJSONObject.getLong("knotId");
                String title = knotsJSONObject.getString("knotName");
                String description = knotsJSONObject.getString("knotDetails");

                KnotEntity knot = new KnotEntity(knotID, title, description);
                knotEntities.add(knot);
            }
            this.knotListAdapter.notifyDataSetChanged();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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