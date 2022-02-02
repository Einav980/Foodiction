package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class RecipePageTabsAdapter extends FragmentStateAdapter {

    public RecipePageTabsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return RecipePageDetailsFragment.newInstance();
            case 1:
                return RecipePageIngredientsFragment.newInstance();
            case 2:
                return RecipePageInstructionsFragment.newInstance();
        }
        return RecipePageDetailsFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
