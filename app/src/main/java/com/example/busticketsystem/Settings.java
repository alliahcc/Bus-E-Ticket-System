package com.example.busticketsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import ticket.info.*;

import androidx.appcompat.app.AppCompatActivity;
import ticket.info.BusInfo;

public class Settings extends AppCompatActivity {

    TextView seatCapacity;
    BusInfo info;
    private Button saveButton;
    private Button endTripButton;
    private SharedPreferences prefs;
    private boolean isTripEnded;

    private RadioButton route1Button;
    private RadioButton route2Button;

    int selectedRouteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // This code initializes the seat capacity text view
        seatCapacity = findViewById(R.id.settings_capacityCount);
        seatCapacity.setText(String.valueOf(BusInfo.seatCount));

        // This code hides the navigation bar and makes
        // the UI immersive and fullscreen for a smoother user experience.
        View overlay = findViewById(R.id.SettingsPage);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // This code sets up a back button that closes the current activity when clicked.
        ImageView back_icon = findViewById(R.id.back_icon);
        back_icon.setFocusable(false);
        back_icon.setClickable(true);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //This code initializes input fields for driver name,
        // conductor name, and plate number, then creates a BusInfo object.
        EditText driverName = findViewById(R.id.driverName);
        EditText conductorName = findViewById(R.id.conductorName);
        EditText plateNumber = findViewById(R.id.plateNumber);

        info = new BusInfo();

        // This code retrieves a saved driver name from SharedPreferences,
        // updates the UI accordingly, and saves changes when the user presses Enter.
        SharedPreferences prefDriverName = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedDriverName = prefDriverName.getString("driver_name", "");
        if (!savedDriverName.isEmpty()) {
            info.setDriverName(savedDriverName);
            driverName.setText(savedDriverName);
        } else {
            info.setDriverName("");
        }

        driverName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String s1 = driverName.getText().toString();

                    SharedPreferences prefs = v.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("driver_name", s1);
                    editor.apply();

                    if (info != null) {
                        info.setDriverName(s1);
                    }

