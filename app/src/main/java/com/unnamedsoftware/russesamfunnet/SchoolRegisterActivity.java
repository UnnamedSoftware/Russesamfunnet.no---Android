package com.unnamedsoftware.russesamfunnet;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.RecyclerView.SchoolAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SchoolRegisterActivity extends AppCompatActivity
{
    private TextView schoolNameDisplay;
    private Button registerButton;

    private Spinner municipalitySpinner;
    private Spinner locationSpinner;

    private String municipality;
    private String location;

    private List<SchoolEntity> schoolEntityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SchoolAdapter schoolAdapter;

    private String termsOfService;

    private String firstName;
    private String surname;
    private String email;
    private String password;
    private String dateString;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_register);

        this.locationSpinner = findViewById(R.id.locationSpinner);


        //Spinner setup
        setupMunicipalitySpinner();
        setupLocationSpinner();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        try
        {
            getSchools();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Velg skole");

        InputStream inputStream = this.getResources().openRawResource(R.raw.terms_of_service);
        try
        {
            termsOfService = StreamToString(inputStream);
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

        this.registerButton = findViewById(R.id.registerButton);
        this.registerButton.setClickable(false);

        setInputData();

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String school = schoolAdapter.getSchoolName();


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
            this.dateString = (String) bundle.get("dataString");
        } catch (NullPointerException e)
        {
            System.out.println("Empty intent. e: ");
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void registerRuss(String dateString, String school) throws IOException {
        String newUrl;
        if (AccessToken.getCurrentAccessToken() != null)
        {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            newUrl =  getString(R.string.url) + "facebookRegisterNew?accessToken=" + AccessToken.getCurrentAccessToken().getToken()
                    + "&email=" + email
                    + "&schoolId=" + school
                    + "&russYear=0"
                    + "&birthdate=" + dateString;
        }else {
            newUrl =  getString(R.string.url) + "russasamfunnetRegister?email=" + email
                    + "&password=" + password
                    + "&schoolName=" + school
                    + "&firstName=" + firstName
                    + "&lastName=" + surname;
        }
        try {
            new JSONObjectParser(new JSONObjectParser.OnPostExecute() {
                @Override
                public void onPostExecute(JSONObject jsonObject) {
                    try {
                        if(jsonObject.getString("loginStatus").equals("Login success")) {
                            ((Global) getApplication()).setAccessToken(jsonObject.getString("accessToken"), "russesamfunnet");
                            displayTermsOfService();
                        }else if(jsonObject.getString("loginStatus").equals("User successfully registered")) {
                            ((Global) getApplication()).setAccessToken(jsonObject.getString("accessToken"), "russesamfunnet");
                            displayTermsOfService();
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
        startActivity(new Intent(SchoolRegisterActivity.this, Feed.class));
        finish();
    }

    /**
     * Displays the terms of service as an AlertDialog.
     * The user has to accept or disagree.
     * If the user accepts the input data gets sent to the server.
     * If the user disagrees the user is sent to the Login screen.
     */
    private void displayTermsOfService()
    {
        final AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else
        {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Vilkår for Tjenesten")
                .setMessage(termsOfService)
                .setPositiveButton("Avslå", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                    }
                })
                .setNegativeButton("Aksepter", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        goToFeed();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Takes an inputStream and extracts its string
     *
     * @param in
     * @return Returns inputStreams string
     * @throws IOException
     */
    public static String StreamToString(InputStream in) throws IOException
    {
        if (in == null)
        {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try
        {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1)
            {
                writer.write(buffer, 0, n);
            }
        } finally
        {
        }
        return writer.toString();
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
        }else
        {
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
        }
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
