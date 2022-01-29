package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


public class RecipePageTabsAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;

    public RecipePageTabsAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments = new ArrayList<>();
        fragments.add(RecipePageDetailsFragment.newInstance());
        fragments.add(RecipePageIngredientsFragment.newInstance());
        fragments.add(RecipePageInstructionsFragment.newInstance());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
