package com.example.maliciousurldetector;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

public class AlarmManager {
    private static final String TAG = "AlarmManager";

    public static void triggerMaliciousAlert(Context context, String url, String reason) {
        Log.d(TAG, "🚨 TRIGGERING MALICIOUS ALERT: " + reason);

        // ✅ SIREN SOUND
        playSiren(context);

        // ✅ SOS VIBRATION
        vibrateSOS(context);
    }

    private static void playSiren(Context context) {
        try {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.siren);
            if (mp != null) {
                mp.setOnCompletionListener(MediaPlayer::release);
                mp.start();
                Log.d(TAG, "🔊 SIREN PLAYING");
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ SIREN FAILED", e);
        }
    }

    private static void vibrateSOS(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null || !vibrator.hasVibrator()) return;

        // ✅ SOS PATTERN: [500ms ON][200ms OFF][500ms ON][200ms OFF][1000ms ON]
        long[] sosPattern = {0, 500, 200, 500, 200, 1000};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(sosPattern, -1));
        } else {
            vibrator.vibrate(sosPattern, -1);
        }
        Log.d(TAG, "📳 SOS VIBRATION STARTED");
    }
}
