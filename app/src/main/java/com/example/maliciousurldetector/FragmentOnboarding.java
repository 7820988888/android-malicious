package com.example.maliciousurldetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentOnboarding extends Fragment {

    private static final String ARG_IMAGE = "arg_image";
    private static final String ARG_POSITION = "arg_position";

    public static FragmentOnboarding newInstance(int imageRes, int position) {
        FragmentOnboarding fragment = new FragmentOnboarding();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, imageRes);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    private int imageRes, position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageRes = getArguments().getInt(ARG_IMAGE);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.item_onboarding, container, false);

        ImageView image = v.findViewById(R.id.imageOnboarding);
        TextView title = v.findViewById(R.id.textTitle);
        TextView desc = v.findViewById(R.id.textDescription);

        Button btnSkip = v.findViewById(R.id.btnSkip);
        Button btnNext = v.findViewById(R.id.btnNext);
        Button btnGetStarted = v.findViewById(R.id.btnGetStarted);

        LinearLayout layoutFeatures = v.findViewById(R.id.layoutFeatures);

        // Load Animation
        Animation glow = AnimationUtils.loadAnimation(getContext(), R.anim.glow_pulse);

        image.setImageResource(imageRes);

        // ---- TITLE + DESCRIPTION ----
        if (position == 0) {
            title.setText("WELCOME TO SAFELINK");
            desc.setText("Secure your device instantly.");

            // Apply animation to NEXT button only on Page 1
            btnNext.startAnimation(glow);

        } else {
            title.setText("SMART PROTECTIONS");
            desc.setText("Real-time detection keeps you safe.");

            // Apply animation to GET STARTED button only on Page 2
            btnGetStarted.startAnimation(glow);
        }

        // ⭐ PAGE 1 → No features
        if (position == 0) {
            layoutFeatures.setVisibility(View.GONE);
            btnSkip.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnGetStarted.setVisibility(View.GONE);
        }

        // ⭐ PAGE 2 → Show features + Get Started
        else {
            layoutFeatures.setVisibility(View.VISIBLE);

            btnSkip.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
            btnGetStarted.setVisibility(View.VISIBLE);
        }

        // BUTTON ACTIONS
        btnSkip.setOnClickListener(v1 ->
                startActivity(new Intent(getActivity(), LoginActivity.class)));

        btnNext.setOnClickListener(v1 -> {
            if (getActivity() instanceof OnboardingActivity) {
                ((OnboardingActivity) getActivity()).onNextClicked(position);
            }
        });

        btnGetStarted.setOnClickListener(v1 ->
                startActivity(new Intent(getActivity(), LoginActivity.class)));

        return v;
    }
}
