package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unnamedsoftware.russesamfunnet.Entity.CompletedKnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;

import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */

public class Knot extends AppCompatActivity
{
    private KnotEntity knotEntity;
    private FloatingActionButton completeFloatingActionButton;
    private RussEntity witness;
    private CompletedKnotEntity completed;

    private CircularImageView witnessCircularImageView1;
    private CircularImageView witnessCircularImageView2;

    private Boolean knotCompleted = false;


    private File userImageFile = new File("/storage/emulated/0/Android/data/com.unnamedsoftware.russesamfunnet/files/Pictures/russesamfunnetProfilePicture.jpg");

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knot);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet");
        completed = new CompletedKnotEntity();
        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e)
        {
            System.out.println(e.fillInStackTrace());
        }

        Intent i = getIntent();
        knotEntity = (KnotEntity) i.getSerializableExtra("knot_entity");
        witness = (RussEntity) i.getSerializableExtra("witness");
        if (witness != null)
        {
            if (AccessToken.getCurrentAccessToken() != null)
            {
                String url = (getString(R.string.url)
                        + "addWitness?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                        + "&type=facebook"
                        + "&knotId=" + knotEntity.getKnotId()
                        + "&witnessId=" + witness.getRussId();
                setWitness(url);
            } else
            {
                String url = (getString(R.string.url)
                        + "addWitness?accessToken=" + ((Global) getApplication()).getAccessToken())
                        + "&type=russesamfunnet"
                        + "&knotId=" + knotEntity.getKnotId()
                        + "&witnessId=" + witness.getRussId();
                setWitness(url);
            }
        }

        checkIfCompleted();
        knotEntity.getKnotId();
        this.fillInData();

        this.completeFloatingActionButton = findViewById(R.id.complete_button);
        completeFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Check if knot is completed
                if (knotCompleted == false)
                {
                    completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotCompleted)));
                    completeFloatingActionButton.setImageResource(R.drawable.ic_check_black_48dp);
                    knotCompleted = true;
                    if (AccessToken.getCurrentAccessToken() != null)
                    {
                        String url = (getString(R.string.url)
                                + "registerCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                                + "&type=facebook"
                                + "&knotId=" + knotEntity.getKnotId()
                                + "&witness1=0"
                                + "&witness2=0";
                        completeKnot(url);
                        getCompletedKnot();
                    } else
                    {
                        String url = (getString(R.string.url)
                                + "registerCompletedKnot?accessToken=" + ((Global) getApplication()).getAccessToken())
                                + "&type=russesamfunnet"
                                + "&knotId=" + knotEntity.getKnotId()
                                + "&witness1=0"
                                + "&witness2=0";
                        completeKnot(url);
                    }
                } else if (knotCompleted == true)
                {
                    completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotNotCompleted)));
                    completeFloatingActionButton.setImageResource(R.drawable.ic_close_black_48dp);
                    knotCompleted = false;
                    if (AccessToken.getCurrentAccessToken() != null)
                    {
                        String url = (getString(R.string.url)
                                + "unRegisterCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                                + "&type=facebook"
                                + "&knotId=" + knotEntity.getKnotId();
                        completeKnot(url);
                    } else
                    {
                        String url = (getString(R.string.url)
                                + "unRegisterCompletedKnot?accessToken=" + ((Global) getApplication()).getAccessToken())
                                + "&type=russesamfunnet"
                                + "&knotId=" + knotEntity.getKnotId();
                        completeKnot(url);
                    }
                    witnessCircularImageView1.setImageResource(R.drawable.ic_add_white_48dp);
                    witnessCircularImageView2.setImageResource(R.drawable.ic_add_white_48dp);
                    TextView witnessName = findViewById(R.id.witness1Name);
                    TextView witnessName2 = findViewById(R.id.witness2Name);
                    witnessName.setText("");
                    witnessName2.setText("");
                    completed = null;

                }

            }
        });
        try
        {
            if (completed != null)
            {
                System.out.println("Success");
            }
            System.out.println("-----------------------------------------------------------------------------------------------ID: " + completed.getWitnessId1().getRussId());
            System.out.println("-----------------------------------------------------------------------------------------------ID: " + completed.getWitnessId2().getRussId());

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        this.witnessCircularImageView1 = findViewById(R.id.add_witness_button1);
        witnessCircularImageView1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Knot.this, WitnessSelection.class);
                intent.putExtra("knotEntity", knotEntity);
                System.out.println(knotEntity.getKnotId());
                startActivity(intent);
            }
        });
        this.witnessCircularImageView2 = findViewById(R.id.add_witness_button2);
        witnessCircularImageView2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Knot.this, WitnessSelection.class);
                intent.putExtra("knotEntity", knotEntity);
                startActivity(intent);
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


    public void setWitness(String url)
    {
        try
        {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {
                    if (jsonObject != null)
                    {
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public void getCompletedKnot()
    {
        String url;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            url = (getString(R.string.url)
                    + "getCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=facebook"
                    + "&knotId=" + knotEntity.getKnotId();

        } else
        {
            url = (getString(R.string.url)
                    + "getCompletedKnot?accessToken=" + ((Global) getApplication()).getAccessToken())
                    + "&type=russesamfunnet"
                    + "&knotId=" + knotEntity.getKnotId();
        }
        System.out.println(url);
        try
        {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {
                    if (jsonObject != null)
                    {
                        System.out.println("Fill completed data");
                        fillCompletedData(jsonObject);
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public RussEntity getRussEntityFromJson(JSONObject jsonObject)
    {
        try
        {
            Long russId = jsonObject.getLong("russId");
            String russStatus = jsonObject.getString("russStatus");
            String firstName = jsonObject.getString("firstName");
            String lastName = jsonObject.getString("lastName");
            String email = jsonObject.getString("email");
            String russPassword = jsonObject.getString("russPassword");
            String russRole = jsonObject.getString("russRole");
            int russYear = Integer.valueOf(jsonObject.getString("russYear"));

            return new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public KnotEntity getKnotEntityFromJson(JSONObject jsonObject)
    {
        try
        {
            Long knotID = jsonObject.getLong("knotId");
            String title = jsonObject.getString("knotName");
            String description = jsonObject.getString("knotDetails");

            return new KnotEntity(knotID, title, description);

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void fillCompletedData(JSONObject jsonObject)
    {
        try
        {
            JSONObject russObj = (JSONObject) jsonObject.get("russId");
            RussEntity russ = getRussEntityFromJson(russObj);
            JSONObject knotObj = (JSONObject) jsonObject.get("knotId");
            KnotEntity knot = getKnotEntityFromJson(knotObj);
            Long completedId = jsonObject.getLong("completedId");
            try
            {
                JSONObject witness1Obj = (JSONObject) jsonObject.get("witnessId1");
                RussEntity witness1 = getRussEntityFromJson(witness1Obj);
                completed.setWitnessId1(witness1);
                System.out.println(witness1.getRussId());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                JSONObject witness2Obj = (JSONObject) jsonObject.get("witnessId2");
                RussEntity witness2 = getRussEntityFromJson(witness2Obj);
                completed.setWitnessId2(witness2);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            completed.setCompletedId(completedId);
            completed.setKnotId(knot);
            completed.setRussId(russ);

            System.out.println("-----------------------------------------------------------------------------------------------ID: " + completed.getWitnessId1().getRussId());

            System.out.println("************************************************** " + completed.getWitnessId2().getRussId());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        updateWitnessPhoto();

    }

    private void removeWitness(Long witnessId)
    {
        String removeUrl = "";
        if (AccessToken.getCurrentAccessToken() != null)
        {
            removeUrl = (getString(R.string.url)
                    + "removeWitness?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=facebook"
                    + "&knotId=" + knotEntity.getKnotId()
                    + "&witnessId=" + witnessId;

        } else
        {
            removeUrl = (getString(R.string.url)
                    + "removeWitness?accessToken=" + ((Global) getApplication()).getAccessToken())
                    + "&type=russesamfunnet"
                    + "&knotId=" + knotEntity.getKnotId()
                    + "&witnessId=" + witnessId;
        }
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
                            System.out.println(jsonObject.getString("response"));
                            getCompletedKnot();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }).execute(new URL(removeUrl));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    private void updateWitnessPhoto()
    {
        try
        {
            if (completed.getWitnessId1().getRussId() != null)
            {
                System.out.println("************************************************** " + completed.getWitnessId1().getRussId());
                boolean hasImageOnServer = false;

                TextView witnessName = findViewById(R.id.witness1Name);
                witnessName.setText(completed.getWitnessId1().getSearchName());

                if (!userImageFile.exists())
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(userImageFile.getAbsolutePath());
                    this.witnessCircularImageView1.setImageBitmap(bitmap);
                } else if (hasImageOnServer)
                {
                    new LoadImage(this, witnessCircularImageView1).execute("http://russesamfunnet.no/logos/logo.png");
                } else
                {
                    this.witnessCircularImageView1.setImageResource(R.drawable.default_user);
                }
            }

            if (completed.getWitnessId2().getRussId() != null)
            {
                System.out.println("************************************************** " + completed.getWitnessId2().getRussId());
                boolean hasImageOnServer = true;
                TextView witnessName = findViewById(R.id.witness2Name);
                witnessName.setText(completed.getWitnessId2().getSearchName());


                if (!userImageFile.exists())
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(userImageFile.getAbsolutePath());
                    this.witnessCircularImageView2.setImageBitmap(bitmap);
                } else if (hasImageOnServer)
                {
                    new LoadImage(this, witnessCircularImageView2).execute("http://russesamfunnet.no/logos/logo.png");
                } else
                {
                    this.witnessCircularImageView2.setImageResource(R.drawable.default_user);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void checkIfCompleted()
    {
        String url = "";
        if (AccessToken.getCurrentAccessToken() != null)
        {
            url = (getString(R.string.url)
                    + "checkIfCompleted?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=facebook"
                    + "&knotId=" + knotEntity.getKnotId();
        } else
        {
            url = (getString(R.string.url)
                    + "checkIfCompleted?accessToken=" + ((Global) this.getApplication()).getAccessToken())
                    + "&type=russesamfunnet"
                    + "&knotId=" + knotEntity.getKnotId();
            completeKnot(url);
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
                        if (jsonObject.getString("response").equals("true"))
                        {
                            completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotCompleted)));
                            completeFloatingActionButton.setImageResource(R.drawable.ic_check_black_48dp);
                            setKnotCompleted(true);
                            getCompletedKnot();
                        } else
                        {
                            completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotNotCompleted)));
                            completeFloatingActionButton.setImageResource(R.drawable.ic_close_black_48dp);
                            System.out.println("No");
                            setKnotCompleted(false);
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

    public void setKnotCompleted(Boolean bol)
    {
        knotCompleted = bol;
    }


    /**
     * Displays the dialog for searching
     */
    public void completeKnot(String url)
    {
        System.out.println(url);
        try
        {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {
                    try
                    {
                        JSONObject knot = jsonObject.getJSONObject("knotId");
                        JSONObject russ = jsonObject.getJSONObject("russId");
                        System.out.println(russ.getString("firstName") + " completed " + knot.getString("knotName"));
                    } catch (Exception e)
                    {
                        try
                        {
                            System.out.println(jsonObject.getString("response"));
                        } catch (Exception em)
                        {
                            em.printStackTrace();
                        }
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

    }

    private void fillInData()
    {

        TextView textViewTitle = findViewById(R.id.knot_name);
        textViewTitle.setText(knotEntity.getDetails());

        TextView textViewDescription = findViewById(R.id.description);
        textViewDescription.setText(knotEntity.getTitle());
    }


    @Override
    public boolean onSupportNavigateUp()
    {
        //onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
        Knot.this.finish();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Knot.this, KnotList.class);
        Knot.this.finish();
        startActivity(intent);
    }

}