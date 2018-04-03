package com.unnamedsoftware.russesamfunnet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Register extends AppCompatActivity
{
    String termsOfService;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI(findViewById(R.id.registerParent));

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

                                        if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.registerFirstNameInput)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerFirstNameInput));
                                            Toast toast = Toast.makeText(Register.this, "Please enter your first name", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                            InputAnalyzer.clearBorder(findViewById(R.id.registerSurnameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerEmailInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerPasswordInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerConfirmPasswordInput));

                                        } else if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.registerSurnameInput)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerSurnameInput));
                                            Toast toast = Toast.makeText(Register.this, "Please enter your surname", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                            InputAnalyzer.clearBorder(findViewById(R.id.registerFirstNameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerEmailInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerPasswordInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerConfirmPasswordInput));

                                        } else if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.registerEmailInput)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerEmailInput));
                                            Toast toast = Toast.makeText(Register.this, "Please enter an email address", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                            InputAnalyzer.clearBorder(findViewById(R.id.registerFirstNameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerSurnameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerPasswordInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerConfirmPasswordInput));

                                        } else if (!InputAnalyzer.isUserEmailValid(findViewById(R.id.registerEmailInput)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerEmailInput));
                                            Toast toast = Toast.makeText(Register.this, "Please enter a valid email", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                            InputAnalyzer.clearBorder(findViewById(R.id.registerFirstNameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerSurnameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerPasswordInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerConfirmPasswordInput));

                                        } else if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.registerPasswordInput)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerPasswordInput));
                                            Toast toast = Toast.makeText(Register.this, "Please enter a password", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                            InputAnalyzer.clearBorder(findViewById(R.id.registerFirstNameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerSurnameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerEmailInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerConfirmPasswordInput));

                                        } else if (!InputAnalyzer.isPasswordValid(findViewById(R.id.registerPasswordInput)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerPasswordInput));
                                            Toast toast = Toast.makeText(Register.this, "Please enter a valid password, minimum 8 char.", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                            InputAnalyzer.clearBorder(findViewById(R.id.registerFirstNameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerSurnameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerEmailInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerConfirmPasswordInput));

                                        } else if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.registerConfirmPasswordInput)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerConfirmPasswordInput));
                                            Toast toast = Toast.makeText(Register.this, "Please confirm your password", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                            InputAnalyzer.clearBorder(findViewById(R.id.registerFirstNameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerSurnameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerEmailInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerPasswordInput));

                                        } else if (!InputAnalyzer.doesPasswordsMatch(findViewById(R.id.registerPasswordInput), findViewById(R.id.registerConfirmPasswordInput)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerPasswordInput));
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.registerConfirmPasswordInput));
                                            Toast toast = Toast.makeText(Register.this, "Passwords does not match", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();

                                            InputAnalyzer.clearBorder(findViewById(R.id.registerFirstNameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerSurnameInput));
                                            InputAnalyzer.clearBorder(findViewById(R.id.registerEmailInput));

                                        } else
                                        {
                                            displayTermsOfService();
                                        }

                                        EditText email = findViewById(R.id.registerEmailInput);
                                        EditText password = findViewById(R.id.registerPasswordInput);
                                        EditText firstName = findViewById(R.id.registerFirstNameInput);
                                        EditText surname = findViewById(R.id.registerSurnameInput);
                                        //Send data to server
                                        url = (getString(R.string.url) + "russasamfunnetRegister?email=" + email.getText()
                                                                       + "&password=" + password.getText()
                                                                       + "&schoolId=" + 1
                                                                       + "&firstName=" + firstName.getText()
                                                                       + "&lastName=" + surname.getText());
                                        System.out.println(url);
                                        sendData(url);
                                        
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
        } else
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

    public void sendData(String url)
    {
            try {
                new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                    @Override
                    public void onPostExecute(JSONObject jsonObject) {

                            try {
                                //GOTO feed. or wait for confirmation from admin
                                startActivity(new Intent(Register.this, Feed.class));
                                finish();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                    }
                }).execute(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }


    /**
     * Takes an inputStream and extracts its string
     *
     * @param in
     * @return Returns inputStreams string
     * @throws IOException
     */
    public static String StreamToString(InputStream in) throws IOException
    {
        if (in == null)
        {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try
        {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1)
            {
                writer.write(buffer, 0, n);
            }
        } finally
        {
        }
        return writer.toString();
    }

    /**
     * Hides the keyboard
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Register.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}