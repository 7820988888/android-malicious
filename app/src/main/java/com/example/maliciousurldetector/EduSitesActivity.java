package com.example.maliciousurldetector;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

/**
 * EduSitesActivity
 * - Opens educational websites in Chrome (fallback chooser)
 */
public class EduSitesActivity extends AppCompatActivity {

    private static final String URL_COURsera    = "https://www.coursera.org";
    private static final String URL_EDX        = "https://www.edx.org";
    private static final String URL_NPTEL      = "https://onlinecourses.nptel.ac.in";
    private static final String URL_SWAYAM     = "https://swayam.gov.in";
    private static final String URL_KHAN       = "https://www.khanacademy.org";
    private static final String URL_UDEMY      = "https://www.udemy.com";
    private static final String URL_FUTURELEARN= "https://www.futurelearn.com";
    private static final String URL_LINKEDIN   = "https://www.linkedin.com/learning";
    private static final String URL_MIT_OCW    = "https://ocw.mit.edu";
    private static final String URL_GOOGLE_DIG = "https://learndigital.withgoogle.com/digitalgarage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_sites);

        // back button
        ImageView back = findViewById(R.id.btnBackEdu);
        back.setOnClickListener(v -> onBackPressed());

        // click mappings
        findViewById(R.id.card_coursera).setOnClickListener(v -> openUrlInChrome(URL_COURsera));
        findViewById(R.id.card_edx).setOnClickListener(v -> openUrlInChrome(URL_EDX));
        findViewById(R.id.card_nptel).setOnClickListener(v -> openUrlInChrome(URL_NPTEL));
        findViewById(R.id.card_swayam).setOnClickListener(v -> openUrlInChrome(URL_SWAYAM));
        findViewById(R.id.card_khan).setOnClickListener(v -> openUrlInChrome(URL_KHAN));
        findViewById(R.id.card_udemy).setOnClickListener(v -> openUrlInChrome(URL_UDEMY));
        findViewById(R.id.card_futurelearn).setOnClickListener(v -> openUrlInChrome(URL_FUTURELEARN));
        findViewById(R.id.card_linkedin_learning).setOnClickListener(v -> openUrlInChrome(URL_LINKEDIN));
        findViewById(R.id.card_mit_ocw).setOnClickListener(v -> openUrlInChrome(URL_MIT_OCW));
        findViewById(R.id.card_google_digital).setOnClickListener(v -> openUrlInChrome(URL_GOOGLE_DIG));
    }

    private void openUrlInChrome(String url) {
        Uri uri = Uri.parse(url);
        Intent chromeIntent = new Intent(Intent.ACTION_VIEW, uri);
        chromeIntent.setPackage("com.android.chrome");
        try {
            startActivity(chromeIntent);
        } catch (ActivityNotFoundException e) {
            // Chrome not available -> show chooser
            Intent fallback = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(fallback, "Open with"));
        }
    }
}
