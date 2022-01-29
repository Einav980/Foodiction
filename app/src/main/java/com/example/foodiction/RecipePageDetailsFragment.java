package com.example.foodiction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipePageDetailsFragment extends Fragment {
    TextView recipeDescription;
    TextView recipeMakingDuration;
    TextView recipeCategories;

    public static RecipePageDetailsFragment newInstance() {
        RecipePageDetailsFragment fragment = new RecipePageDetailsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recipe_page_details, container, false);
    }
}