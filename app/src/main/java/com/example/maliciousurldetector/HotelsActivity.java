package com.example.maliciousurldetector;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * HotelsActivity
 * - Shows a 2-column grid of hotel booking sources.
 * - Attempts to open each URL in Chrome first; falls back to chooser if Chrome isn't installed.
 */
public class HotelsActivity extends AppCompatActivity {

    // URLs for hotel/booking sources
    private static final String URL_BOOKING     = "https://www.booking.com";
    private static final String URL_MMT_HOTEL   = "https://www.makemytrip.com/hotels/";
    private static final String URL_GOIBIBO     = "https://www.goibibo.com/hotels/";
    private static final String URL_AGODA       = "https://www.agoda.com";
    private static final String URL_AIRBNB      = "https://www.airbnb.com";
    private static final String URL_YATRA       = "https://www.yatra.com/hotels";

    private static final String URL_TRAVELOKA   = "https://www.traveloka.com";
    private static final String URL_PRICELINE   = "https://www.priceline.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels); // make sure file name matches

        // Back button
        ImageView back = findViewById(R.id.btnBackHotels);
        back.setOnClickListener(v -> onBackPressed());

        // Hook up click listeners (IDs must match your XML)
        findViewById(R.id.card_booking).setOnClickListener(v -> openUrlInChrome(URL_BOOKING));
        findViewById(R.id.card_makemytrip).setOnClickListener(v -> openUrlInChrome(URL_MMT_HOTEL));
        findViewById(R.id.card_goibibo).setOnClickListener(v -> openUrlInChrome(URL_GOIBIBO));
        findViewById(R.id.card_agoda).setOnClickListener(v -> openUrlInChrome(URL_AGODA));
        findViewById(R.id.card_airbnb).setOnClickListener(v -> openUrlInChrome(URL_AIRBNB));
        findViewById(R.id.card_yatra).setOnClickListener(v -> openUrlInChrome(URL_YATRA));
        findViewById(R.id.card_traveloka).setOnClickListener(v -> openUrlInChrome(URL_TRAVELOKA));
        findViewById(R.id.card_priceline).setOnClickListener(v -> openUrlInChrome(URL_PRICELINE));
    }

    /**
     * Opens a URL in Chrome if available, otherwise opens a chooser letting user pick browser.
     *
     * @param url the website to open
     */
    private void openUrlInChrome(String url) {
        Uri uri = Uri.parse(url);
        Intent chromeIntent = new Intent(Intent.ACTION_VIEW, uri);
        chromeIntent.setPackage("com.android.chrome"); // ensure Chrome opens if available

        try {
            startActivity(chromeIntent);
        } catch (ActivityNotFoundException e) {
            // Chrome not installed — open with chooser so user can pick any browser
            Intent fallback = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(fallback, "Open with"));
        }
    }
}
