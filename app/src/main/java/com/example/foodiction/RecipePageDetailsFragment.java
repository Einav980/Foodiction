package com.example.foodiction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipePageDetailsFragment extends Fragment {
    TextView recipeDescription;
    TextView recipeMakingDuration;
    TextView recipeCategoriesTextView;
    TextView recipeNameTitle;

    public static RecipePageDetailsFragment newInstance() {
        RecipePageDetailsFragment fragment = new RecipePageDetailsFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recipe_page_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeDescription = getView().findViewById(R.id.recipe_page_recipe_description);
        recipeMakingDuration = getView().findViewById(R.id.recipe_page_recipe_making_duration_value);
        recipeNameTitle = getView().findViewById(R.id.recipe_page_recipe_title);
        recipeCategoriesTextView = getView().findViewById(R.id.recipe_page_categories_value_text);

        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if(extras == null) {
                Toast.makeText(getActivity() , "Error" , Toast.LENGTH_SHORT);
            }
            else {
                Category recipeCategory = extras.getParcelable("recipe_category");
                recipeDescription.setText(extras.getString("recipe_description"));
                recipeMakingDuration.setText(extras.getString("recipe_making_duration"));
                recipeNameTitle.setText(extras.getString("recipe_display_name"));
                recipeCategoriesTextView.setText(recipeCategory.getName());
            }
        }
    }
}