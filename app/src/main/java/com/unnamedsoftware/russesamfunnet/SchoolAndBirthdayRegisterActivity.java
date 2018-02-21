package com.unnamedsoftware.russesamfunnet;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import java.util.Calendar;


public class SchoolAndBirthdayRegisterActivity extends AppCompatActivity
{
    private EditText editText;
    private static Dialog dialog;

    final String[] day = new String[1];
    final String[] month = new String[1];
    final String[] year = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_and_birthday_register);

        editText = findViewById(R.id.dateDisplay);

        this.day[0] = String.valueOf(1);
        this.month[0] = String.valueOf(1);
        this.year[0] = Integer.toString(getYear() - 17);


        ImageButton imageButton = findViewById(R.id.dateImage);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseDate();
            }
        });

        editText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseDate();
            }
        });
    }


    /**
     * Displays the date picker, when date is set updates the date display and saves the date as a string for use with the registration
     */
    private void chooseDate()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Fødsels dato");
        dialog.setContentView(R.layout.datepicker);

        Button confirm = dialog.findViewById(R.id.confirmButton);

        final NumberPicker numberPickerDay = dialog.findViewById(R.id.dateDay);
        final NumberPicker numberPickerMonth = dialog.findViewById(R.id.dateMonth);
        final NumberPicker numberPickerYear = dialog.findViewById(R.id.dateYear);

        //--- Set Day ---
        numberPickerDay.setMaxValue(30);
        numberPickerDay.setMinValue(1);
        numberPickerDay.setWrapSelectorWheel(false);
        numberPickerDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue)
            {
                day[0] = Integer.toString(newValue);
            }
        });

        //--- Set Month ---
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setMinValue(1);

        //If you want to have Months displayed as strings do use this as a guide
        /*
            NumberPicker numberPicker = new NumberPicker(this);
            String[] arrayString= new String[]{"hakuna","matata","timon","and","pumba"};
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(arrayString.length-1);

            numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
            return arrayString[value];
            }
            });
        */


        numberPickerMonth.setWrapSelectorWheel(false);
        numberPickerMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue)
            {
                month[0] = Integer.toString(newValue);
            }
        });

        //--- Set Year ---
        numberPickerYear.setMaxValue(getYear() - 17);
        numberPickerYear.setMinValue(getYear() - 30);
        numberPickerYear.setWrapSelectorWheel(false);
        numberPickerYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue)
            {
                year[0] = Integer.toString(newValue);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String date = (day[0] + "." + month[0] + "." + year[0]);
                editText.setText(date);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    /**
     * Retrieves the current year and returns it as an int
     * @return the current year as an int
     */
    protected int getYear()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }
}
