package com.example.busticketsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.NumberFormat;
import java.util.Locale;

import ticket.info.*;

public class TripSummary extends AppCompatActivity {

    private TextView terminalNoTextView;
    private TextView driverNameTextView;
    private TextView conductorNameTextView;
    private TextView plateNoTextView;
    private TextView routeInputTextView;
    private TextView totalPassengerInputTextView;
    private TextView totalFareInputTextView;
    private ImageView backIconImageView;
    private static final String TERMINAL_NUMBER = "1012710";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fare);

        // Initializes UI elements for terminal, driver,
        // conductor, plate number, route, passenger count, fare, and back icon.
        terminalNoTextView = findViewById(R.id.terminal_no);
        driverNameTextView = findViewById(R.id.driver_name);
        conductorNameTextView = findViewById(R.id.conductor_name);
        plateNoTextView = findViewById(R.id.plate_no);
        routeInputTextView = findViewById(R.id.routeinput);
        totalPassengerInputTextView = findViewById(R.id.totalpassengerinput);
        totalFareInputTextView = findViewById(R.id.totalfareinput);
        backIconImageView = findViewById(R.id.ticket_backIcon);

        // Displays the trip summary and sets up a back button to return to the trip overview.
        displayTripSummary();
        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripSummary.this, TripOverview.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Retrieves total revenue and passenger count from the intent extras.
        Intent intent = getIntent();
        if (intent != null) {
            double totalRevenue = intent.getDoubleExtra("total_revenue", 0.0);
            int totalPassengers = intent.getIntExtra("total_passengers", 0);
        }
    }

    // Displays trip summary details including terminal number, driver,
    // conductor, plate number, route, passenger count, and total fare.
    private void displayTripSummary() {
        Intent intent = getIntent();
        if (terminalNoTextView != null) {
            terminalNoTextView.setText(TERMINAL_NUMBER);
        }

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String driverName = prefs.getString("driver_name", "N/A");
        String conductorName = prefs.getString("conductor_name", "N/A");
        String plateNumber = prefs.getString("plate_number", "N/A");

        driverNameTextView.setText(driverName);
        conductorNameTextView.setText(conductorName);
        plateNoTextView.setText(plateNumber);

        if (routeInputTextView != null) {
            routeInputTextView.setText(DestinationInfo.presetName != null ? DestinationInfo.presetName : "Route N/A");
        }

        int totalPassengersCarriedInt = intent.getIntExtra("total_passengers", 0);

        String totalPassengersCarriedString = String.valueOf(totalPassengersCarriedInt);
        if (totalPassengersCarriedString.startsWith("-")) {
            totalPassengersCarriedString = totalPassengersCarriedString.substring(1);
        }


        if (totalPassengerInputTextView != null) {
            totalPassengerInputTextView.setText(totalPassengersCarriedString);
        }

        double totalTripRevenue = intent.getDoubleExtra("total_revenue", 0.0);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        if (totalFareInputTextView != null) {
            totalFareInputTextView.setText(currencyFormat.format(totalTripRevenue));
        }
    }

}