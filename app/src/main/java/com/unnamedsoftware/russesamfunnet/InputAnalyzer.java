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
        shape.getPaint().setStrokeWidth(5);

        EditText inputField = (EditText) fieldName;
        inputField.setBackground(shape);
    }

    /**
     * WIP, supposed to clear or remove the border for when the user have done fixed the operation but sill (either new or other old one) have an error.
     * This is to remove confusion.
     * @param fieldName
     */
    public static void clearBorder(View fieldName)
    {
        EditText inputField = (EditText) fieldName;
        inputField.getBackground().clearColorFilter();
    }


    /**
     * Checks if the input field field is empty, returns true if it is.
     *
     * @return True if field is empty
     */
    public static boolean isInputFieldEmpty(View fieldName)
    {
        EditText inputEditText = (EditText) fieldName;
        try
        {
            String input = inputEditText.getText().toString();

            if (input.matches(""))
            {
                return true;
            } else
            {
                return false;
            }
        } catch (NullPointerException e)
        {
            System.out.println("Nothing in input");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if the given string is empty or null, returns true if this is the case.
     * @param string
     * @return True if string is empty or null
     */
    public static boolean isStringEmpty(String string)
    {
        String checkString = string;

        try
        {
            if (checkString.isEmpty())
            {
                return true;
            }
        } catch (NullPointerException e)
        {
            return true;
        }

        return false;
    }



    /**
     * Returns true if the email is in the correct format
     *
     * @return True if the email is in the correct format
     */
    public static boolean isUserEmailValid(View viewID)
    {
        Boolean state;

        EditText inputEditText = (EditText) viewID;
        String inputString = inputEditText.getText().toString();

        state = android.util.Patterns.EMAIL_ADDRESS.matcher(inputString).matches();
        return state;
    }

    /**
     * Returns true if the password is correct length
     * Length restrictions are between 8 and 255 char.
     * @return Returns true if the password is correct length
     */
    public static boolean isPasswordValid(View viewId)
    {
        boolean state = false;

        EditText inputEditText = (EditText) viewId;
        String inputString = inputEditText.getText().toString();

        if (inputString.length() >= 8 && inputString.length() <= 255)
        {
            state = true;
        }
        return state;
    }

    /**
     * Returns true if the passwords matches
     * @return Returns true if the passwords matches
     */
    public static boolean doesPasswordsMatch(View password1, View password2)
    {
        boolean state = false;

        EditText pass1 = (EditText) password1 ;
        String stringPass1 = pass1.getText().toString();

        EditText pass2 = (EditText) password2 ;
        String stringPass2 = pass2.getText().toString();

        if (stringPass1.equals(stringPass2))
        {
            state = true;
        }
        return state;
    }
}
