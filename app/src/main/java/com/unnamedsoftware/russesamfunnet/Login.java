package com.unnamedsoftware.russesamfunnet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Eilert Berg on 19.01.2018.
 */

public class Login extends AppCompatActivity
{

    private Boolean response = false;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.loginParent));

        loginUser(findViewById(R.id.loginButton));



        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        Button dummyButton = findViewById(R.id.dummyButton);
        dummyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Login.this, Feed.class));
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        finishServerCom();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.fillInStackTrace();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * The functions associated with the login button
     *
     * @param view
     */
    public void loginUser(View view)
    {
        view.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.userEmail)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.userEmail));
                                            Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                                        } else if (!InputAnalyzer.isUserEmailValid(findViewById(R.id.userEmail)))
                                        {
                                            Toast.makeText(Login.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                                        } else if (InputAnalyzer.isInputFieldEmpty(findViewById(R.id.userPassword)))
                                        {
                                            InputAnalyzer.drawRedBorder(findViewById(R.id.userPassword));
                                            Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                        } else
                                        {
                                            userRegistered(view);
                                        }
                                    }
                                }
        );
    }


    /**
     * Checks if the user is registered
     *
     * @return
     */
    private void userRegistered(View view)
    {
        EditText userEmail = findViewById(R.id.userEmail);
        String userEmailString = userEmail.getText().toString();

        EditText userPassword = findViewById(R.id.userPassword);
        String userPasswordString = userPassword.getText().toString();

        toServer(view, userEmailString, userPasswordString);
    }


    /**
     * Sends login information to server
     *
     * @param view
     * @param userEmailString
     * @param userPasswordString
     */
    private void toServer(View view, String userEmailString, String userPasswordString)
    {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        String url = getString(R.string.url) + "login?email=" + userEmailString + "&password=" + userPasswordString;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        String theResponse = response.substring(1, response.length()-1);
                        System.out.println(response);
                        System.out.println(theResponse);
                        if (theResponse.equalsIgnoreCase("true"))
                        {
                            System.out.println("finish");
                            finishServerCom();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                System.out.println("Something did not work:" + error);
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", " application/json");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void finishServerCom()
    {
        startActivity(new Intent(Login.this, Feed.class));
        finish();
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
                    hideSoftKeyboard(Login.this);
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