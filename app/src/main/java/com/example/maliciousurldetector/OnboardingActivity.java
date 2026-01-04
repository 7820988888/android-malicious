package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.onboardingViewPager);

        // तुमचे 3 ऑनबोर्डिंग इमेज
        int[] images = new int[]{
                R.drawable.onbord1,
                R.drawable.pin,

        };

        adapter = new OnboardingAdapter(this, images);
        viewPager.setAdapter(adapter);
    }

    // "Skip" बटण क्लिक झाल्यावर → LoginActivity
    public void onSkipClicked() {
        startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
        finish();
    }

    // "Next" बटण क्लिक झाल्यावर → पुढचा पेज किंवा LoginActivity
    public void onNextClicked(int position) {
        if (position < adapter.getItemCount() - 1) {
            viewPager.setCurrentItem(position + 1);
        } else {
            startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
            finish();
        }
    }

    public int getPageCount() {
        return adapter != null ? adapter.getItemCount() : 0;
    }
}
