package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
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
 * Created by Alexander Eilert Berg on 29.01.2018.
 */

public class KnotList extends AppCompatActivity
{

    private List<KnotEntity> knotEntities = new ArrayList<>();
    private RecyclerView recyclerView;
    private KnotListAdapter knotListAdapter;
    private JSONArray jsonArray = null;

    private String url;

    // JSON Node names
    private static final String TAG_KNOTLIST = "knotList";
    private static final String TAG_TITLE = "title";
    private static final String TAG_KNOTID = "knotID";

    JSONArray knots = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knot_list);
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            url = (getString(R.string.url) + "getKnotsList?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook");
        }else {
            url = getString(R.string.url) + "getKnotsList?accessToken=" + ((Global) this.getApplication()).getAccessToken() + "&type=russesamfunnet";
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet - Knute liste");

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        this.recyclerView = findViewById(R.id.recycler_view_knot_list);
        this.knotListAdapter = new KnotListAdapter(knotEntities);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(knotListAdapter);


        try
        {
            getKnotList();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

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
     * Uses the JSONParser to request the knot list from the server.
     */
    private void getKnotList() throws IOException {
        try {
            new JSONParser(new JSONParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONArray jsonArray) {
                    fillKnotList(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

        public void fillKnotList(JSONArray jsonArray)
    {
        try
        {
            knots = jsonArray;

            for(int i = 0; i < knots.length(); i++)
            {
                JSONObject knotsJSONObject = knots.getJSONObject(i);
                Long knotID = Long.valueOf(knotsJSONObject.getString("knotId"));
                String title = knotsJSONObject.getString("knotName");
                String description = knotsJSONObject.getString("knotDetails");

                KnotEntity knot = new KnotEntity(knotID,title,description);
                knotEntities.add(knot);
            }
            this.knotListAdapter.notifyDataSetChanged();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }catch (JSONException e)
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
