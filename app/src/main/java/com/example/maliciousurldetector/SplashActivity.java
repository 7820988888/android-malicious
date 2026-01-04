package com.example.maliciousurldetector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Fullscreen mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        VideoView videoView = findViewById(R.id.splashVideo);
        TextView tvAppTitle = findViewById(R.id.tvAppTitle);
        TextView btnSkip = findViewById(R.id.btnSkip);

        tvAppTitle.setVisibility(View.VISIBLE);

        // Skip Button → Go to Onboarding
        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            finish();
        });

        // Video
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vi);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(false);
            videoView.start();
        });

        videoView.setOnCompletionListener(mp -> {
            startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            finish();
        });
    }
}
