package com.example.maliciousurldetector;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class IPQSHelper {

    private static final String TAG = "IPQSHelper";

    public interface ResultCallback {
        void onResult(boolean isMalicious, String details);
        void onError(String error);
    }

    public static void checkUrl(Context context, String rawUrl, ResultCallback callback) {
        try {
            String apiKey = context.getString(R.string.ipqs_api_key);
            rawUrl = rawUrl.trim().toLowerCase();

            if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) {
                rawUrl = "http://" + rawUrl;
            }

            URL parsed = new URL(rawUrl);
            String cleanUrl = (parsed.getProtocol() + "://" + parsed.getHost() + parsed.getPath())
                    .replaceAll("/$", "");

            Log.d(TAG, "🔍 NORMALIZED URL = " + cleanUrl);
            String encoded = URLEncoder.encode(cleanUrl, "UTF-8");

            String endpoint = "https://ipqualityscore.com/api/json/url/" + apiKey + "/" + encoded +
                    "?strictness=0&fast=false&timeout=7&cache_bust=" + System.currentTimeMillis();

            Log.d(TAG, "📡 IPQS REQUEST = " + endpoint);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET, endpoint, null,
                    response -> {
                        try {
                            Log.d(TAG, "📊 FULL IPQS RESPONSE: " + response.toString());

                            // ✅ CHECK API SUCCESS FIRST
                            if (!response.optBoolean("success", true)) {
                                String errorMsg = response.optString("message", "API Error");
                                Log.e(TAG, "IPQS API ERROR: " + errorMsg);
                                callback.onError("IPQS: " + errorMsg);
                                return;
                            }

                            boolean unsafe = response.optBoolean("unsafe", false);
                            boolean phishing = response.optBoolean("phishing", false);
                            boolean malware = response.optBoolean("malware", false);
                            boolean suspicious = response.optBoolean("suspicious", false);
                            boolean impersonation = response.optBoolean("brand_impersonation", false);
                            boolean parking = response.optBoolean("parking", false);
                            boolean spamming = response.optBoolean("spamming", false);
                            boolean risky_tld = response.optBoolean("risky_tld", false);
                            int risk = response.optInt("risk_score", 0);
                            String domainTrust = response.optString("domain_trust", "unknown");
                            String finalUrl = response.optString("final_url", cleanUrl);
                            String category = response.optString("category", "");

                            boolean isMalicious = phishing || malware || impersonation || unsafe ||
                                    suspicious || parking || spamming || risky_tld ||
                                    risk >= 75 || "suspicious".equalsIgnoreCase(domainTrust);

                            String details = String.format(
                                    "risk=%d,unsafe=%s,phishing=%s,suspicious=%s,trust=%s",
                                    risk, unsafe, phishing, suspicious, domainTrust);

                            Log.d(TAG, "✅ RESULT: malicious=" + isMalicious + " | " + details);

                            // ✅ TRIGGER ALARM FOR HIGH RISK
                            if (isMalicious && context != null) {
                                AlarmManager.triggerMaliciousAlert(context, cleanUrl, "🚨 IPQS High Risk: " + details);
                            }

                            callback.onResult(isMalicious, details);

                        } catch (Exception e) {
                            Log.e(TAG, "PARSE ERROR: " + e.getMessage(), e);
                            callback.onError("Parse error: " + e.getMessage());
                        }
                    },
                    error -> {
                        Log.e(TAG, "IPQS ERROR: " + error.toString(), error);
                        callback.onError("IPQS failed: " + error.toString());
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Cache-Control", "no-cache, no-store, must-revalidate");
                    headers.put("Pragma", "no-cache");
                    headers.put("Expires", "0");
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };

            request.setShouldCache(false);
            request.setRetryPolicy(new DefaultRetryPolicy(10_000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(request);

        } catch (Exception e) {
            Log.e(TAG, "FATAL ERROR: " + e.getMessage(), e);
            callback.onError(e.getMessage());
        }
    }
}
