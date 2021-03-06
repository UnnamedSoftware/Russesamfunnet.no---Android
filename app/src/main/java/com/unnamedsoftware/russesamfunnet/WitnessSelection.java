package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.unnamedsoftware.russesamfunnet.Entity.FeedEntity;
import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.Search.SearchWitnessAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WitnessSelection extends AppCompatActivity
{


    private List<RussEntity> russEntityList;
    SearchWitnessAdapter searchWitnessAdapter;
    private KnotEntity knotEntity;
    private HashMap<String, Bitmap> images = new HashMap<>();
    private HashMap<RussEntity, Bitmap> russMap = new HashMap<>();
    private int view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dialog);
        russEntityList = new ArrayList<>();
        Intent intent = getIntent();
        knotEntity = (KnotEntity) intent.getSerializableExtra("knotEntity");
        System.out.println(knotEntity.getKnotId());


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
                getWitnesses(editText.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });
        getWitnesses("");
    }

    private void buildList()
    {
        searchWitnessAdapter = new SearchWitnessAdapter(russEntityList, knotEntity, russMap);

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.saResults);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(searchWitnessAdapter);
    }

    public void getWitnesses(String parameter)
    {
        String url;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            url = (getString(R.string.url)
                    + "searchForRussByName?accessToken=" + AccessToken.getCurrentAccessToken().getToken())
                    + "&type=facebook"
                    + "&parameter=" + parameter;
        } else
        {
            url = (getString(R.string.url)
                    + "searchForRussByName?accessToken=" + ((Global) this.getApplication()).getAccessToken())
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
                        fillWitnessListSuggestions(jsonArray);

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


    private void fillWitnessListSuggestions(JSONArray jsonArray)
    {
        russEntityList.clear();
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
                RussEntity russ = new RussEntity(russId, russStatus, firstName, lastName, email, russPassword, russRole, russYear,profilePicture,russCard);
                if (!russ.getRussId().equals(((Global) this.getApplication()).getRussId()))
                {
                    russEntityList.add(russ);
                    searchWitnessAdapter.notifyDataSetChanged();
                    setProfilePicture(russ);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        buildList();

    }

    private void setProfilePicture(final RussEntity russEntity)
    {
        final String url = russEntity.getProfilePicture();
        System.out.println(url);
        String userImageURI = "http://158.38.101.162:8080/files/" + url;

        if (!url.equals("null")) {
            if(images.containsKey(url))
            {
                russMap.put(russEntity, images.get(url));
                searchWitnessAdapter.notifyDataSetChanged();
            }
            ((Global) this.getApplication()).getImageLoader().loadImage(userImageURI, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    System.out.println("TRUE");
                    images.put(url, loadedImage);
                    russMap.put(russEntity, loadedImage);
                    searchWitnessAdapter.notifyDataSetChanged();
                }
            });
        } else {
            images.put(url,null);
            russMap.put(russEntity, null);
            searchWitnessAdapter.notifyDataSetChanged();
        }


    }
}
