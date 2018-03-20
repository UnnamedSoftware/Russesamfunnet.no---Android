package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.SchoolAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SchoolAndBirthdayRegisterActivity extends AppCompatActivity
{
    private EditText dateDisplay;
    private TextView schoolNameDisplay;
    private Button okButton;

    private Spinner municipalitySpinner;
    private Spinner locationSpinner;

    private String municipality;
    private String location;
    private String dateString;

    final String[] day = new String[1];
    final String[] month = new String[1];
    final String[] year = new String[1];

    private List<SchoolEntity> schoolEntityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SchoolAdapter schoolAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_and_birthday_register);

        this.dateDisplay = findViewById(R.id.dateDisplay);
        this.locationSpinner = findViewById(R.id.locationSpinner);

        this.day[0] = String.valueOf(1);
        this.month[0] = String.valueOf(1);
        this.year[0] = Integer.toString(getYear() - 17);

        //Spinner setup
        setupMunicipalitySpinner();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

        try
        {
            getSchools();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.recyclerView = findViewById(R.id.recycler_view_schoolList);
        this.schoolAdapter = new SchoolAdapter(schoolEntityList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        this.okButton = findViewById(R.id.okButton);
        this.okButton.setAlpha(.5f);
        this.okButton.setClickable(false);

        if (InputAnalyzer.isStringEmpty(dateString))
        {
            Toast.makeText(this, "Vennligst velg din fødselsdato", Toast.LENGTH_LONG);

        } else if (InputAnalyzer.isStringEmpty(schoolAdapter.getSchoolName()))
        {
           Toast.makeText(this,"Vennligst velg din skole", Toast.LENGTH_LONG);

        } else
        {
            this.okButton.setAlpha(1f);
            this.okButton.setClickable(true);

        }

        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String school = schoolAdapter.getSchoolName();

                System.out.println(dateString);
                System.out.println(school);

                try{
                    System.out.println("Ta-daaaa");
                    registerRuss(dateString, school);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void registerRuss(String dateString, String school) throws IOException {
        String newUrl =  getString(R.string.url) + "facebookRegister?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&birthdate=" + dateString + "&schoolId=" + school;
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    try {
                        if(jsonObject.getString("loginStatus").equals("User successfully registered")) {
                            goToFeed();
                        }else
                        {
                            //do something else
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL(newUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void goToFeed()
    {
        startActivity(new Intent(SchoolAndBirthdayRegisterActivity.this, Feed.class));
        finish();
    }

    /**
     * Retrieves the needed data set from the server using the JSONparser. Needs location and municipality
     */
    private void getSchools() throws IOException
    {
        String wantedDataSet = "schools";
        String url = getString(R.string.url);
        String wanted = wantedDataSet.substring(0, 1).toUpperCase() + wantedDataSet.substring(1);
        if (this.municipality == null && this.location == null) //Both are empty, get all data
        {
            String getArgument = "getAll" + wanted;
            url += getArgument;

        } else if (this.municipality.isEmpty() && !this.location.isEmpty()) //Only municipality is empty, get all data based on location
        {
            String getArgument = "getLocation" + wanted;
            url += getArgument + "?" + "location=" + this.location;

        } else if (!this.municipality.isEmpty() && this.location.isEmpty()) //Only location is empty, get all data based on municipality
        {
            String getArgument = "getMunicipality" + wanted;
            url += getArgument + "?" + "municipality=" + this.municipality;

        } else //Both municipality and location are given, retrieve data based on them.
        {
            String getArgument = "getLocation" + wanted;
            url += getArgument + "?" + "location=" + this.location;
        }
        System.out.println(url);
        try
        {
            new JSONParser(new JSONParser.OnPostExecute()
            {
                @Override
                public void onPostExecute(JSONArray jsonArray) throws JSONException
                {
                    fillDataSet(jsonArray);
                }
            }).execute(new URL(url));
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Takes the given jsonArray and depending on the wanted data set chooses one of the cases
     * in the switch.
     *
     * @param jsonArray
     */
    private void fillDataSet(JSONArray jsonArray) throws JSONException
    {
        JSONArray schools = null;
        try
        {
            schools = jsonArray;
            for (int i = 0; i < schools.length(); i++)
            {
                JSONObject newDataJSONObject = schools.getJSONObject(i);

                Integer schoolId = Integer.valueOf(newDataJSONObject.getString("schoolId"));
                String schoolName = newDataJSONObject.getString("schoolName");
                String schoolStatus = newDataJSONObject.getString("schoolStatus");
                SchoolEntity school = new SchoolEntity(schoolId, schoolName, schoolStatus);

                schoolEntityList.add(school);
            }
            schoolAdapter.notifyDataSetChanged();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates the spinner associated with the municipalities
     */
    private void setupMunicipalitySpinner()
    {
        this.municipalitySpinner = findViewById(R.id.municipalitySpinner);
        final ArrayAdapter<CharSequence> municipalityAdapter = ArrayAdapter.createFromResource(this, R.array.municipalities, android.R.layout.simple_list_item_1);
        municipalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(municipalityAdapter);
        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                municipality = municipalitySpinner.getSelectedItem().toString();
                setupLocationSpinner();
                recyclerView.setAdapter(schoolAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }

    /**
     * Creates the spinner associated with the locations.
     * The locations are either based on the chosen municipality
     * or all available locations if municipality is not chosen.
     */
    private void setupLocationSpinner()
    {
        this.locationSpinner = findViewById(R.id.locationSpinner);
        final ArrayAdapter<CharSequence> locationAdapter;
        if (municipality != null)
        {
            switch (municipality)
            {
                case "Akershus":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Akershus, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Aust-Agder":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.AustAgder, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Buskerud":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Buskerud, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Finnmark":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Finnmark, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Hedmark":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Hedmark, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Hordaland":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Hordaland, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Møre og Romsdal":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.MoreOgRomsdal, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Nordland":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Nordland, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Oppland":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Oppland, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Rogaland":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Rogaland, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Sogn og Fjordane":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.SognOgFjordane, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Telemark":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Telemark, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Troms":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Troms, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Trøndelag":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Trondelag, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Vest-Agder":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.VestAgder, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Vestfold":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Vestfold, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
                case "Østfold":
                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.Ostfold, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;

                    case "Velg kommune":

                    locationAdapter = ArrayAdapter.createFromResource(this, R.array.AllLocation, android.R.layout.simple_list_item_1);
                    locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            location = locationSpinner.getSelectedItem().toString();
                            updateSchoolAdapter();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
            }
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

    /**
     * Clears the school adapter and request the school list based on chosen location
     */
    private void updateSchoolAdapter()
    {
        schoolAdapter.clear();
        try
        {
            getSchools();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
