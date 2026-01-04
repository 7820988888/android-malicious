package com.example.maliciousurldetector;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications, switchRealtime, switchSafeBrowsing, switchSound;
    private Button btnClearHistory;
    private ImageButton btnBack;
    private TextView txtLanguage; // fixed English text

    private SharedPreferences sharedPreferences;
    private final String PREFS_NAME = "AppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Bind UI
        switchNotifications = findViewById(R.id.switch_notifications);
        switchRealtime = findViewById(R.id.switch_realtime);
        switchSafeBrowsing = findViewById(R.id.switch_safe_browsing);
        switchSound = findViewById(R.id.switch_sound);
        btnClearHistory = findViewById(R.id.btn_clear_history);
        btnBack = findViewById(R.id.btn_back);
        txtLanguage = findViewById(R.id.txt_language); // TextView instead of spinner

        // Back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // Set fixed language
        txtLanguage.setText("English");

        // Save switch states
        switchNotifications.setChecked(sharedPreferences.getBoolean("notifications", true));
        switchRealtime.setChecked(sharedPreferences.getBoolean("realtime", true));
        switchSafeBrowsing.setChecked(sharedPreferences.getBoolean("safebrowsing", true));
        switchSound.setChecked(sharedPreferences.getBoolean("sound", true));

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                sharedPreferences.edit().putBoolean("notifications", isChecked).apply()
        );
        switchRealtime.setOnCheckedChangeListener((buttonView, isChecked) ->
                sharedPreferences.edit().putBoolean("realtime", isChecked).apply()
        );
        switchSafeBrowsing.setOnCheckedChangeListener((buttonView, isChecked) ->
                sharedPreferences.edit().putBoolean("safebrowsing", isChecked).apply()
        );
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) ->
                sharedPreferences.edit().putBoolean("sound", isChecked).apply()
        );

        // Clear history button
        btnClearHistory.setOnClickListener(v -> {
            sharedPreferences.edit().remove("history").apply();
            Toast.makeText(this, "History cleared!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
