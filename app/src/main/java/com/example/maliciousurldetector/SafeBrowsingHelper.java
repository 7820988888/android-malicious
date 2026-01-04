package com.example.maliciousurldetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SafeBrowsingHelper {
    private static final String TAG = "SafeBrowsingHelper";

    public interface ResultCallback {
        void onResult(boolean isDangerous);
        void onError(String error);
    }

    public static void checkUrl(Context context, String url, ResultCallback callback) {
        String apiKey = context.getString(R.string.google_api_key);
        String endpoint = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + apiKey;

        // clear cached entry
        SharedPreferences prefs = context.getSharedPreferences("ScanPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove(url).apply();

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("client", new JSONObject().put("clientId", "malicious-url-detector").put("clientVersion", "1.0"));

            JSONObject threatInfo = new JSONObject();
            threatInfo.put("threatTypes", new JSONArray().put("MALWARE").put("SOCIAL_ENGINEERING").put("UNWANTED_SOFTWARE"));
            threatInfo.put("platformTypes", new JSONArray().put("ANY_PLATFORM"));
            threatInfo.put("threatEntryTypes", new JSONArray().put("URL"));
            threatInfo.put("threatEntries", new JSONArray().put(new JSONObject().put("url", url)));

            requestBody.put("threatInfo", threatInfo);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    endpoint,
                    requestBody,
                    response -> {
                        boolean isDangerous = response.has("matches");
                        Log.d(TAG, "GSB response: " + response.toString());
                        if (isDangerous) {
                            triggerAllAlerts(context, url);
                        }
                        callback.onResult(isDangerous);
                    },
                    error -> {
                        Log.e(TAG, "GSB API error: " + error.toString(), error);
                        // fallback to VirusTotal URL scan if GSB fails (optional)
                        fallbackToVirusTotal(context, url, callback);
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Cache-Control", "no-cache");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            request.setShouldCache(false);
            VolleySingleton.getInstance(context).addToRequestQueue(request);

        } catch (JSONException e) {
            Log.e(TAG, "GSB JSON error: " + e.getMessage(), e);
            fallbackToVirusTotal(context, url, callback);
        }
    }

    private static void fallbackToVirusTotal(Context context, String url, ResultCallback callback) {
        Log.d(TAG, "Fallback to VirusTotal for URL: " + url);
        VirusTotalApi vt = new VirusTotalApi(context);
        vt.scanUrl(url, new VirusTotalApi.VirusTotalCallback() {
            @Override
            public void onResult(boolean isMalicious, JSONObject result) {
                if (isMalicious) triggerAllAlerts(context, url);
                callback.onResult(isMalicious);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "VirusTotal error: " + errorMessage);
                callback.onError(errorMessage);
            }
        });
    }

    public static void triggerAllAlerts(Context context, String url) {
        NotificationUtils.sendNotification(context, "⚠️ Malicious URL Detected", url);
        AlarmUtils.playAlarm(context); // keep your existing AlarmUtils to play sound
        // vibration handled inside AlarmUtils or add here
    }
}
