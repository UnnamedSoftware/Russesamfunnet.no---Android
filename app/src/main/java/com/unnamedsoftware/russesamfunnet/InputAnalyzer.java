package com.unnamedsoftware.russesamfunnet;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.EditText;

/**
 * Checks given text..... TODO:PLEASE ADD DOCUMENTATION :(
 * Created by Alexander Eilert Berg on 02.02.2018.
 */

public class InputAnalyzer
{

    /**
     * Adds a red boarder to the given field
     *
     * @param fieldName
     */
    public static void drawRedBorder(View fieldName)
    {
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
    public static boolean isInputFieldEmpty(View fieldName)
    {
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
    public static boolean isUserEmailValid(View viewID)
    {
        Boolean state;

        EditText usernameEditText = (EditText) viewID;
        String userEmail = usernameEditText.getText().toString();

        state = android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches();
        return state;
    }




}
