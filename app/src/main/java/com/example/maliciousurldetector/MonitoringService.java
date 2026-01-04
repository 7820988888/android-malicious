package com.example.parentchildmonitor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.maliciousurldetector.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class MonitoringService extends Service {

    private static final String TAG = "MonitoringService";
    private static final String CHANNEL_ID = "monitor_channel_v1";

    private FirebaseFirestore db;
    private String childId;
    private Timer heartbeatTimer;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        childId = FirebaseAuth.getInstance().getUid();
        handler = new Handler();

        createChannel();
        startForeground(1, buildNotification("Monitoring active"));

        startHeartbeat();
        startUsagePolling();
        startClipboardListener();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(CHANNEL_ID, "Monitor", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(ch);
        }
    }

    private Notification buildNotification(String text) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ParentChild Monitor")
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void startHeartbeat() {
        heartbeatTimer = new Timer();
        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (childId == null) return;
                DocumentReference doc = db.collection("children").document(childId);
                doc.update("lastHeartbeat", System.currentTimeMillis()).addOnFailureListener(e -> {
                    doc.set(new java.util.HashMap<String, Object>() {{
                        put("lastHeartbeat", System.currentTimeMillis());
                    }});
                });
            }
        }, 0, 60 * 1000);
    }

    private void startUsagePolling() {
        handler.postDelayed(this::recordForegroundApp, 3000);
    }

    private void recordForegroundApp() {
        try {
            UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long end = System.currentTimeMillis();
            long begin = end - 5000;
            UsageEvents events = usm.queryEvents(begin, end);
            UsageEvents.Event ev = new UsageEvents.Event();
            while (events.hasNextEvent()) {
                events.getNextEvent(ev);
                if (ev.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    String pkg = ev.getPackageName();
                    Log.d(TAG, "FG app: " + pkg);
                    if (childId != null) {
                        db.collection("children").document(childId)
                                .collection("events").add(new java.util.HashMap<String, Object>() {{
                                    put("type", "app_foreground");
                                    put("package", pkg);
                                    put("ts", System.currentTimeMillis());
                                }});
                        db.collection("children").document(childId).update("lastUsedApp", pkg);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "usage err: " + e.getMessage());
        } finally {
            handler.postDelayed(this::recordForegroundApp, 3000);
        }
    }

    private void startClipboardListener() {
        try {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.addPrimaryClipChangedListener(() -> {
                    try {
                        CharSequence txt = cm.getPrimaryClip().getItemAt(0).getText();
                        if (txt != null && childId != null) {
                            db.collection("children").document(childId)
                                    .collection("events").add(new java.util.HashMap<String, Object>() {{
                                        put("type", "clipboard");
                                        put("text", txt.toString());
                                        put("ts", System.currentTimeMillis());
                                    }});
                        }
                    } catch (Exception ex) { }
                });
            }
        } catch (Exception e) { Log.e(TAG, "clip err: " + e.getMessage()); }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (heartbeatTimer != null) heartbeatTimer.cancel();
        handler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }
}
