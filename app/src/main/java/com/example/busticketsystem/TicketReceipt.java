package com.example.busticketsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import ticket.info.*;

public class TicketReceipt extends AppCompatActivity {
    private static final String DISCOUNT_KEY = "calculated_discount";
    private static final String DISTANCE_KEY = "calculated_distance";
    private static final String PREFS_NAME = "AppPrefs";
    private static final String LAST_TICKET_NUMBER_KEY = "lastTicketNumber";
    private static final String TERMINAL_NUMBER = "1012710";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ticket_receipt);

        // This code ensures a full-screen immersive UI for the ticket receipt page,
        // hiding the navigation bar for a smoother experience.
        View overlay = findViewById(R.id.TicketReceiptPage);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // This code sets up a back button on the ticket receipt page that adds
        // the fare amount to the total revenue before navigating to the trip overview screen.
        ImageView back_icon = findViewById(R.id.ticket_backIcon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalFareForThisTicket = getIntent().getDoubleExtra("totalFare", 0.0);
                Fare.addRevenue(totalFareForThisTicket);
                Intent intent = new Intent(TicketReceipt.this, TripOverview.class);
                startActivity(intent);
                finish();
            }
        });

        // This code extracts trip-related data from the intent,
        // including passenger counts, route details, total fare, distance,
        // and discount amounts, ensuring the necessary information is retrieved
        // for processing or display.
        Intent intent = getIntent();
        int adultCount = intent.getIntExtra("adultCount", 0);
        int childCount = intent.getIntExtra("childCount", 0);
        int studentCount = intent.getIntExtra("studentCount", 0);
        int seniorpwdCount = intent.getIntExtra("seniorpwdCount", 0);
        String origin = intent.getStringExtra("origin");
        String destination = intent.getStringExtra("destination");
        double totalFare = intent.getDoubleExtra("totalFare", 0.0);
        double distanceKm = intent.getDoubleExtra("distance", 0.0);

        double childDiscountAmount = intent.getDoubleExtra("childDiscountAmount", 0.0);
        double studentDiscountAmount = intent.getDoubleExtra("studentDiscountAmount", 0.0);
        double seniorpwdDiscountAmount = intent.getDoubleExtra("seniorpwdDiscountAmount", 0.0);

        // This code initializes multiple `TextView` elements to display key ticket information,
        // such as terminal number, ticket number, trip details, and fare breakdown. It looks well-structured for presenting receipt data dynamically.
        TextView terminalNoTextView = findViewById(R.id.terminal_no);
        TextView ticketNoTextView = findViewById(R.id.ticket_no);
        TextView dateTimeTextView = findViewById(R.id.date_time);
        TextView driverNameTextView = findViewById(R.id.driver_name);
        TextView conductorNameTextView = findViewById(R.id.conductor_name);
        TextView plateNoTextView = findViewById(R.id.plate_no);
        TextView destination1TextView = findViewById(R.id.destination1);
        TextView destination2TextView = findViewById(R.id.destination2);
        TextView amountNoTextView = findViewById(R.id.kilometers_no);
        TextView discountNoTextView = findViewById(R.id.discount_no);
        TextView totalNoTextView = findViewById(R.id.total_no);

        // This code populates various `TextView` elements with essential ticket details,
        // including terminal number, ticket number, date and time, driver and conductor names,
        // plate number, route details, and distance traveled. Additionally, it retrieves and
        // displays any saved discount amounts from SharedPreferences.
        terminalNoTextView.setText(TERMINAL_NUMBER);
        String ticketNumber = generateTicketNumber();
        ticketNoTextView.setText(ticketNumber);

        String currentDateTime = getCurrentDateTime();
        dateTimeTextView.setText(currentDateTime);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String driverName = prefs.getString("driver_name", "N/A");
        String conductorName = prefs.getString("conductor_name", "N/A");
        String plateNumber = prefs.getString("plate_number", "N/A");

        driverNameTextView.setText(driverName);
        conductorNameTextView.setText(conductorName);
        plateNoTextView.setText(plateNumber);

        destination1TextView.setText(origin);
        destination2TextView.setText(destination);

        SharedPreferences prefs2 = getSharedPreferences("Kilometers", MODE_PRIVATE);
        float savedDistance = prefs2.getFloat(DISTANCE_KEY, 0.0f);
        String kilometersText = String.valueOf(savedDistance);
        amountNoTextView.setText(kilometersText + " km");

        StringBuilder discountTypesText = new StringBuilder();
        double totalDiscountAmount = 0.0;

        SharedPreferences prefs3 = getSharedPreferences("Discount", MODE_PRIVATE);
        float savedDiscountAmount = prefs3.getFloat(DISCOUNT_KEY, 0.0f);

        // This code dynamically constructs the discount label based on the number of child,
        // student, and senior/PWD passengers. If no discounts apply, it simply displays "None."
        // Otherwise, it lists applicable discounts along with the saved discount amount in Philippine pesos.
        if (childCount > 0) {
            discountTypesText.append("CHILD /");
        }
        if (studentCount > 0) {
            if (discountTypesText.length() > 0) discountTypesText.append(" ");
            discountTypesText.append("STUDENT /");
        }
        if (seniorpwdCount > 0) {
            if (discountTypesText.length() > 0) discountTypesText.append(" ");
            discountTypesText.append("SENIOR/PWD /");
        }

        if (discountTypesText.length() == 0) {
            discountNoTextView.setText("None");
        } else {
            discountNoTextView.setText(discountTypesText.toString() + " ₱" + savedDiscountAmount);
        }

        // This code formats the total fare using `DecimalFormat` to display it with proper
        // comma separation and two decimal places, ensuring a clean presentation.
        // Additionally, it applies window insets dynamically to handle system bars and
        // maintain UI consistency across different screen layouts.
        DecimalFormat totalFormatter = new DecimalFormat("#,##0.00");
        totalNoTextView.setText(totalFormatter.format(totalFare));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TicketReceiptPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // This code generates a unique ticket number by retrieving the last saved ticket number
    // from SharedPreferences, incrementing it, and formatting it using `DecimalFormat`.
    // It ensures sequential ticket numbering and persistent storage.
    private String generateTicketNumber() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int lastTicketNumber = prefs.getInt(LAST_TICKET_NUMBER_KEY, 0);
        int newTicketNumber = lastTicketNumber + 1;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LAST_TICKET_NUMBER_KEY, newTicketNumber);
        editor.apply();

        DecimalFormat formatter = new DecimalFormat("000");
        return "445820-00000000-" + formatter.format(newTicketNumber);
    }

    // This code formats the current date and time into a human-readable string,
    // displaying the day of the week, full month name, day, year, and time in hours,
    // minutes, and seconds.
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd, yyyy: HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}