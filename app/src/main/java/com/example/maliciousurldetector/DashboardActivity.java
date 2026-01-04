package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    // ===== Scroll Reveal Animation =====
    private NestedScrollView nestedScrollView;
    private final List<View> revealViews = new ArrayList<>();
    private final Map<View, Boolean> revealedMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        // 🔐 Firebase + Google Sign-In setup
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        // 🧭 Toolbar setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView polygon = findViewById(R.id.cyberPolygon);
        Animation glow = AnimationUtils.loadAnimation(this, R.anim.glow_pulse);
        polygon.startAnimation(glow);




        // 🧭 Drawer setup
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this::handleNavigationItemSelected);

        // 👤 Update Drawer Header
        updateUserInfoInDrawer();

        // ===== Card Clicks =====
        findViewById(R.id.scanUrlCard).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        findViewById(R.id.appSafetyCard).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("action", "scan_apps");
            startActivity(intent);
        });

        findViewById(R.id.subscriptionCard).setOnClickListener(v ->
                startActivity(new Intent(this, SubscriptionActivity.class)));

        findViewById(R.id.safeWebsiteCard).setOnClickListener(v ->
                startActivity(new Intent(this, SafeWebsiteActivity.class)));
        findViewById(R.id.browserAnalyticsCard).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ModeSelectionActivity.class));
        });


        // ===== ScrollView setup =====
        nestedScrollView = findViewById(R.id.nestedScrollView);

        // ===== Initialize reveal targets =====
        int[] animatedIds = new int[]{
                R.id.scanUrlCard,
                R.id.appSafetyCard,
                R.id.subscriptionCard,
                R.id.safeWebsiteCard
        };

        for (int id : animatedIds) {
            View v = findViewById(id);
            if (v != null) {
                v.setAlpha(0f);
                v.setTranslationY(120f);
                revealViews.add(v);
                revealedMap.put(v, false);
            }
        }

        // ===== Scroll Animation Handling =====
        if (nestedScrollView != null) {
            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        int direction = scrollY > oldScrollY ? 1 : -1;
                        for (View rv : revealViews) checkAndAnimate(rv, direction);
                    });

            nestedScrollView.post(() -> {
                for (View rv : revealViews) checkAndAnimate(rv, 1);
            });
        }
    }

    // ===== Scroll Animation Logic =====
    private void checkAndAnimate(View view, int scrollDirection) {
        if (view == null) return;
        Boolean revealed = revealedMap.getOrDefault(view, false);

        if (isInViewport(view, nestedScrollView, 0.85f)) {
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

    // ===== Drawer Header Info =====
    private void updateUserInfoInDrawer() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            View headerView = navView.getHeaderView(0);
            TextView userName = headerView.findViewById(R.id.userNameTextView);
            TextView userEmail = headerView.findViewById(R.id.userEmailTextView);
            ImageView profileImage = headerView.findViewById(R.id.profileImageView);

            userName.setText(user.getDisplayName() != null ? user.getDisplayName() : "User");
            userEmail.setText(user.getEmail());
        }
    }

    // ===== Navigation Menu Clicks =====
    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_subscription) {
            startActivity(new Intent(this, SubscriptionActivity.class));
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_privacy) {
            startActivity(new Intent(this, PrivacyPolicyActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutUsActivity.class));
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "🚪 Logging out...", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            if (mGoogleSignInClient != null) {
                mGoogleSignInClient.signOut();
            }
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        drawerLayout.closeDrawers();
        return true;
    }

    // ===== Back Button for Drawer =====
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
