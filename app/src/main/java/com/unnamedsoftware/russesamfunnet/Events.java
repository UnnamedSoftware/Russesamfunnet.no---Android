package com.unnamedsoftware.russesamfunnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.unnamedsoftware.russesamfunnet.Entity.EventEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.EventAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 15.03.2018.
 */

public class Events extends AppCompatActivity
{
    private List<EventEntity> eventEntityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.event));

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dummy();

        this.recyclerView = findViewById(R.id.eaEvents);
        this.eventAdapter = new EventAdapter(eventEntityList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(eventAdapter);

    }


    private void dummy()
    {
        EventEntity event = new EventEntity("Event 1", 1, "Aenean elementum, lectus consequat congue tincidunt, nibh quam venenatis enim, volutpat mollis quam felis quis enim. Curabitur et ante et lorem dapibus euismod. Praesent eget facilisis ante. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam lectus felis, tempor ac volutpat in, aliquet et odio. Integer imperdiet dui egestas placerat volutpat. Cras sed maximus dui.");
        eventEntityList.add(event);

        event = new EventEntity("Event 2", 2,"Quisque mollis ultrices mattis. Sed nec ex tellus. Vivamus malesuada aliquet pretium. Nam mauris orci, luctus eget mollis at, dapibus quis orci. Quisque dictum turpis a lacus fringilla, vel dapibus justo tempus. Integer vel libero ac mauris lobortis tincidunt ac ac libero. Suspendisse dapibus cursus tristique.");
        eventEntityList.add(event);

        event = new EventEntity("Event 3", 3, "Proin et cursus metus. In mollis enim dolor, vel fermentum odio egestas a. Proin maximus pharetra elit, quis condimentum dui molestie ut. Nunc ac ipsum a justo aliquam fermentum. Aliquam bibendum tortor id eros dictum varius. In eu est et eros interdum elementum. Curabitur vehicula dui quis hendrerit ornare. Curabitur elementum quis nulla ut viverra. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.");
        eventEntityList.add(event);

        event = new EventEntity("Event 4", 4, "Nunc malesuada ligula eget est cursus accumsan. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse potenti. Vivamus sed lorem ornare, pulvinar est sed, congue eros. Nullam auctor nec metus in malesuada. Mauris volutpat leo vitae ante aliquam porta. Maecenas non erat viverra, ornare libero nec, ullamcorper nulla. Vestibulum pretium enim quis efficitur sagittis. Nullam quis ipsum in erat posuere blandit. Donec pretium lectus erat, ut blandit urna lacinia non.");
        eventEntityList.add(event);

        event = new EventEntity("Event 5", 5, "Curabitur luctus vulputate purus, et varius elit pretium a. Aliquam id malesuada libero. Etiam vestibulum vestibulum libero. Phasellus sit amet elit bibendum, tristique libero in, commodo arcu. Quisque orci ipsum, ultricies id diam id, laoreet aliquam tellus. Nullam iaculis non ex nec volutpat. Vivamus efficitur elit varius, consequat velit ac, molestie elit. Fusce eget accumsan lorem, eu tincidunt dolor. Nam ut molestie nunc, eu luctus orci. Ut aliquam eget nibh a scelerisque. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vestibulum molestie magna ut eleifend faucibus. Vestibulum bibendum nisi eu diam bibendum, ac porttitor ipsum dignissim. In tellus leo, malesuada id metus eget, sollicitudin lobortis libero. Quisque blandit malesuada mollis. Praesent felis enim, suscipit nec vulputate in, congue quis tortor.");
        eventEntityList.add(event);
    }
}
