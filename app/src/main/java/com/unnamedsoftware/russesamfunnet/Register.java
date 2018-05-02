package com.unnamedsoftware.russesamfunnet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Register extends AppCompatActivity
{
    private String firstName;
    private String surname;
    private String email;
    private String password;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI(findViewById(R.id.registerParent));

        registerUser(findViewById(R.id.registerButton));
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
                                            checkIfEmailIsInUse();
                                        }
                                    }
                                }
        );
    }

    private void checkIfEmailIsInUse()
    {
        String urlSend;
            urlSend = (getString(R.string.url) + "checkIfEmailIsInUse?email=" + email);

        try
        {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONObject jsonObject)
                {
                    try {
                        if (jsonObject.getString("response").equals("Email is not in use")) {
                            moveToNextActivity();
                        } else {
                            Toast toast = Toast.makeText(Register.this, "Email is already in use", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(urlSend));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public void moveToNextActivity()
    {
        setInputData();
        Intent intent = new Intent(Register.this, BirthdayRegisterActivity.class);
        intent.putExtra("firstName", firstName);
        intent.putExtra("surname", surname);
        intent.putExtra("email", email);
        // String passwordSHA = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        String hash = new String(Hex.encodeHex(DigestUtils.sha256(password)));
        intent.putExtra("password", hash);
        startActivity(intent);
    }


    /**
     * Reads the information in the various editTexts and sets them as variables
     */
    private void setInputData()
    {
        try
        {
            this.firstName = ((EditText) findViewById(R.id.registerFirstNameInput)).getText().toString();
            this.surname = ((EditText) findViewById(R.id.registerSurnameInput)).getText().toString();
            this.email = ((EditText) findViewById(R.id.registerEmailInput)).getText().toString();
            this.password = ((EditText) findViewById(R.id.registerPasswordInput)).getText().toString();
        } catch (NullPointerException e)
        {
            System.out.println("Empty editText fields. e: ");
            e.printStackTrace();
        }
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

    public void setupUI(View view)
    {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText))
        {
            view.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    hideSoftKeyboard(Register.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}