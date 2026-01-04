package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BrowserAnalyticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_analytics);

        Button browserAnalyticsBtn = findViewById(R.id.browserAnalyticsBtn);

        browserAnalyticsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(BrowserAnalyticsActivity.this, ModeSelectionActivity.class);
            startActivity(intent);
        });
    }
}
