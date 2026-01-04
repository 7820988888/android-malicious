package com.example.maliciousurldetector;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * FoodSitesActivity
 * - Opens the Play Store page for each app if Play Store installed.
 * - Otherwise opens the Play Store web page (browser).
 *
 * Replace package names if you find them outdated.
 */
public class FoodSitesActivity extends AppCompatActivity {

    // Play Store package names (placeholders — update if changed)
    private static final String PKG_ZOMATO    = "com.application.zomato";         // example
    private static final String PKG_SWIGGY    = "in.swiggy.android";
    private static final String PKG_BLINKIT   = "com.grofers.customerapp";       // Blinkit (may be grofers package)
    private static final String PKG_ZEPTO     = "com.zeptoconsumerapp";
    private static final String PKG_DUNZO     = "com.dunzo.user";
    private static final String PKG_BIGBASKET = "com.bigbasket.mobileapp";
    private static final String PKG_INSTAMART = "com.mcdonalds.mobileapp" ; // probably needs update
    private static final String PKG_GROFERS   = "com.grofers.customerapp"; // same as blinkit historically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_sites);

        // Back button in toolbar (id customBack in layout)
        ImageView back = findViewById(R.id.customBack);
        back.setOnClickListener(v -> onBackPressed());

        // Hook clicks - each opens Play Store or web fallback
        findViewById(R.id.cardZomato).setOnClickListener(v -> openPlayStoreApp(PKG_ZOMATO));
        findViewById(R.id.cardSwiggy).setOnClickListener(v -> openPlayStoreApp(PKG_SWIGGY));
        findViewById(R.id.cardBlinkit).setOnClickListener(v -> openPlayStoreApp(PKG_BLINKIT));
        findViewById(R.id.cardZepto).setOnClickListener(v -> openPlayStoreApp(PKG_ZEPTO));
        findViewById(R.id.cardDunzo).setOnClickListener(v -> openPlayStoreApp(PKG_DUNZO));
        findViewById(R.id.cardBigBasket).setOnClickListener(v -> openPlayStoreApp(PKG_BIGBASKET));
        findViewById(R.id.cardInstamart).setOnClickListener(v -> openPlayStoreApp(PKG_INSTAMART));
        findViewById(R.id.cardGrofers).setOnClickListener(v -> openPlayStoreApp(PKG_GROFERS));
    }

    private void openPlayStoreApp(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            // No package provided -> open Play Store search
            openUrlInBrowser("https://play.google.com/store/search?q=" + Uri.encode("food delivery apps") + "&c=apps");
            return;
        }

        try {
            // Try Play Store app
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.setPackage("com.android.vending"); // enforce Play Store
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            // Play Store app missing -> fallback to the web link
            openUrlInBrowser("https://play.google.com/store/apps/details?id=" + packageName);
        }
    }

    private void openUrlInBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(webIntent);
    }
}
