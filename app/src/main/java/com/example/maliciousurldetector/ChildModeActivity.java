package com.example.maliciousurldetector;

import android.app.AppOpsManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maliciousurldetector.R;

public class ChildModeActivity extends AppCompatActivity {

    Button startMonitorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_mode);

        startMonitorBtn = findViewById(R.id.linkBtn); // reuse linkBtn id

        startMonitorBtn.setOnClickListener(v -> {
            if (!hasUsageStatsPermission()) {
                requestUsageAccess();
                return;
            }
            Intent svc = new Intent(this, com.example.parentchildmonitor.MonitoringService.class);
            startForegroundService(svc);
            Toast.makeText(this, "Monitoring started", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean hasUsageStatsPermission() {
        try {
            AppOpsManager appOps = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            return false;
        }
    }

    private void requestUsageAccess() {
        Toast.makeText(this, "Please enable Usage Access for this app", Toast.LENGTH_LONG).show();
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }
}
