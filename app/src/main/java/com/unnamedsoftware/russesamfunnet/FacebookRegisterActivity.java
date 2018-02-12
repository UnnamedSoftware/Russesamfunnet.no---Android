package com.unnamedsoftware.russesamfunnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class FacebookRegisterActivity extends AppCompatActivity {

    Spinner day;
    Spinner month;
    Spinner year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_register);

        day = findViewById(R.id.dayPicker);
        month = findViewById(R.id.monthPicker);
        year = findViewById(R.id.yearPicker);

        final ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.dayOptions, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(dayAdapter);
     //   day.setOnItemSelectedListener(this);

        final ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.monthOptions, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(monthAdapter);
     //   month.setOnItemSelectedListener(this);

        final ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.yearOptions, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearAdapter);
     //   year.setOnItemSelectedListener(this);
    }

}