                    driverName.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    return true;
                }
                return false;
            }
        });

        setFocusChangeListener(driverName, "driver_name");

        // This code retrieves the saved conductor name from SharedPreferences,
        // updates the UI, and ensures changes are stored when the user presses Enter.
        SharedPreferences prefConductorName = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedConductorName = prefConductorName.getString("conductor_name", "");
        if (!savedConductorName.isEmpty()) {
            info.setConductorName(savedConductorName);
            conductorName.setText(savedConductorName);
        } else {
            info.setConductorName("");
        }

        conductorName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String s1 = conductorName.getText().toString();

                    SharedPreferences prefs = v.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("conductor_name", s1);
                    editor.apply();

                    if (info != null) {
                        info.setConductorName(s1);
                    }

                    conductorName.clearFocus();

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    return true;
                }
                return false;
            }
        });

        setFocusChangeListener(conductorName, "conductor_name");

        // This code retrieves the saved plate number from SharedPreferences,
        // updates the UI, and ensures changes are stored when the user presses Enter.
        SharedPreferences prefPlateNumber = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedPlateNumber = prefPlateNumber.getString("plate_number", "");
        if (!savedPlateNumber.isEmpty()) {
            info.setVehicleNumber(savedPlateNumber);
            plateNumber.setText(savedPlateNumber);
        } else {
            info.setVehicleNumber("");
        }

        plateNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String s1 = plateNumber.getText().toString();

                    SharedPreferences prefs = v.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("plate_number", s1);
                    editor.apply();

                    if (info != null) {
                        info.setVehicleNumber(s1);
                    }

                    plateNumber.clearFocus();

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    return true;
                }
                return false;
            }
        });

        setFocusChangeListener(plateNumber, "plate_number");

        // This code initializes buttons for saving and ending the trip,
        // along with a radio group for route selection.
        saveButton = findViewById(R.id.saveButton);
        endTripButton = findViewById(R.id.endTrip);

        RadioGroup selectRoute = findViewById(R.id.selectRoute);
        route1Button = findViewById(R.id.route1Button);
        route2Button = findViewById(R.id.route2Button);

        // This code retrieves the trip status from SharedPreferences and
        // enables route selection buttons if the trip has ended.
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isTripEnded = prefs.getBoolean("is_trip_ended", false);

        if (route1Button != null) {
            route1Button.setEnabled(isTripEnded);
        }
        if (route2Button != null) {
            route2Button.setEnabled(isTripEnded);
        }

        // This code retrieves the selected route from SharedPreferences,
        // updates UI elements accordingly, and applies preset data if a valid route is found.
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        selectedRouteId = sharedPreferences.getInt("selected_route", -1);

        if (selectedRouteId != -1 && isTripEnded) {

            if (selectedRouteId == R.id.route1Button && route1Button != null) {
                route1Button.setChecked(true);
            } else if (selectedRouteId == R.id.route2Button && route2Button != null) {
                route2Button.setChecked(true);
            }

            disableRadioButtons(route1Button, route2Button);

            saveButton.setEnabled(false);
            endTripButton.setEnabled(true);

            int routeValue = -1;
            if (selectedRouteId == R.id.route1Button) {
                routeValue = 0;
            } else if (selectedRouteId == R.id.route2Button) {
                routeValue = 1;
            }

            if (routeValue != -1) {
                DestinationInfo.presetSetter(routeValue);
            }
        } else {
            saveButton.setEnabled(true);
            endTripButton.setEnabled(false);
        }

        // This code enables the save button when a route is selected in the radio group.
        selectRoute.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                saveButton.setEnabled(checkedId != -1);
            }
        });

        // This code sets the text of route selection buttons based on preset data,
        // checks the appropriate route if previously selected, and disables the buttons accordingly.
        route1Button.setText(DestinationInfo.routeNames(0));
        route2Button.setText(DestinationInfo.routeNames(1));
        if (selectedRouteId == 0 || selectedRouteId == 1) {
            if (selectedRouteId == 0) {
                route1Button.setChecked(true);
            } else {
                route2Button.setChecked(true);
            }
            disableRadioButtons(route1Button, route2Button);
            DestinationInfo.presetSetter(selectedRouteId);
        }

        // This code saves the selected route in SharedPreferences,
        // disables further selection, applies preset data, and enables the end trip button.
        saveButton.setOnClickListener(v -> {
            int selectedId = selectRoute.getCheckedRadioButtonId();
            if (selectedId != -1) {
                int routeValue = -1;
                if (selectedId == R.id.route1Button) {
                    routeValue = 0;
                } else if (selectedId == R.id.route2Button) {
                    routeValue = 1;
                }

                if (routeValue != -1) {
                    disableRadioButtons(route1Button, route2Button);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("selected_route", routeValue);
                    editor.putBoolean("is_trip_ended", false);
                    editor.apply();

                    if (saveButton != null) {
                        saveButton.setEnabled(false); // Disable save after saving
                    }
                    if (endTripButton != null) {
                        endTripButton.setEnabled(true); // Enable end trip after saving
                    }

                    DestinationInfo.presetSetter(routeValue);
                }
            }
        });

        // This code finalizes the trip by saving the trip-ended state,
        // clearing route data, resetting passenger and revenue counters, and launching a summary screen.
        endTripButton.setOnClickListener(v -> {
            enableRadioButtons(route1Button, route2Button);
            double finalTripRevenue = Fare.tripRevenue;
            int finalTotalPassengers = RealTimeData.totalBoardedPassengersForTrip;

            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean("is_trip_ended", true);

            editor.remove("selected_route");

            editor.apply();

            SharedPreferences tripDataPrefs = getSharedPreferences("TripData", MODE_PRIVATE);
            SharedPreferences.Editor tripDataEditor = tripDataPrefs.edit();

            tripDataEditor.remove("current_destination_index");
            tripDataEditor.apply();

            Intent summaryIntent = new Intent(Settings.this, TripSummary.class);

            summaryIntent.putExtra("total_revenue", finalTripRevenue);
            summaryIntent.putExtra("total_passengers", finalTotalPassengers);

            summaryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(summaryIntent);

            finish();

            RealTimeData.currentDestinationCounter = 0;
            RealTimeData.totalBoardedPassengersForTrip = 0;

            if (RealTimeData.dropOffQueue != null) {
                RealTimeData.dropOffQueue.clear();
            }

            Fare.tripRevenue = 0.0;
            selectRoute.clearCheck();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTripEnded && selectedRouteId == -1)  {
            saveButton.setEnabled(true);
            endTripButton.setEnabled(false);
        } else if (isTripEnded && selectedRouteId != -1) {
            route1Button.setChecked(false);
            route2Button.setChecked(false);
            saveButton.setEnabled(false);
            endTripButton.setEnabled(true);
            Log.d("ey", "ey");
            enableRadioButtons(route1Button, route2Button);
        } else if (!isTripEnded && selectedRouteId != -1) {
            saveButton.setEnabled(false);
            endTripButton.setEnabled(true);
        }
    }

    // This code disables multiple radio buttons by iterating through them in a loop.
    private void disableRadioButtons(RadioButton... buttons) {
        for (RadioButton button : buttons) {
            button.setEnabled(false);
        }
    }

    // This code enables multiple radio buttons by iterating through them in a loop.
    private void enableRadioButtons(RadioButton... buttons) {
        for (RadioButton button : buttons) {
            button.setEnabled(true);
        }
    }

    // This code enables an input field and sets focus on it for user interaction.
    private void enableField(EditText field) {
        field.setEnabled(true);
        field.requestFocus();
    }

    // This code hides the keyboard for the specified input field using the system's input method manager.
    private void hideKeyboard(EditText field) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
    }

    // This code automatically saves user input for driver name, conductor name, and plate number when the field loses focus, storing it in SharedPreferences and updating the `BusInfo` object accordingly. A smart way to keep data persistent!
    private void setFocusChangeListener(EditText field, String key) {
        field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    String textToSave = field.getText().toString();

                    SharedPreferences prefs = v.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(key, textToSave);
                    editor.apply();

                    if (info != null) {
                        switch (key) {
                            case "driver_name":
                                info.setDriverName(textToSave);
                                break;
                            case "conductor_name":
                                info.setConductorName(textToSave);
                                break;
                            case "plate_number":
                                info.setVehicleNumber(textToSave);
                                break;
                        }
                    }
                }
            }
        });
    }
}


