package com.example.maliciousurldetector;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 🔹 Custom Back Button
        ImageView backButton = findViewById(R.id.customBack);
        backButton.setOnClickListener(v -> onBackPressed());

        historyListView = findViewById(R.id.historyListView);

        String[] scanHistory = {
                "https://malicious-site.com - ⚠️ Blocked",
                "https://safe-website.com - ✅ Safe",
                "https://fake-login.net - ⚠️ Blocked"
        };

        // ✅ Adapter with WHITE text
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                scanHistory
        ) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);          // 🔥 WHITE TEXT
                text.setTextSize(16);                    // Optional
                return view;
            }
        };

        historyListView.setAdapter(adapter);
    }
}
