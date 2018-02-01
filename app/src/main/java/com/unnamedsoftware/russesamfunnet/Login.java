package com.unnamedsoftware.russesamfunnet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Eilert Berg on 19.01.2018.
 */

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginUser(findViewById(R.id.loginButton));

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    }

    /**
     * The functions associated with the login button
     *
     * @param view
     */
    public void loginUser(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputFieldEmpty(findViewById(R.id.userEmail))) {
                    drawRedBorder(findViewById(R.id.userEmail));
                    Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (!isUserEmailValid()) {
                    Toast.makeText(Login.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                } else if (isInputFieldEmpty(findViewById(R.id.userPassword))) {
                    drawRedBorder(findViewById(R.id.userPassword));
                    Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else
                    {
                        userRegistered(view);
                        startActivity(new Intent(Login.this, Feed.class));
                    }
                }
            }
        );}



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
        String url = getString(R.string.url) + "/login?email=" + userEmailString + "&password=" + userPasswordString ;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        finishServerCom();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Something did not work:" + error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", " application/json");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void finishServerCom()
    {
        finish();
    }


    /**
     * Adds a red boarder to the given field
     *
     * @param fieldName
     */
    private void drawRedBorder(View fieldName) {
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.RED);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(3);

        EditText inputField = (EditText) fieldName;
        inputField.setBackground(shape);
    }


    /**
     * Checks if the input field field is empty, returns true if it is.
     *
     * @return True if field is empty
     */
    private boolean isInputFieldEmpty(View fieldName) {
        Boolean state;

        EditText inputEditText = (EditText) fieldName;
        String input = inputEditText.getText().toString();

        if (input.matches(""))
        {
            state = true;
        } else
            {
            state = false;
        }
        return state;
    }


    /**
     * Returns true if the email is in the correct format
     *
     * @return True if the email is in the correct format
     */
    private boolean isUserEmailValid() {
        Boolean state;

        EditText usernameEditText = findViewById(R.id.userEmail);
        String userEmail = usernameEditText.getText().toString();

        state = android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches();
        System.out.println(userEmail);
        System.out.println(state);
        return state;
    }
}
