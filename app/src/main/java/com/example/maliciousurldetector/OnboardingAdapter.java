package com.example.maliciousurldetector;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OnboardingAdapter extends FragmentStateAdapter {

    private final int[] images;

    public OnboardingAdapter(@NonNull FragmentActivity fa, int[] images) {
        super(fa);
        this.images = images;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FragmentOnboarding.newInstance(images[position], position);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}
