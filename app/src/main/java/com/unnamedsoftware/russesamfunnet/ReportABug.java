package com.unnamedsoftware.russesamfunnet;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Alexander Eilert Berg on 18.04.2018.
 */
public class ReportABug extends AppCompatActivity
{
    private Long userID = ((Global) this.getApplication()).getRussId();
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_a_bug);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button send = findViewById(R.id.buttonBugSubmit);
        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText editText = findViewById(R.id.reportBugMessage);

                Report report = new Report();
                report.reportBug(editText.getText().toString(),userID,context);
            }
        });


    }

}
