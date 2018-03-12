package com.unnamedsoftware.russesamfunnet;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.unnamedsoftware.russesamfunnet.Search.SearchFragment;

import java.util.Calendar;


public class SchoolAndBirthdayRegisterActivity extends FragmentActivity
{
    private EditText editText;

    private Spinner municipalitySpinner;
    private Spinner locationSpinner;
    private static Dialog dialog;

    private String municipality;
    private String location;

    final String[] day = new String[1];
    final String[] month = new String[1];
    final String[] year = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_and_birthday_register);

        this.editText = findViewById(R.id.dateDisplay);
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
     * Loads the given fragment into the fragmentLayout
     * @param fragment
     */
    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.schoolRegistrationSearchFrameLayout, fragment);
        fragmentTransaction.commit();
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
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
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setArguments(getBundle());
                            loadFragment(searchFragment);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {
                        }
                    });
                    break;
            }
        } else
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
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setArguments(getBundle());
                    loadFragment(searchFragment);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {
                }
            });
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
                editText.setText(date);
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
    protected int getYear()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    /**
     * Creates a bundle for the search fragment
     */
    protected Bundle getBundle()
    {
        Bundle bundle = new Bundle();
        bundle.putString("location", this.location);
        bundle.putString("municipality", this.municipality);
        bundle.putString("wantedDataSet", "school");
        return bundle;
    }
}
