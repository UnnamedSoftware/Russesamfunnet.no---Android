package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.FeedPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class UserProfile extends AppCompatActivity
{
    JSONArray user = null;
    String url = null;
    RussEntity russ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Russesamfunnet - Bruker profil");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            url = getString(R.string.url) + "userRuss?russId=" + bundle.getInt("russ_entity");
        }else
        {
            System.out.println("User id: ");
            System.out.println(((MyApplication) this.getApplication()).getRussId());
            url = getString(R.string.url) + "userRuss?russId=" + ((MyApplication) this.getApplication()).getRussId();
        }

        try {
            getUserRuss();
        }catch (Exception e)
        {
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

    public void fillProfile(JSONArray jsonArray)
    {
        try
        {
            user = jsonArray;

            for(int i = 0; i < user.length(); i++)
            {
                JSONObject u = user.getJSONObject(i);

                int russId = Integer.valueOf(u.getString("russId"));
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

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
