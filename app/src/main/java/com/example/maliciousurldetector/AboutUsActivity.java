package com.example.maliciousurldetector;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView backButton;
    private NestedScrollView scrollView;
    private LinearLayout contentLayout;

    private final List<View> revealViews = new ArrayList<>();
    private final Map<View, Boolean> revealedMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // 🔹 Initialize Views
        scrollView = findViewById(R.id.scrollView);
        contentLayout = findViewById(R.id.contentLayout);
        backButton = findViewById(R.id.customBack);

        // 🔙 Custom Back Button
        backButton.setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // 🔹 Add all text sections in the reveal list
        for (int i = 0; i < contentLayout.getChildCount(); i++) {
            View child = contentLayout.getChildAt(i);
            child.setAlpha(0f);
            child.setTranslationY(120f);
            revealViews.add(child);
            revealedMap.put(child, false);
        }

        // 🔹 Scroll-based Animation (same logic as Dashboard)
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    int direction = scrollY > oldScrollY ? 1 : -1;
                    for (View rv : revealViews) checkAndAnimate(rv, direction);
                });

        // Trigger initial animation for visible views
        scrollView.post(() -> {
            for (View rv : revealViews) checkAndAnimate(rv, 1);
        });
    }

    // ===== Scroll Animation Logic =====
    private void checkAndAnimate(View view, int scrollDirection) {
        if (view == null) return;
        Boolean revealed = revealedMap.getOrDefault(view, false);

        if (isInViewport(view, scrollView, 0.9f)) {
            if (!revealed) {
                float startY = scrollDirection > 0 ? 120f : -120f;
                view.setTranslationY(startY);
                view.setAlpha(0f);
                view.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setDuration(700)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
                revealedMap.put(view, true);
            }
        } else if (revealed) {
            float toY = scrollDirection > 0 ? -60f : 60f;
            view.animate()
                    .translationY(toY)
                    .alpha(0f)
                    .setDuration(300)
                    .setInterpolator(new AccelerateInterpolator())
                    .start();
            revealedMap.put(view, false);
        }
    }

    // ===== Check if a view is visible in the viewport =====
    private boolean isInViewport(View view, NestedScrollView parent, float triggerPct) {
        if (view == null || parent == null) return false;
        int[] parentLoc = new int[2];
        parent.getLocationOnScreen(parentLoc);
        int parentTop = parentLoc[1];
        int parentHeight = parent.getHeight();
        int triggerY = parentTop + (int) (parentHeight * triggerPct);

        int[] viewLoc = new int[2];
        view.getLocationOnScreen(viewLoc);
        int viewTop = viewLoc[1];
        int viewBottom = viewTop + view.getHeight();

        return (viewTop <= triggerY) && (viewBottom >= parentTop);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
