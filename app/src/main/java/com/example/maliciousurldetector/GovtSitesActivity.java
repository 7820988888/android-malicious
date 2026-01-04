package com.example.maliciousurldetector;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GovtSitesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_sites);

        // BACK BUTTON
        ImageView backBtn = findViewById(R.id.btnBackGov);
        backBtn.setOnClickListener(v -> onBackPressed());

        // CLICK EVENTS FOR ALL GOVT SERVICES
        findViewById(R.id.imgUidai).setOnClickListener(v ->
                openUrl("https://uidai.gov.in"));

        findViewById(R.id.imgEpfo).setOnClickListener(v ->
                openUrl("https://www.epfindia.gov.in"));

        findViewById(R.id.imgIncomeTax).setOnClickListener(v ->
                openUrl("https://www.incometax.gov.in"));

        findViewById(R.id.imgDigilocker).setOnClickListener(v ->
                openUrl("https://www.digilocker.gov.in"));

        findViewById(R.id.imgUmang).setOnClickListener(v ->
                openUrl("https://web.umang.gov.in"));

        findViewById(R.id.imgCowin).setOnClickListener(v ->
                openUrl("https://www.cowin.gov.in"));

        findViewById(R.id.imgPassport).setOnClickListener(v ->
                openUrl("https://www.passportindia.gov.in"));

        findViewById(R.id.imgPmKisan).setOnClickListener(v ->
                openUrl("https://pmkisan.gov.in"));
    }

    private void openUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent chromeIntent = new Intent(Intent.ACTION_VIEW, uri);
        chromeIntent.setPackage("com.android.chrome");

        try {
            startActivity(chromeIntent);
        } catch (ActivityNotFoundException e) {
            // Chrome not installed -> open in any browser
            Intent fallback = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(fallback);
        }
    }
}
