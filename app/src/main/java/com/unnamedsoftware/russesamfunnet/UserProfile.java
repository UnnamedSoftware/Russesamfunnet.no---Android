package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class UserProfile extends AppCompatActivity {
    JSONArray user = null;
    String url = null;
    RussEntity russ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Russesamfunnet - Bruker profil");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            if (AccessToken.getCurrentAccessToken() != null) {
                url = getString(R.string.url)
                        + "getOtherRuss?accessToken=" + AccessToken.getCurrentAccessToken().getToken()
                        +"&type=facebook";
            } else if (((Global) this.getApplication()).getAccessToken() != null) {
            url = getString(R.string.url)
                    + "getOtherRuss?accessToken=" + ((Global) this.getApplication()).getAccessToken()
                    +"&type=russesamfunnet";
            }
            System.out.println(url);
        } catch (Exception e) {
            if (AccessToken.getCurrentAccessToken() != null) {
                System.out.println(AccessToken.getCurrentAccessToken().getToken());
                url = (getString(R.string.url) + "userRuss?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
            } else if (((Global) this.getApplication()).getAccessToken() != null) {
                System.out.println("User id: ");
                System.out.println(((Global) this.getApplication()).getAccessToken());
                url = getString(R.string.url) + "userRuss?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
            }
        }

        try {
            getUserRuss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserRuss() throws IOException {
        try {
            System.out.println(url);
            new JSONParser(new JSONParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONArray jsonArray) {
                    fillProfile(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void fillProfile(JSONArray jsonArray) {
        try {
            user = jsonArray;

            for (int i = 0; i < user.length(); i++) {
                JSONObject u = user.getJSONObject(i);

                Long russId = Long.valueOf(u.getString("russId"));
                String russStatus = u.getString("russStatus");
                String firstName = u.getString("firstName");
                String lastName = u.getString("lastName");
                String email = u.getString("email");
                String russPassword = u.getString("russPassword");
                String russRole = u.getString("russRole");
                int russYear = Integer.valueOf(u.getString("russYear"));

                russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear);

                EditText userName = (EditText) findViewById(R.id.userName);
                userName.setText(russ.getFirstName() + " " + russ.getLastName());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
