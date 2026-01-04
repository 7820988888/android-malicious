package com.example.maliciousurldetector;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ShoppingSitesActivity extends AppCompatActivity {

    // Package names (Play Store)
    private static final String PKG_AMAZON = "com.amazon.mShop.android.shopping";
    private static final String PKG_FLIPKART = "com.flipkart.android";
    private static final String PKG_MYNTRA = "com.myntra.android";
    private static final String PKG_MEESHO = "com.meesho.supply";
    private static final String PKG_AJIO = "com.ril.ajio";
    private static final String PKG_SNAPDEAL = "com.snapdeal.main";
    private static final String PKG_TATACLIQ = "com.tatacliq.tatacliq";
    private static final String PKG_NYKAA = "com.fsn.nykaa";
    private static final String PKG_JIOMART = "com.jpl.jiomart";
    private static final String PKG_RELIANCE_DIGITAL = "in.digital.reliance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_sites);

        // Back button
        ImageView backBtn = findViewById(R.id.customBack);
        backBtn.setOnClickListener(v -> onBackPressed());

        // CardViews
        CardView cardAmazon = findViewById(R.id.cardAmazon);
        CardView cardFlipkart = findViewById(R.id.cardFlipkart);
        CardView cardMyntra = findViewById(R.id.cardMyntra);
        CardView cardMeesho = findViewById(R.id.cardMeesho);
        CardView cardAjio = findViewById(R.id.cardAjio);
        CardView cardSnapdeal = findViewById(R.id.cardSnapdeal);
        CardView cardTataCliq = findViewById(R.id.cardTataCliq);

        CardView cardJioMart = findViewById(R.id.cardJioMart);
        CardView cardNykaa = findViewById(R.id.cardNykaa);
        CardView cardReliance = findViewById(R.id.cardRelianceDigital);

        // Add Click Listeners
        cardAmazon.setOnClickListener(v -> openAppOrSearch(PKG_AMAZON, "Amazon"));
        cardFlipkart.setOnClickListener(v -> openAppOrSearch(PKG_FLIPKART, "Flipkart"));
        cardMyntra.setOnClickListener(v -> openAppOrSearch(PKG_MYNTRA, "Myntra"));
        cardMeesho.setOnClickListener(v -> openAppOrSearch(PKG_MEESHO, "Meesho"));
        cardAjio.setOnClickListener(v -> openAppOrSearch(PKG_AJIO, "Ajio"));
        cardSnapdeal.setOnClickListener(v -> openAppOrSearch(PKG_SNAPDEAL, "Snapdeal"));
        cardTataCliq.setOnClickListener(v -> openAppOrSearch(PKG_TATACLIQ, "Tata Cliq"));

        cardJioMart.setOnClickListener(v -> openAppOrSearch(PKG_JIOMART, "JioMart"));
        cardNykaa.setOnClickListener(v -> openAppOrSearch(PKG_NYKAA, "Nykaa"));
        cardReliance.setOnClickListener(v -> openAppOrSearch(PKG_RELIANCE_DIGITAL, "Reliance Digital"));
    }

    // Open app or search on Play Store
    private void openAppOrSearch(String packageName, String appName) {
        if (packageName != null && packageName.trim().length() > 2) {
            try {
                Intent goToMarket = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + packageName));
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goToMarket);
                return;
            } catch (ActivityNotFoundException e) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                startActivity(webIntent);
                return;
            }
        }

        // Search fallback
        String searchUrl = "https://play.google.com/store/search?q=" + Uri.encode(appName) + "&c=apps";
        Intent searchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
        startActivity(searchIntent);
    }
}
