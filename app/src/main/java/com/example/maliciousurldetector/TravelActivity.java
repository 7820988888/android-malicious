package com.example.maliciousurldetector;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TravelActivity extends AppCompatActivity {

    // LINKS
    private static final String URL_IRCTC = "https://www.irctc.co.in";
    private static final String URL_REDBUS = "https://www.redbus.in";
    private static final String URL_FLIGHTS = "https://www.makemytrip.com/flights/";
    private static final String URL_TOURISM = "https://www.incredibleindia.org";
    private static final String URL_SHIP = "https://www.cruisebooking.com";
    private static final String URL_CAR = "https://www.uber.com/in";

    // NEW LINKS
    private static final String URL_METRO = "http://www.delhimetrorail.com/";
    private static final String URL_TAXI = "https://www.rapido.bike/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        // Back button
        ImageView back = findViewById(R.id.btnBackTravel);
        back.setOnClickListener(v -> onBackPressed());

        // Main cards
        View trainCard = findViewById(R.id.card_train);
        View flightCard = findViewById(R.id.card_flight);
        View busCard = findViewById(R.id.card_bus);
        View tourismCard = findViewById(R.id.card_tourism);

        View carCard = findViewById(R.id.card_car);
        View shipCard = findViewById(R.id.card_ship);

        // NEW cards
        View metroCard = findViewById(R.id.card_metro);
        View taxiCard = findViewById(R.id.card_taxi);

        // Click controllers
        trainCard.setOnClickListener(v -> openUrl(URL_IRCTC));
        flightCard.setOnClickListener(v -> openUrl(URL_FLIGHTS));
        busCard.setOnClickListener(v -> openUrl(URL_REDBUS));
        tourismCard.setOnClickListener(v -> openUrl(URL_TOURISM));
        carCard.setOnClickListener(v -> openUrl(URL_CAR));
        shipCard.setOnClickListener(v -> openUrl(URL_SHIP));

        // NEW
        metroCard.setOnClickListener(v -> openUrl(URL_METRO));
        taxiCard.setOnClickListener(v -> openUrl(URL_TAXI));
    }

    private void openUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent chromeIntent = new Intent(Intent.ACTION_VIEW, uri);
        chromeIntent.setPackage("com.android.chrome");

        try {
            startActivity(chromeIntent);
        } catch (ActivityNotFoundException e) {
            Intent fallback = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(fallback, "Open with"));
        }
    }
}
