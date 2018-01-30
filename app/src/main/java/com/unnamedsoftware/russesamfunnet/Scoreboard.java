package com.unnamedsoftware.russesamfunnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.unnamedsoftware.russesamfunnet.RecyclerView.ListUser;
import com.unnamedsoftware.russesamfunnet.RecyclerView.RecyclerViewScoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Scoreboard extends AppCompatActivity
{
    private List<ListUser> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewScoreboard recyclerViewScoreboard;

    private String url = getString(R.string.url) + "scoreboardTop10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet - Scoreboard");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewScoreboard = new RecyclerViewScoreboard(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewScoreboard);

        //Dummy data
        dummy();

    }

    private void dummy()
    {
        ListUser user = new ListUser("Ken Netland", 1);
        userList.add(user);

        user = new ListUser("Julia Aaby", 2);
        userList.add(user);

        user = new ListUser("Gustav Buras", 3);
        userList.add(user);

        user = new ListUser("Svenn Valen", 4);
        userList.add(user);

        user = new ListUser("Halvard Roisum", 5);
        userList.add(user);

        user = new ListUser("Jostein Kringen", 6);
        userList.add(user);

        user = new ListUser("Inga Estrem", 7);
        userList.add(user);

        user = new ListUser("Margrethe Svenningsen", 8);
        userList.add(user);

        user = new ListUser("Torbjørg Odegaard", 9);
        userList.add(user);

        user = new ListUser("Karoline Handal", 10);
        userList.add(user);

        user = new ListUser("Girts Strazdins ", 19);
        userList.add(user);

        recyclerViewScoreboard.notifyDataSetChanged();
    }











    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
