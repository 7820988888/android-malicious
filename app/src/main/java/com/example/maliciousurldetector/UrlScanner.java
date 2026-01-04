package com.example.maliciousurldetector;

import android.content.Context;
import org.json.JSONObject;

public class UrlScanner {

    public interface ScanCallback {
        void onResult(boolean isMalicious, String source);
        void onError(String error);
    }

    public static void scan(Context context, String url, ScanCallback callback) {

        // 1️⃣ FIRST: IPQS
        IPQSHelper.checkUrl(context, url, new IPQSHelper.ResultCallback() {
            @Override
            public void onResult(boolean ipqsDanger, String details) {
                if (ipqsDanger) {
                    callback.onResult(true, "IPQS");
                    return;
                }

                // 2️⃣ SECOND: Google Safe Browsing
                SafeBrowsingHelper.checkUrl(context, url, new SafeBrowsingHelper.ResultCallback() {
                    @Override
                    public void onResult(boolean gsbDanger) {
                        if (gsbDanger) {
                            callback.onResult(true, "Google Safe Browsing");
                            return;
                        }

                        // 3️⃣ THIRD: VirusTotal
                        VirusTotalApi vt = new VirusTotalApi(context);
                        vt.scanUrl(url, new VirusTotalApi.VirusTotalCallback() {
                            @Override
                            public void onResult(boolean vtDanger, JSONObject fullResult) {
                                if (vtDanger) {
                                    callback.onResult(true, "VirusTotal");
                                } else {
                                    callback.onResult(false, "SAFE");
                                }
                            }

                            @Override
                            public void onError(String errorMessage) {
                                callback.onError("VirusTotal Error: " + errorMessage);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        callback.onError("Google Safe Browsing Error: " + error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                // If IPQS fails → continue to GSB
                SafeBrowsingHelper.checkUrl(context, url, new SafeBrowsingHelper.ResultCallback() {
                    @Override
                    public void onResult(boolean gsbDanger) {
                        if (gsbDanger) {
                            callback.onResult(true, "Google Safe Browsing");
                        } else {
                            // Continue to VirusTotal
                            VirusTotalApi vt = new VirusTotalApi(context);
                            vt.scanUrl(url, new VirusTotalApi.VirusTotalCallback() {
                                @Override
                                public void onResult(boolean vtDanger, JSONObject fullResult) {
                                    if (vtDanger) {
                                        callback.onResult(true, "VirusTotal");
                                    } else {
                                        callback.onResult(false, "SAFE");
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    callback.onError("VirusTotal Error: " + errorMessage);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String error) {
                        callback.onError("GSB Error: " + error);
                    }
                });
            }
        });
    }
}




