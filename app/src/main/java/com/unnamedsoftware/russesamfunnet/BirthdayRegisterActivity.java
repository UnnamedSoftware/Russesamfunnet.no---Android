package com.unnamedsoftware.russesamfunnet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Alexander Eilert Berg on 26.03.2018.
 */

public class BirthdayRegisterActivity extends AppCompatActivity
{
    private EditText dateDisplay;

    final String[] day = new String[1];
    final String[] month = new String[1];
    final String[] year = new String[1];

    private String firstName;
    private String surname;
    private String email;
    private String password;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_register);

        this.dateDisplay = findViewById(R.id.dateDisplay);

        this.day[0] = String.valueOf(1);
        this.month[0] = String.valueOf(1);
        this.year[0] = Integer.toString(getYear() - 17);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Velg fødselsdato");

        ImageButton imageButton = findViewById(R.id.dateImage);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseDate();
            }
        });

        dateDisplay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseDate();
            }
        });

        setInputData();

        Button button = findViewById(R.id.nextButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (dateString == null)
                {
                    Toast toast = Toast.makeText(BirthdayRegisterActivity.this, "Vennligst velg fødselsdato", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else
                {
                    Intent intent = new Intent(BirthdayRegisterActivity.this, SchoolRegisterActivity.class);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("surname", surname);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("dataString", dateString);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * Set data from intent to variables
     */
    private void setInputData()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try
        {
            this.firstName = (String) bundle.get("firstName");
            this.surname = (String) bundle.get("surname");
            this.email = (String) bundle.get("email");
            this.password = (String) bundle.get("password");
        } catch (NullPointerException e)
        {
            System.out.println("Empty intent. e: ");
            e.printStackTrace();
        }
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
        numberPickerDay.setMaxValue(31);
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
        numberPickerYear.setWrapSelectorWheel(true);
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
                dateDisplay.setText(date);
                dateString = date;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Retrieves the current year and returns it as an int
     *
     * @return the current year as an int
     */
    private int getYear()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

}
