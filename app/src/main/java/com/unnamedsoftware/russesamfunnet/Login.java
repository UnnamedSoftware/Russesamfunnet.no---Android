package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Eilert Berg on 19.01.2018.
 */

public class Login extends AppCompatActivity
{
    private static final int RC_SIGN_IN = 83;
    private static final String ERROR_TAG = "FATAL ERROR";

    private Boolean response = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signIn(mGoogleSignInClient);
            }
        });

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

    }

    @Override
    public void onStart(){
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    /**
     * If the user already is logged in, jump straight to app
     * @param account
     */
    private void updateUI(GoogleSignInAccount account) {
        System.out.println(account.getDisplayName());

    }

    /**
     * Login google user
     */
    private void signIn(GoogleSignInClient mGoogleSignInClient) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, this.RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ERROR_TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    /**
     * The functions associated with the login button
     *
     * @param view
     */
    public void loginUser(View view)
    {
<<<<<<< HEAD
view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (isInputFieldEmpty(findViewById(R.id.userEmail)))
                    {
                        drawRedBorder(findViewById(R.id.userEmail));
                        Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    } else if (!isUserEmailValid())
                    {
                        Toast.makeText(Login.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    } else if (isInputFieldEmpty(findViewById(R.id.userPassword)))
                    {
                        drawRedBorder(findViewById(R.id.userPassword));
                        Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        if(userRegistered(view))
                        {
                            startActivity(new Intent(Login.this, Feed.class));
                        }else {Toast.makeText(Login.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();}
                    }
                }
            }
=======
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
>>>>>>> 3f348496fa496f1e287bb29cbb5c3d13f283390b
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

}
