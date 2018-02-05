package com.unnamedsoftware.russesamfunnet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Register extends AppCompatActivity
{
    String termsOfService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUser(findViewById(R.id.registerButton));

        InputStream inputStream = this.getResources().openRawResource(R.raw.terms_of_service);
        try
        {
            termsOfService = StreamToString(inputStream);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * The functions associated with the register button
     *
     * @param view
     */
    public void registerUser(View view)
    {
        view.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        /*
                                        if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.userEmail)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.userEmail));
                                            Toast.makeText(Register.this, "Please enter email", Toast.LENGTH_SHORT).show();
                                        } else if (!(InputAnalyzer.isUserEmailValid(findViewById(R.id.emailInput))))
                                        {
                                            Toast.makeText(Register.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                                        } else if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.userPassword)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.userPassword));
                                            Toast.makeText(Register.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                        } else
                                        {
                                            try
                                            {
                                                if(displayTermsOfService())
                                                {
                                                    goToFeed();
                                                }
                                            } catch (IOException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            //Send data to server
                                        }*/
                                        displayTermsOfService();
                                    }
                                }
        );
    }

    /**
     * Displays the terms of service as an AlertDialog.
     * The user has to accept or disagree.
     * If the user accepts the input data gets sent to the server.
     * If the user disagrees the user is sent to the Login screen.
     */
    private void displayTermsOfService()
    {
        final AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }else
        {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Vilkår for Tjenesten")
        .setMessage(termsOfService)
        .setPositiveButton("Avslå", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                startActivity(new Intent(Register.this, Login.class));
            }
        })
        .setNegativeButton("Aksepter", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
    }


    public static String StreamToString(InputStream in) throws IOException
    {
        if(in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }
}