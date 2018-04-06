package com.unnamedsoftware.russesamfunnet;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.CompletedKnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.Search.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */

public class Knot extends AppCompatActivity {
    private KnotEntity knotEntity;
    private FloatingActionButton completeFloatingActionButton;
    private FloatingActionButton witnessFloatingActionButton1;
    private FloatingActionButton witnessFloatingActionButton2;
    private RussEntity witness;
    private CompletedKnotEntity completed;


    private Boolean knotCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knot);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet");

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }

        Intent i = getIntent();
        knotEntity = (KnotEntity) i.getSerializableExtra("knot_entity");
        witness = (RussEntity) i.getSerializableExtra("witness");
        if (witness != null) {
            if (AccessToken.getCurrentAccessToken() != null) {
                String url = (getString(R.string.url)
                        + "addWitness?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                        + "&type=facebook"
                        + "&knotId=" + knotEntity.getKnotId()
                        + "&witnessId=" + witness.getRussId();
                setWitness(url);
            } else {
                String url = (getString(R.string.url)
                        + "addWitness?accessToken=" + ((Global) getApplication()).getAccessToken())
                        + "&type=russesamfunnet"
                        + "&knotId=" + knotEntity.getKnotId()
                        + "&witnessId=" + witness.getRussId();
                setWitness(url);
            }
        }

        checkIfCompleted();
        System.out.println(knotEntity.getKnotId());
        this.fillInData();
        this.witnessFloatingActionButton1 = findViewById(R.id.add_witness_button1);
        witnessFloatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Knot.this, WitnessSelection.class);
                intent.putExtra("knotEntity", knotEntity);
                System.out.println(knotEntity.getKnotId());
                startActivity(intent);
            }
        });
        this.witnessFloatingActionButton2 = findViewById(R.id.add_witness_button2);
        witnessFloatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Knot.this, WitnessSelection.class);
                intent.putExtra("knotEntity", knotEntity);
                startActivity(intent);
            }
        });
        this.completeFloatingActionButton = findViewById(R.id.complete_button);
        completeFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if knot is completed
                if (knotCompleted == false) {
                    completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotCompleted)));
                    completeFloatingActionButton.setImageResource(R.drawable.ic_check_black_48dp);
                    knotCompleted = true;
                    if (AccessToken.getCurrentAccessToken() != null) {
                        String url = (getString(R.string.url)
                                + "registerCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                                + "&type=facebook"
                                + "&knotId=" + knotEntity.getKnotId()
                                + "&witness1=0"
                                + "&witness2=0";
                        completeKnot(url);
                        getCompletedKnot();
                    } else {
                        String url = (getString(R.string.url)
                                + "registerCompletedKnot?accessToken=" + ((Global) getApplication()).getAccessToken())
                                + "&type=russesamfunnet"
                                + "&knotId=" + knotEntity.getKnotId()
                                + "&witness1=0"
                                + "&witness2=0";
                        completeKnot(url);
                    }
                } else if (knotCompleted == true) {
                    completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotNotCompleted)));
                    completeFloatingActionButton.setImageResource(R.drawable.ic_close_black_48dp);
                    knotCompleted = false;
                    if (AccessToken.getCurrentAccessToken() != null) {
                        String url = (getString(R.string.url)
                                + "unRegisterCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                                + "&type=facebook"
                                + "&knotId=" + knotEntity.getKnotId();
                        completeKnot(url);
                    } else {
                        String url = (getString(R.string.url)
                                + "unRegisterCompletedKnot?accessToken=" + ((Global) getApplication()).getAccessToken())
                                + "&type=russesamfunnet"
                                + "&knotId=" + knotEntity.getKnotId();
                        completeKnot(url);
                    }
                }

            }
        });


    }

    public void setWitness(String url) {
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    if (jsonObject != null) {
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void getCompletedKnot() {
        String url;
        if (AccessToken.getCurrentAccessToken() != null) {
            url = (getString(R.string.url)
                    + "getCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=facebook"
                    + "&knotId=" + knotEntity.getKnotId();

        } else {
            url = (getString(R.string.url)
                    + "getCompletedKnot?accessToken=" + ((Global) getApplication()).getAccessToken())
                    + "&type=russesamfunnet"
                    + "&knotId=" + knotEntity.getKnotId();
        }
        System.out.println(url);
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    if (jsonObject != null) {
                        fillCompletedData(jsonObject);
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public RussEntity getRussEntityFromJson(JSONObject jsonObject)
    {
        try {
            Long russId = jsonObject.getLong("russId");
            String russStatus = jsonObject.getString("russStatus");
            String firstName = jsonObject.getString("firstName");
            String lastName = jsonObject.getString("lastName");
            String email = jsonObject.getString("email");
            String russPassword = jsonObject.getString("russPassword");
            String russRole = jsonObject.getString("russRole");
            int russYear = Integer.valueOf(jsonObject.getString("russYear"));

            return new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  null;
        }
    }

    public KnotEntity getKnotEntityFromJson(JSONObject jsonObject)
    {
        try
        {
                Long knotID = jsonObject.getLong("knotId");
                String title = jsonObject.getString("knotName");
                String description = jsonObject.getString("knotDetails");

                return new KnotEntity(knotID,title,description);

        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void fillCompletedData(JSONObject jsonObject) {
        try {
            completed = new CompletedKnotEntity();

            RussEntity russ = getRussEntityFromJson((JSONObject) jsonObject.get("russId"));
            KnotEntity knot = getKnotEntityFromJson((JSONObject) jsonObject.get("knotId"));
            Long completedId = jsonObject.getLong("completedId");
            RussEntity witness1 = getRussEntityFromJson((JSONObject) jsonObject.get("witnessId1"));
            RussEntity witness2 = getRussEntityFromJson((JSONObject) jsonObject.get("witnessId1"));

            completed.setCompletedId(completedId);
            completed.setKnotId(knot);
            completed.setRussId(russ);
            completed.setWitnessId1(witness1);
            completed.setWitnessId2(witness2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkIfCompleted() {
        String url = "";
        if (AccessToken.getCurrentAccessToken() != null) {
            url = (getString(R.string.url)
                    + "checkIfCompleted?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=facebook"
                    + "&knotId=" + knotEntity.getKnotId();
        } else {
            url = (getString(R.string.url)
                    + "checkIfCompleted?accessToken=" + ((Global) this.getApplication()).getAccessToken())
                    + "&type=russesamfunnet"
                    + "&knotId=" + knotEntity.getKnotId();
        }
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    try {
                        if (jsonObject.getString("response").equals("true")) {
                            completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotCompleted)));
                            completeFloatingActionButton.setImageResource(R.drawable.ic_check_black_48dp);
                            setKnotCompleted(true);
                            getCompletedKnot();
                        } else {
                            completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotNotCompleted)));
                            completeFloatingActionButton.setImageResource(R.drawable.ic_close_black_48dp);
                            System.out.println("No");
                            setKnotCompleted(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void setKnotCompleted(Boolean bol) {
        knotCompleted = bol;
    }


    /**
     * Displays the dialog for searching
     */


    public void completeKnot(String url) {
        System.out.println(url);
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    try {
                        JSONObject knot = jsonObject.getJSONObject("knotId");
                        JSONObject russ = jsonObject.getJSONObject("russId");
                        System.out.println(russ.getString("firstName") + " completed " + knot.getString("knotName"));
                    } catch (Exception e) {
                        try {
                            System.out.println(jsonObject.getString("response"));
                        } catch (Exception em) {
                            em.printStackTrace();
                        }
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void fillInData() {

        TextView textViewTitle = findViewById(R.id.knot_name);
        textViewTitle.setText(knotEntity.getDetails());

        TextView textViewDescription = findViewById(R.id.description);
        textViewDescription.setText(knotEntity.getTitle());
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}