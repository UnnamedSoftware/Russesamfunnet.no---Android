package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Alexander Eilert Berg on 25.01.2018.
 */

public class Settings extends AppCompatActivity
{

    private Button button;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Russesamfunnet - Innstillinger");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Swipe func.
        LinearLayout linearLayout = findViewById(R.id.SettingsLayout);
        linearLayout.setOnTouchListener(new OnSwipeTouchListener(this)
        {
            public void onSwipeRight()
            {
                onBackPressed();
            }
        });

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}