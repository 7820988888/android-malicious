package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ModeSelectionActivity extends AppCompatActivity {

    Button parentModeBtn, childModeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);

        parentModeBtn = findViewById(R.id.parentModeBtn);
        childModeBtn = findViewById(R.id.childModeBtn);

        parentModeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ModeSelectionActivity.this, ParentLoginActivity.class);
            startActivity(intent);
        });

        childModeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ModeSelectionActivity.this, ChildLoginActivity.class);
            startActivity(intent);
        });
    }
}
