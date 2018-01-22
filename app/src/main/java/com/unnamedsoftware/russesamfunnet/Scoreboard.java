package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Scoreboard extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        android.app.ActionBar backAction = getActionBar();
        backAction.setDisplayHomeAsUpEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), Feed.class);
        startActivityForResult(intent, 0);
        return true;

    }
}
