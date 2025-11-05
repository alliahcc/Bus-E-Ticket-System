package com.example.busticketsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ticket.info.*;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddPassenger extends AppCompatActivity {

    private static final String DISCOUNT_KEY = "calculated_discount";
    private static final String DISTANCE_KEY = "calculated_distance";
    private double totalFare = 0.0;
    private int adultCount = 0;
    private int childCount = 0;
    private int studentCount = 0;
    private int seniorpwdCount = 0;
    private ArrayList<Passenger> passengersList = new ArrayList<>();
    private AutoCompleteTextView pickupDropdown;
    private AutoCompleteTextView dropOffDropdown;
    private Button adultPlusButton, adultMinusButton;
    private TextView adultCountTextView;

    private Button childPlusButton, childMinusButton;
    private TextView childCountTextView;

    private Button studentPlusButton, studentMinusButton;
    private TextView studentCountTextView;

    private Button seniorpwdPlusButton, seniorMinusButton;
    private TextView seniorpwdCountTextView;
    private TextView totalAmountTextView;
    private Button confirmButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_passenger);

        passengersList = new ArrayList<>();

        // Initializes UI components for a passenger booking system
        pickupDropdown = findViewById(R.id.pickup_list);
        dropOffDropdown = findViewById(R.id.dropoff_list);

        adultPlusButton = findViewById(R.id.adultPlusButton);
        adultMinusButton = findViewById(R.id.adultMinusButton);
        adultCountTextView = findViewById(R.id.adultCount);

        childPlusButton = findViewById(R.id.childPlusButton);
        childMinusButton = findViewById(R.id.childMinusButton);
        childCountTextView = findViewById(R.id.childCount);

        studentPlusButton = findViewById(R.id.syidentPlusButton);
        studentMinusButton = findViewById(R.id.studentMinusButton);
        studentCountTextView = findViewById(R.id.studentCount);

        seniorpwdPlusButton = findViewById(R.id.seniorpwdPlusButton);
        seniorMinusButton = findViewById(R.id.seniorMinusButton);
        seniorpwdCountTextView = findViewById(R.id.seniorpwdCount);

        totalAmountTextView = findViewById(R.id.textView3);

        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Sets up a full-screen overlay with hidden system navigation
        // for an immersive user experience.
        View overlay = findViewById(R.id.AddPassengerPage);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // Retrieves stored preferences and loads the selected route ID,
        // defaulting to -1 if unavailable.
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int selectedRouteId = prefs.getInt("selected_route", -1);

        // This code checks if a route is set; if not,
        // it disables UI elements and shows a warning message.
        if (selectedRouteId != -1) {
            DestinationInfo.presetSetter(selectedRouteId);
        } else {
            Toast.makeText(this, "Please set a route in Settings first.", Toast.LENGTH_LONG).show();
            pickupDropdown.setEnabled(false);
            dropOffDropdown.setEnabled(false);
            confirmButton.setEnabled(false);
            adultPlusButton.setEnabled(false);
            adultMinusButton.setEnabled(false);
            childPlusButton.setEnabled(false);
            childMinusButton.setEnabled(false);
            studentPlusButton.setEnabled(false);
            studentMinusButton.setEnabled(false);
            seniorpwdPlusButton.setEnabled(false);
            seniorMinusButton.setEnabled(false);

            return;
        }

        // This code sets up a dropdown list for route selection
        // if data is available; otherwise, it disables UI elements and shows a warning.
        if (DestinationInfo.originDestination != null && !DestinationInfo.originDestination.isEmpty()) {
            ArrayAdapter<String> pickupAdapter = new ArrayAdapter<>(
                    this,
                    R.layout.drop_down_list,
                    DestinationInfo.originDestination
            );
            pickupDropdown.setAdapter(pickupAdapter);

            dropOffDropdown.setEnabled(false);

        } else {
            Toast.makeText(this, "Route information not available after setting preset.", Toast.LENGTH_LONG).show();
            pickupDropdown.setEnabled(false);
            dropOffDropdown.setEnabled(false);
            confirmButton.setEnabled(false);
            adultPlusButton.setEnabled(false);
            adultMinusButton.setEnabled(false);
            childPlusButton.setEnabled(false);
            childMinusButton.setEnabled(false);
            studentPlusButton.setEnabled(false);
            studentMinusButton.setEnabled(false);
            seniorpwdPlusButton.setEnabled(false);
            seniorMinusButton.setEnabled(false);
            return;
        }

        // This code initializes dropdowns and buttons,
        // then updates the passenger count and total fare UI.
        setupDropdownListeners();
        setupButtonListeners();
        updatePassengerCountUI();
        updateTotalFareUI();
    }
    // This code formats the total fare as currency and updates the display accordingly.
    private void updateTotalFareUI() {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        totalAmountTextView.setText("Total: ₱" + formatter.format(totalFare));
    }

    // This code checks if a valid route is selected by verifying that
    // both origin and destination exist and are in the correct order.
    private boolean isValidRouteSelected() {
        String selectedOrigin = pickupDropdown.getText().toString();
        String selectedDestination = dropOffDropdown.getText().toString();

        if (DestinationInfo.originDestination == null || DestinationInfo.originDestination.isEmpty()) {
            return false;
        }

        if (selectedOrigin.isEmpty() || selectedDestination.isEmpty()) {
            return false;
        }

        int originIndex = DestinationInfo.originDestination.indexOf(selectedOrigin);
        int destinationIndex = DestinationInfo.originDestination.indexOf(selectedDestination);

        if (originIndex != -1 && destinationIndex != -1 && destinationIndex > originIndex) {
            return true;
        } else {
            return false;
        }
    }

    // This code sets up dropdown listeners to update route selections,
    // populate available drop-off options based on the chosen pickup, and provide user feedback.
    private void setupDropdownListeners() {
        pickupDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPickup = pickupDropdown.getText().toString();
                DestinationInfo.origin = selectedPickup;

                dropOffDropdown.setText("");
                dropOffDropdown.setEnabled(false);

                if (DestinationInfo.originDestination == null || DestinationInfo.originDestination.isEmpty()) {
                    Toast.makeText(AddPassenger.this, "Route data is missing. Please restart the app.", Toast.LENGTH_LONG).show();
                    return;
                }

                int selectedIndex = DestinationInfo.originDestination.indexOf(selectedPickup);

                List<String> dropOffList = new ArrayList<>();
                if (selectedIndex != -1 && selectedIndex < DestinationInfo.originDestination.size() - 1) {
                    dropOffList = DestinationInfo.originDestination.subList(selectedIndex + 1, DestinationInfo.originDestination.size());
                }

                ArrayAdapter<String> dropOffAdapter = new ArrayAdapter<>(
                        AddPassenger.this,
                        R.layout.drop_down_list,
                        dropOffList
                );
                dropOffDropdown.setAdapter(dropOffAdapter);

                if (!dropOffList.isEmpty()) {
                    dropOffDropdown.setEnabled(true);
                } else {
                    Toast.makeText(AddPassenger.this, "No valid drop-off locations from here.", Toast.LENGTH_SHORT).show();
                    dropOffDropdown.setEnabled(false);
                }

                Toast.makeText(AddPassenger.this, "Pickup: " + selectedPickup, Toast.LENGTH_SHORT).show();
            }
        });

        dropOffDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDropOff = dropOffDropdown.getText().toString();
                DestinationInfo.destination = selectedDropOff;

                if (!isValidRouteSelected()) {
                    Toast.makeText(AddPassenger.this, "Invalid drop-off selected for this pickup.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(AddPassenger.this, "Drop-off: " + selectedDropOff, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This code updates the UI to display the current passenger count for each category.
    private void updatePassengerCountUI() {
        adultCountTextView.setText(String.valueOf(adultCount));
        childCountTextView.setText(String.valueOf(childCount));
        studentCountTextView.setText(String.valueOf(studentCount));
        seniorpwdCountTextView.setText(String.valueOf(seniorpwdCount));
    }

    // This code sets up button listeners for confirmation, cancellation as well as adding and removing passengers.
    private void setupButtonListeners() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedPickup = pickupDropdown.getText().toString();
                String selectedDropOff = dropOffDropdown.getText().toString();
                int totalPassengers = adultCount + childCount + studentCount + seniorpwdCount;

                if (selectedPickup.isEmpty() || selectedDropOff.isEmpty() || !isValidRouteSelected()) {
                    Toast.makeText(AddPassenger.this, "Please select a valid Pickup and Drop-off location.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (totalPassengers <= 0) {
                    Toast.makeText(AddPassenger.this, "Please add at least one passenger.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int originIndex = DestinationInfo.originDestination.indexOf(selectedPickup);
                int destinationIndex = DestinationInfo.originDestination.indexOf(selectedDropOff);

                if (originIndex != -1 && destinationIndex != -1 && destinationIndex > originIndex) {

                    int dropOffIndexInRoute = destinationIndex;
                    int currentBusDestinationIndex = RealTimeData.currentDestinationCounter;

                    boolean destinationPassed = false;
                    if (dropOffIndexInRoute < currentBusDestinationIndex) {
                        Toast.makeText(AddPassenger.this, "Warning: Drop-off destination " + selectedDropOff + " has been passed.", Toast.LENGTH_LONG).show();
                        destinationPassed = true;
                    }

                    DestinationInfo destInfo = new DestinationInfo();
                    destInfo.calculatedDistance(originIndex, destinationIndex);
                    double calculatedDistance = destInfo.calculatedKilometers;

                    SharedPreferences prefsKm = getSharedPreferences("Kilometers", MODE_PRIVATE);
                    SharedPreferences.Editor editorKm = prefsKm.edit();
                    editorKm.putFloat(DISTANCE_KEY, (float) calculatedDistance);
                    editorKm.apply();

                    double totalCalculatedDiscount = 0.0;
                    SharedPreferences prefsDiscount = getSharedPreferences("Discount", MODE_PRIVATE);
                    SharedPreferences.Editor editorDiscount = prefsDiscount.edit();
                    editorDiscount.putFloat(DISCOUNT_KEY, (float) TicketInfo.discount);
                    editorDiscount.apply();

                    if (!destinationPassed) {
                        int dropOffRunningNumber = destinationIndex;

                        boolean foundExistingEntry = false;
                        for (RealTimeData.DropOffQueue queueEntry : RealTimeData.dropOffQueue) {
                            if (queueEntry.runningListNumber == dropOffRunningNumber) {
                                queueEntry.passengerDroppingOff += totalPassengers;
                                foundExistingEntry = true;
                                break;
                            }
                        }

                        if (!foundExistingEntry) {
                            RealTimeData.DropOffQueue newDropOff = new RealTimeData.DropOffQueue();
                            newDropOff.runningListNumber = dropOffRunningNumber;
                            newDropOff.currentLocation = selectedDropOff;
                            newDropOff.passengerDroppingOff = totalPassengers;

                            RealTimeData.dropOffQueue.add(newDropOff);
                        }

                        if (!RealTimeData.dropOffQueue.isEmpty()) {
                            RealTimeData.dropOffQueue.get(0).dropOffSorter();
                        }

                        Toast.makeText(AddPassenger.this, totalPassengers + " passenger(s) added to pending list for " + selectedDropOff, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddPassenger.this, "Ticket saved, but drop-off not added to real-time queue.", Toast.LENGTH_SHORT).show(); // Optional: another feedback
                    }

                    RealTimeData.totalBoardedPassengersForTrip += totalPassengers;

                    Intent intent = new Intent(AddPassenger.this, TicketReceipt.class);

                    intent.putExtra("adultCount", adultCount);
                    intent.putExtra("childCount", childCount);
                    intent.putExtra("studentCount", studentCount);
                    intent.putExtra("seniorpwdCount", seniorpwdCount);

                    intent.putExtra("totalFare", totalFare);

                    intent.putExtra("origin", selectedPickup);
                    intent.putExtra("destination", selectedDropOff);

                    startActivity(intent);

                } else {
                    Toast.makeText(AddPassenger.this, "Error processing route data. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPassenger.this, TripOverview.class);
                startActivity(intent);
                finish();
            }
        });

        adultPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidRouteSelected()) {
                    Toast.makeText(AddPassenger.this, "Please select a valid route first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int originIndex = DestinationInfo.originDestination.indexOf(pickupDropdown.getText().toString());
                int destinationIndex = DestinationInfo.originDestination.indexOf(dropOffDropdown.getText().toString());

                if (originIndex != -1 && destinationIndex != -1 && destinationIndex > originIndex) {
                    Passenger newPassenger = new Passenger(originIndex, destinationIndex, 0);

                    passengersList.add(newPassenger);

                    totalFare += newPassenger.getFare();

                    adultCount++;
                    updatePassengerCountUI();
                    updateTotalFareUI();
                } else {
                    Toast.makeText(AddPassenger.this, "Error creating passenger. Route data invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adultMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adultCount > 0) {
                    boolean passengerRemoved = false;

                    for (int i = passengersList.size() - 1; i >= 0; i--) {
                        Passenger p = passengersList.get(i);
                        if (p.getPassengerTypeCode() == 0) {
                            totalFare -= p.getFare();
                            passengersList.remove(i);
                            passengerRemoved = true;
                            break;
                        }
                    }

                    if (passengerRemoved) {
                        adultCount--;
                        updatePassengerCountUI();
                        updateTotalFareUI();
                    }
                }
            }
        });

        childPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (!isValidRouteSelected()) {
                    Toast.makeText(AddPassenger.this, "Please select a valid route first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int originIndex = DestinationInfo.originDestination.indexOf(pickupDropdown.getText().toString());
                int destinationIndex = DestinationInfo.originDestination.indexOf(dropOffDropdown.getText().toString());

                if (originIndex != -1 && destinationIndex != -1 && destinationIndex > originIndex) {
                    Passenger newPassenger = new Passenger(originIndex, destinationIndex, 1);

                    passengersList.add(newPassenger);
                    totalFare += newPassenger.getFare();

                    childCount++;
                    updatePassengerCountUI();
                    updateTotalFareUI();
                }
            }
        });

        childMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childCount > 0) {
                    boolean passengerRemoved = false;

                    for (int i = passengersList.size() - 1; i >= 0; i--) {
                        Passenger p = passengersList.get(i);
                        if (p.getPassengerTypeCode() == 1) {
                            totalFare -= p.getFare();
                            passengersList.remove(i);
                            passengerRemoved = true;
                            break;
                        }
                    }

                    if (passengerRemoved) {
                        childCount--;
                        updatePassengerCountUI();
                        updateTotalFareUI();
                    }
                }
            }
        });

        studentPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (!isValidRouteSelected()) {
                    Toast.makeText(AddPassenger.this, "Please select a valid route first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int originIndex = DestinationInfo.originDestination.indexOf(pickupDropdown.getText().toString());
                int destinationIndex = DestinationInfo.originDestination.indexOf(dropOffDropdown.getText().toString());

                if (originIndex != -1 && destinationIndex != -1 && destinationIndex > originIndex) {
                    Passenger newPassenger = new Passenger(originIndex, destinationIndex, 2);

                    passengersList.add(newPassenger);
                    totalFare += newPassenger.getFare();

                    studentCount++;
                    updatePassengerCountUI();
                    updateTotalFareUI();
                }
            }
        });

        studentMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentCount > 0) {
                    boolean passengerRemoved = false;

                    for (int i = passengersList.size() - 1; i >= 0; i--) {
                        Passenger p = passengersList.get(i);
                        if (p.getPassengerTypeCode() == 2) {
                            totalFare -= p.getFare();
                            passengersList.remove(i);
                            passengerRemoved = true;
                            break;
                        }
                    }
                    if (passengerRemoved) {
                        studentCount--;
                        updatePassengerCountUI();
                        updateTotalFareUI();
                    }
                }
            }
        });

        seniorpwdPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (!isValidRouteSelected()) {
                    Toast.makeText(AddPassenger.this, "Please select a valid route first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int originIndex = DestinationInfo.originDestination.indexOf(pickupDropdown.getText().toString());
                int destinationIndex = DestinationInfo.originDestination.indexOf(dropOffDropdown.getText().toString());

                if (originIndex != -1 && destinationIndex != -1 && destinationIndex > originIndex) {
                    Passenger newPassenger = new Passenger(originIndex, destinationIndex, 3);

                    passengersList.add(newPassenger);
                    totalFare += newPassenger.getFare();

                    seniorpwdCount++;
                    updatePassengerCountUI();
                    updateTotalFareUI();
                }
            }
        });

        seniorMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seniorpwdCount > 0) {
                    boolean passengerRemoved = false;

                    for (int i = passengersList.size() - 1; i >= 0; i--) {
                        Passenger p = passengersList.get(i);
                        if (p.getPassengerTypeCode() == 3) {
                            totalFare -= p.getFare();
                            passengersList.remove(i);
                            passengerRemoved = true;
                            break;
                        }
                    }
                    if (passengerRemoved) {
                        seniorpwdCount--;
                        updatePassengerCountUI();
                        updateTotalFareUI();
                    }
                }
            }
        });
    }
}