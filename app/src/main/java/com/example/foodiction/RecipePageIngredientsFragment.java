package com.example.foodiction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecipePageIngredientsFragment extends Fragment {


    public RecipePageIngredientsFragment() {
        // Required empty public constructor
    }

    public static RecipePageIngredientsFragment newInstance() {
        RecipePageIngredientsFragment fragment = new RecipePageIngredientsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_page_ingredients, container, false);
    }
}