package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.Search.SearchAddUserToGroupAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 12.04.2018.
 */
public class AddUserToGroup extends AppCompatActivity
{

private Long groupID;
    private List<RussEntity> russEntityList;
    private int view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dialog);
        russEntityList = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.groupID = (Long) bundle.get("groupID");

        final EditText editText = findViewById(R.id.search_input);
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                getUsers(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

        });
        getUsers("");
    }

    public void getUsers(String parameter)
    {
        String url;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            url = (getString(R.string.url)
                    + "searchForRussByNameOutsideSchool?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=facebook"
                    + "&parameter=" + parameter;
        } else
        {
            url = (getString(R.string.url)
                    + "searchForRussByNameOutsideSchool?accessToken=" + ((Global) this.getApplication()).getAccessToken())
                    + "&type=russesamfunnet"
                    + "&parameter=" + parameter;
        }
        System.out.println(url);
        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray)
                {
                    try
                    {
                        fillUserListSuggestions(jsonArray);

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

    private void buildList()
    {
        SearchAddUserToGroupAdapter searchAddUserToGroupAdapter;

        RecyclerView recyclerView;
        if (!russEntityList.isEmpty())
        {
            searchAddUserToGroupAdapter = new SearchAddUserToGroupAdapter(russEntityList,groupID,((Global) this.getApplication()).getAccessToken());
            recyclerView = findViewById(R.id.saResults);
            if (recyclerView == null)
            {
                System.out.println("null");
            }
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


            recyclerView.setAdapter(searchAddUserToGroupAdapter);
        }


    }

    private void fillUserListSuggestions(JSONArray jsonArray)
    {
        List<RussEntity> userSuggestions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            try
            {

                JSONObject newRussObject = jsonArray.getJSONObject(i);

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
                if (!russ.getRussId().equals(((Global) this.getApplication()).getRussId()))
                {
                    userSuggestions.add(russ);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        russEntityList.clear();
        russEntityList = userSuggestions;
        buildList();
    }
}
