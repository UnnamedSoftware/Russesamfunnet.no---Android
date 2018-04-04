package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;

import org.json.JSONArray;
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
        System.out.println(knotEntity.getKnotId());
        this.fillInData();
        checkIfCompleted();

        this.completeFloatingActionButton = findViewById(R.id.complete_button);
        completeFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if knot is completed
                if (knotCompleted == false) {
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
                    }else {
                        String url = (getString(R.string.url)
                                + "registerCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
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
                    if (AccessToken.getCurrentAccessToken() != null)
                    {
                        String url = (getString(R.string.url)
                                + "unRegisterCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                                + "&type=facebook"
                                + "&knotId=" + knotEntity.getKnotId();
                        completeKnot(url);
                    }else {
                        String url = (getString(R.string.url)
                                + "unRegisterCompletedKnot?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                                + "&type=russesamfunnet"
                                + "&knotId=" + knotEntity.getKnotId();
                        completeKnot(url);
                    }
                }

            }
        });


    }

    public void checkIfCompleted() {
        String url = "";
        if (AccessToken.getCurrentAccessToken() != null)
        {
            url = (getString(R.string.url)
                    + "checkIfCompleted?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=facebook"
                    + "&knotId=" + knotEntity.getKnotId();
            completeKnot(url);
        }else {
            url = (getString(R.string.url)
                    + "checkIfCompleted?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=russesamfunnet"
                    + "&knotId=" + knotEntity.getKnotId();
            completeKnot(url);
        }
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    try {
                        if(jsonObject.getString("response").equals("true"))
                        {
                            completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotCompleted)));
                            completeFloatingActionButton.setImageResource(R.drawable.ic_check_black_48dp);
                            setKnotCompleted(true);
                        } else{
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void setKnotCompleted(Boolean bol)
    {
        knotCompleted = bol;
    }

    public void getWitnesses(String url) {
        System.out.println(url);
        try {
            new JSONParser(new JSONParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONArray jsonArray) {
                    try {
                        fillWitnessListSuggestions(jsonArray);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void fillWitnessListSuggestions(JSONArray jsonArray) {
        List<RussEntity> witnessSuggestions = new ArrayList<RussEntity>();
        for(int i = 0; i <= jsonArray.length() ; i++)
        {
            try {

                JSONObject u = jsonArray.getJSONObject(i);
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
                RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear);
                witnessSuggestions.add(russ);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

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
                    } catch (Exception e)
                    {
                        try {
                            System.out.println(jsonObject.getString("response"));
                        } catch (Exception em)
                        {
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