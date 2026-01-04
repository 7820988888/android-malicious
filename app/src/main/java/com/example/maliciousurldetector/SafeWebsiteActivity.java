package com.example.maliciousurldetector;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SafeWebsiteActivity extends AppCompatActivity {

    private static final String URL_GOV = "https://www.india.gov.in";
    private static final String URL_PLAY_STORE = "https://play.google.com/store";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_website);

        ImageView backBtn = findViewById(R.id.customBack);
        backBtn.setOnClickListener(v -> onBackPressed());

        // Cards
        View travelling = findViewById(R.id.item_travelling);
        View hotels     = findViewById(R.id.item_hotels);
        View shopping   = findViewById(R.id.item_shopping);
        View food       = findViewById(R.id.item_food);
        View gov        = findViewById(R.id.item_gov);
        View edu        = findViewById(R.id.item_edu);
        View secureAppCard = findViewById(R.id.item_secure_app);

        // Travel
        travelling.setOnClickListener(v -> {
            startActivity(new Intent(this, TravelActivity.class));
        });

        // Hotels
        hotels.setOnClickListener(v -> {
            startActivity(new Intent(this, HotelsActivity.class));
        });

        // Shopping
        shopping.setOnClickListener(v -> {
            startActivity(new Intent(this, ShoppingSitesActivity.class));
        });

        // Food Sites
        food.setOnClickListener(v -> {
            startActivity(new Intent(this, FoodSitesActivity.class));
        });

        // Government Related Sites → GovtSitesActivity
        gov.setOnClickListener(v -> {
            startActivity(new Intent(this, GovtSitesActivity.class));
        });

        // Education Sites → EduSitesActivity
        edu.setOnClickListener(v -> {
            startActivity(new Intent(this, EduSitesActivity.class));
        });

        // Secure App Play Store Card
        if (secureAppCard != null) {
            secureAppCard.setOnClickListener(v -> openPlayStore());
        }
    }

    // OPEN URL IN CHROME
    private void openUrlInChrome(String url) {
        Uri uri = Uri.parse(url);
        Intent chromeIntent = new Intent(Intent.ACTION_VIEW, uri);
        chromeIntent.setPackage("com.android.chrome");

        try {
            startActivity(chromeIntent);
        } catch (ActivityNotFoundException e) {
            // Chrome not installed → open in any browser
            startActivity(Intent.createChooser(
                    new Intent(Intent.ACTION_VIEW, uri),
                    "Open with"
            ));
        }
    }

    // OPEN PLAY STORE SAFELY
    private void openPlayStore() {
        Uri uri = Uri.parse("market://details?id=com.example");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.setPackage("com.android.vending");

        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            // If Play Store not available → open web version
            openUrlInChrome("\"https://play.google.com/store\"");
        }
    }
}
