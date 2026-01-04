package com.example.maliciousurldetector;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";
    private static final long COOLDOWN_PERIOD = 10000; // 10 sec
    private static long lastDetectionTime = 0;
    private final HashSet<String> processedUrls = new HashSet<>();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        CharSequence cs = extras.getCharSequence("android.text");
        String text = (cs != null) ? cs.toString() : null;

        if (text != null) {
            String url = extractUrl(text);
            if (url != null && !processedUrls.contains(url)) {
                processedUrls.add(url);
                checkMaliciousUrl(url);
            }
        }
    }

    private String extractUrl(String text) {
        Pattern p = Pattern.compile("(https?:\\/\\/|www\\.)[a-zA-Z0-9\\-\\.]+\\.[a-z]{2,6}(:\\d{1,5})?(\\/[^\\s]*)?", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.find() ? m.group() : null;
    }

    private void checkMaliciousUrl(String url) {
        long now = System.currentTimeMillis();
        if (now - lastDetectionTime < COOLDOWN_PERIOD) return;
        lastDetectionTime = now;

        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://" + url;

        Log.d(TAG, "🚀 Starting IPQS scan for: " + url);

        String finalUrl = url;
        IPQSHelper.checkUrl(getApplicationContext(), url, new IPQSHelper.ResultCallback() {
            @Override
            public void onResult(boolean isMalicious, String details) {
                Log.d(TAG, "✅ IPQS SCAN COMPLETE: " + isMalicious + " | DETAILS: " + details);

                if (isMalicious) {
                    String alertTitle = "⚠️ Malicious URL (IPQS)";
                    String alertText = finalUrl + "\nDetails: " + details;

                    // ✅ SIREN + VIBRATE ALERT
                    AlarmManager.triggerMaliciousAlert(getApplicationContext(), finalUrl, alertTitle);

                    NotificationUtils.sendNotification(getApplicationContext(), alertTitle, alertText);
                    showToast("🚨 MALICIOUS (IPQS): " + details);
                } else {
                    Log.d(TAG, "🔄 IPQS clean, checking GSB...");
                    SafeBrowsingHelper.checkUrl(getApplicationContext(), finalUrl, new SafeBrowsingHelper.ResultCallback() {
                        @Override
                        public void onResult(boolean isDangerous) {
                            if (isDangerous) {
                                String alertTitle = "⚠️ Malicious URL (GSB)";

                                // ✅ SIREN + VIBRATE ALERT
                                AlarmManager.triggerMaliciousAlert(getApplicationContext(), finalUrl, alertTitle);

                                NotificationUtils.sendNotification(getApplicationContext(), alertTitle, finalUrl);
                                showToast("🚨 MALICIOUS (GSB): " + finalUrl);
                            } else {
                                showToast("✅ SAFE URL: " + finalUrl);
                                Log.d(TAG, "✅ Both IPQS & GSB: SAFE");
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG, "GSB error: " + error);
                            showToast("✅ URL check complete: " + finalUrl);
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "❌ IPQS ERROR: " + error);
                SafeBrowsingHelper.checkUrl(getApplicationContext(), finalUrl, new SafeBrowsingHelper.ResultCallback() {
                    @Override
                    public void onResult(boolean isDangerous) {
                        if (isDangerous) {
                            String alertTitle = "⚠️ Malicious URL (GSB Fallback)";

                            // ✅ SIREN + VIBRATE ALERT
                            AlarmManager.triggerMaliciousAlert(getApplicationContext(), finalUrl, alertTitle);

                            NotificationUtils.sendNotification(getApplicationContext(), alertTitle, finalUrl);
                            showToast("🚨 MALICIOUS (GSB): " + finalUrl);
                        } else {
                            showToast("✅ SAFE URL (GSB): " + finalUrl);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "GSB fallback error: " + error);
                        showToast("ℹ️ Scan unavailable: " + finalUrl);
                    }
                });
            }
        });
    }

    private void showToast(final String msg) {
        new Handler(getMainLooper()).post(() ->
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show());
    }
}
