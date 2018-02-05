package com.unnamedsoftware.russesamfunnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Alexander Eilert Berg on 22.01.2018.
 */

public class Register extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                                        }
                                    }
                                }
        );
    }
}
