package com.example.busticketsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import ticket.info.*;

public class TripOverview extends AppCompatActivity {

    TextView destination;
    TextView availableSeatCountTextView;
    TextView totalPassengerCountTextView;
    ImageButton nextArrow;
    String[] stopPoints = null;
    int index;
    boolean isTripEnded = false;

    private LinearLayout pendingDestinationsContainer;
    int totalBusCapacity = RealTimeData.seatAvailability;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_overview);

        // This code formats the current date and time into a human-readable string,
        // displaying the day of the week, full month name, day, year, and time in hours, minutes, and seconds.
        availableSeatCountTextView = findViewById(R.id.availSeatCount);
        totalPassengerCountTextView = findViewById(R.id.passengerCount);
        destination = findViewById(R.id.currentDestination);
        nextArrow = findViewById(R.id.arrowNext);

        // This code retrieves the selected route and trip status from SharedPreferences,
        // initializes the passenger button,
        // and loads the last saved destination index for real-time tracking.
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        DestinationInfo.presetNumber = prefs.getInt("selected_route", -1);
        isTripEnded = prefs.getBoolean("is_trip_ended", false);

        Button addPassengerButton = findViewById(R.id.addPassengerButton);
        SharedPreferences tripDataPrefs = getSharedPreferences("TripData", MODE_PRIVATE);
        int lastSavedIndex = tripDataPrefs.getInt("current_destination_index", 0);

        index = lastSavedIndex;
        RealTimeData.currentDestinationCounter = lastSavedIndex;

        // This code determines the state of the trip and updates the UI accordingly.
        // If the trip has ended, it displays "- ROUTE COMPLETED -", disables navigation and
        // passenger addition, and resets stop points. If no route is selected, it shows "No Selected Route",
        // hides the navigation arrow, and disables passenger addition. Otherwise, it loads the preset route data,
        // sets the first destination, enables navigation and passenger buttons, or handles errors in route data.
        if (isTripEnded) {
            destination.setText("- ROUTE COMPLETED -");
            nextArrow.setVisibility(View.VISIBLE);
            nextArrow.setEnabled(false);
            if (addPassengerButton != null) {
                addPassengerButton.setEnabled(false);
            }

            stopPoints = null;
            index = 0;

        } else if (DestinationInfo.presetNumber == -1) {
            destination.setText("No Selected Route");
            nextArrow.setVisibility(View.GONE);
            if (addPassengerButton != null) {
                addPassengerButton.setEnabled(false);
            }
            stopPoints = null;
            index = 0;

        } else {
            DestinationInfo.presetSetter(DestinationInfo.presetNumber);

            if (DestinationInfo.originDestination != null && !DestinationInfo.originDestination.isEmpty()) {

                destination.setText(DestinationInfo.originDestination.get(0));

                stopPoints = DestinationInfo.originDestination.toArray(new String[0]);

                nextArrow.setVisibility(View.VISIBLE);
                nextArrow.setEnabled(true);
                if (addPassengerButton != null) {
                    addPassengerButton.setEnabled(true);
                }
            } else {
                destination.setText("Error loading route data");
                nextArrow.setVisibility(View.GONE);
                nextArrow.setEnabled(false);
                if (addPassengerButton != null) {
                    addPassengerButton.setEnabled(false);
                }
                stopPoints = null;
                index = 0;
            }
        }

        // This code handles route progression by managing passenger drop-offs and
        // updating the destination dynamically. If passengers are queued for drop-off
        // at the current stop, it processes the drop-off and updates the UI accordingly.
        // If no passengers are being dropped off, it moves to the next stop, saves progress
        // to SharedPreferences, and updates the trip data
        // When reaching the final destination, it marks the trip as completed,
        // disables relevant UI elements, resets trip-related data, and transitions to
        // the summary screen. It also ensures appropriate user feedback with toast messages
        // when route data is unavailable or the trip has already ended.
        nextArrow.setFocusable(false);
        nextArrow.setClickable(true);

        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopPoints != null && stopPoints.length > 0 && !isTripEnded) {

                    boolean handledAsDropOffClick = false;

                    if (RealTimeData.dropOffQueue != null && !RealTimeData.dropOffQueue.isEmpty()) {
                        String currentUiDestination = destination.getText().toString().trim();
                        String nextDropOffLocation = RealTimeData.dropOffQueue.get(0).currentLocation.trim();

                        if (currentUiDestination.equals(nextDropOffLocation)) {
                            RealTimeData.DropOffQueue.passengerDrop();
                            updatePendingDestinationsUI();
                            updateCountsUI();
                            Toast.makeText(TripOverview.this, "Passengers dropped off at " + currentUiDestination, Toast.LENGTH_SHORT).show();
                            handledAsDropOffClick = true;
                        }
                    }

                    if (!handledAsDropOffClick) {
                        index++;

                        if (index < stopPoints.length) {
                            destination.setText(stopPoints[index]);

                            SharedPreferences prefs = getSharedPreferences("TripData", MODE_PRIVATE); // Use a name like "TripData"
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("current_destination_index", index);
                            editor.apply();

                            RealTimeData.currentDestinationCounter = index;
                        } else {
                            destination.setText("- ROUTE COMPLETED -");
                            nextArrow.setEnabled(false);

                            Intent summaryIntent = new Intent(TripOverview.this, TripSummary.class);
                            summaryIntent.putExtra("total_passengers", RealTimeData.totalBoardedPassengersForTrip);
                            summaryIntent.putExtra("total_revenue", Fare.tripRevenue);

                            startActivity(summaryIntent);


                            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            editor.putBoolean("is_trip_ended", true);

                            SharedPreferences tripDataPrefs = getSharedPreferences("TripData", MODE_PRIVATE);
                            SharedPreferences.Editor tripDataEditor = tripDataPrefs.edit();
                            tripDataEditor.remove("current_destination_index");

                            editor.apply();

                            isTripEnded = true;

                            Button addPassengerButton = findViewById(R.id.addPassengerButton);
                            if (addPassengerButton != null) {
                                addPassengerButton.setEnabled(false);
                            }

                            RealTimeData.currentDestinationCounter = 0;

                            RealTimeData.totalBoardedPassengersForTrip = 0;

                            if (RealTimeData.dropOffQueue != null) {
                                RealTimeData.dropOffQueue.clear();
                            }
                            Fare.tripRevenue = 0.0;
                        }
                    }
                } else if (isTripEnded) {
                    Toast.makeText(TripOverview.this, "Route is already completed.", Toast.LENGTH_SHORT).show();
                } else if (stopPoints == null || stopPoints.length == 0) {
                    Toast.makeText(TripOverview.this, "Route data not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // This code ensures a fully immersive UI experience for the trip overview page by
        // hiding navigation bars and setting the screen to full-screen mode.
        View overlay = findViewById(R.id.TripOverviewPage);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                      | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // This code sets up a button to navigate to the AddPassenger screen when clicked,
        // allowing users to register new passengers during the trip.
        addPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripOverview.this, AddPassenger.class);
                startActivity(intent);
            }
        });

        // This code sets up the settings icon to navigate to the Settings screen when clicked,
        // providing users with easy access to configuration options.
        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripOverview.this, Settings.class);
                startActivity(intent);
            }
        });

        // This code initializes the pendingDestinationsContainer layout and refreshes the UI by calling
        // updatePendingDestinationsUI() and updateCountsUI(), ensuring real-time updates for destination tracking and passenger counts.
        pendingDestinationsContainer = findViewById(R.id.pendingDestinationsContainerLayout);
        updatePendingDestinationsUI();
        updateCountsUI();
    }
    // This method updates the trip UI by checking SharedPreferences
    // for the selected route and trip status, then adjusting the
    // destination and navigation elements accordingly.
    private void updateTripUI() {
        SharedPreferences myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        DestinationInfo.presetNumber = myPrefs.getInt("selected_route", -1);
        isTripEnded = myPrefs.getBoolean("is_trip_ended", false);
        Button addPassengerButton = findViewById(R.id.addPassengerButton);

        if (isTripEnded) {
            destination.setText("- ROUTE COMPLETED -");
            nextArrow.setVisibility(View.VISIBLE);
            nextArrow.setEnabled(false);
            if (addPassengerButton != null) {
                addPassengerButton.setEnabled(false);
            }
            stopPoints = null;
            index = 0;
            RealTimeData.currentDestinationCounter = 0;

        } else if (DestinationInfo.presetNumber == -1) {
            destination.setText("no selected route");
            nextArrow.setVisibility(View.GONE);
            nextArrow.setEnabled(false);
            if (addPassengerButton != null) {
                addPassengerButton.setEnabled(false);
            }

            stopPoints = null;
            index = 0;
            RealTimeData.currentDestinationCounter = 0;

        } else {
            DestinationInfo.presetSetter(DestinationInfo.presetNumber);

            if (DestinationInfo.originDestination != null && !DestinationInfo.originDestination.isEmpty()) {
                stopPoints = DestinationInfo.originDestination.toArray(new String[0]);

                SharedPreferences tripDataPrefs = getSharedPreferences("TripData", MODE_PRIVATE);
                int loadedIndex = tripDataPrefs.getInt("current_destination_index", 0);

                index = loadedIndex;
                RealTimeData.currentDestinationCounter = loadedIndex;

                if (index >= 0 && index < stopPoints.length) {
                    destination.setText(stopPoints[index]);
                } else if (stopPoints.length > 0) {
                    destination.setText(stopPoints[0]);
                    index = 0;
                    RealTimeData.currentDestinationCounter = 0;
                    SharedPreferences.Editor editor = tripDataPrefs.edit();
                    editor.putInt("current_destination_index", index);
                    editor.apply();
                } else {
                    destination.setText("Error loading route data (Empty Stops)");
                    nextArrow.setVisibility(View.GONE);
                    nextArrow.setEnabled(false);
                    if (addPassengerButton != null) addPassengerButton.setEnabled(false);
                    stopPoints = null;
                    index = 0;
                    RealTimeData.currentDestinationCounter = 0;
                }

                nextArrow.setVisibility(View.VISIBLE);
                nextArrow.setEnabled(true);
                if (addPassengerButton != null) {
                    addPassengerButton.setEnabled(true);
                }
            } else {
                destination.setText("Error loading route data");
                nextArrow.setVisibility(View.GONE);
                nextArrow.setEnabled(false);
                if (addPassengerButton != null) {
                    addPassengerButton.setEnabled(false);
                }
                stopPoints = null;
                index = 0;
                RealTimeData.currentDestinationCounter = 0;
            }
        }
    }

    // Ensures UI updates when the activity resumes.
    @Override
    protected void onResume() {
        super.onResume();
        updateTripUI();
        updateCountsUI();
        updatePendingDestinationsUI();
    }

    // Refreshes the pending destinations UI by displaying
    // drop-off locations or a fallback message if none exist.
    private void updatePendingDestinationsUI() {
        pendingDestinationsContainer.removeAllViews();

        if (RealTimeData.dropOffQueue != null && !RealTimeData.dropOffQueue.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(this);

            for (RealTimeData.DropOffQueue dropOff : RealTimeData.dropOffQueue) {
                View itemView = inflater.inflate(R.layout.pending_destination_item, pendingDestinationsContainer, false);
                TextView passengerCountTextView = itemView.findViewById(R.id.passengerCountItem);
                TextView placeTextView = itemView.findViewById(R.id.placeItem);

                passengerCountTextView.setText(String.valueOf(dropOff.passengerDroppingOff));
                placeTextView.setText(dropOff.currentLocation);

                pendingDestinationsContainer.addView(itemView);
            }
        } else {
            TextView noPendingText = new TextView(this);
            noPendingText.setText("No Pending Destinations");
            noPendingText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            pendingDestinationsContainer.addView(noPendingText);
        }
    }

    // Updates seat availability and passenger count, adjusting text color if over capacity.
    private void updateCountsUI() {

        int totalPassengersOnBus = 0;
        if (RealTimeData.dropOffQueue != null) {
            for (RealTimeData.DropOffQueue dropOff : RealTimeData.dropOffQueue) {
                totalPassengersOnBus += dropOff.passengerDroppingOff;
            }
        }

        int totalBusCapacity = BusInfo.seatCount;

        int availableSeats = totalBusCapacity - totalPassengersOnBus;

        if (availableSeats < 0) {
            availableSeats = 0;
        }

        if (availableSeatCountTextView != null) {
            availableSeatCountTextView.setText(String.valueOf(availableSeats));
        }

        if (totalPassengerCountTextView != null) {
            totalPassengerCountTextView.setText(String.valueOf(totalPassengersOnBus));
            if (totalPassengersOnBus > totalBusCapacity) {
                totalPassengerCountTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                totalPassengerCountTextView.setTextColor(getResources().getColor(android.R.color.white));
            }

        }
    }
}