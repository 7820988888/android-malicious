package com.example.maliciousurldetector;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SubscriptionActivity extends AppCompatActivity {

    private Button selectFreeTrial, selectMonthly, selectPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        ImageView backBtn = findViewById(R.id.customBack);
        backBtn.setOnClickListener(v -> onBackPressed());

        // Match button IDs
        selectFreeTrial = findViewById(R.id.selectFreeTrial);
        selectMonthly = findViewById(R.id.selectMonthly);
        selectPro = findViewById(R.id.selectPro);

        // Load glow pulse animation
        Animation glowPulse = AnimationUtils.loadAnimation(this, R.anim.glow_pulse);

        // Apply animation to all buttons
        selectFreeTrial.startAnimation(glowPulse);
        selectMonthly.startAnimation(glowPulse);
        selectPro.startAnimation(glowPulse);

        // Free Trial
        selectFreeTrial.setOnClickListener(v -> {
            Toast.makeText(this, "Free Trial Activated for 3 Trials", Toast.LENGTH_SHORT).show();

            // Navigate directly to MainActivity
            Intent intent = new Intent(SubscriptionActivity.this, MainActivity.class);
            intent.putExtra("SUBSCRIPTION_TYPE", "Free Trial");
            intent.putExtra("PREMIUM_ACTIVE", true);
            startActivity(intent);
            finish();
        });

        // 1-Month Subscription
        selectMonthly.setOnClickListener(v -> {
            Toast.makeText(this, "Subscribed for 1 Month (₹200)", Toast.LENGTH_SHORT).show();

            // Navigate to PaymentActivity with subscription info
            Intent intent = new Intent(SubscriptionActivity.this, PaymentActivity.class);
            intent.putExtra("SUBSCRIPTION_TYPE", "1-Month Plan");
            intent.putExtra("SUBSCRIPTION_PRICE", 200); // Amount in INR
            startActivity(intent);
        });

        // 3-Month Pro Subscription
        selectPro.setOnClickListener(v -> {
            Toast.makeText(this, "Subscribed for 3 Months (₹350)", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SubscriptionActivity.this, PaymentActivity.class);
            intent.putExtra("SUBSCRIPTION_TYPE", "Pro Plan");
            intent.putExtra("SUBSCRIPTION_PRICE", 350);
            startActivity(intent);
        });
    }
}
