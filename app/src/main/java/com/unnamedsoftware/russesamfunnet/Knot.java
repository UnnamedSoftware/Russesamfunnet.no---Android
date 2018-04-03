package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;

/**
 *
 * Created by Alexander Eilert Berg on 29.01.2018.
 */

public class Knot extends AppCompatActivity
{
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
        }catch (Exception e)
        {
            System.out.println(e.fillInStackTrace());
        }

        Intent i = getIntent();
        knotEntity =(KnotEntity) i.getSerializableExtra("knot_entity");
        System.out.println(knotEntity.getKnotId());
        this.fillInData();

        this.completeFloatingActionButton = findViewById(R.id.complete_button);
        completeFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Check if knot is completed
                if (knotCompleted == false)
                {
                    completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotCompleted)));
                    completeFloatingActionButton.setImageResource(R.drawable.ic_check_black_48dp);
                    knotCompleted = true;
                } else if (knotCompleted == true)
                {
                    completeFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorKnotNotCompleted)));
                    completeFloatingActionButton.setImageResource(R.drawable.ic_close_black_48dp);
                    knotCompleted = false;
                }

            }
        });


    }

    private void fillInData(){

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