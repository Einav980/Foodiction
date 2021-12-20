package com.example.foodiction;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class AddRecipeStepAdapter extends AbstractFragmentStepAdapter {

    public AddRecipeStepAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }
    @Override
    public Step createStep(int position) {
        switch(position){
            case 0:
                return new RecipeDetailsStep();
            case 1:
                return new RecipeIngredientsStep();
            case 2:
                return new RecipeInstructionsStep();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        switch (position) {
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("Details")
                        .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("Ingredients")
                        .create();
            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("Instructions")
                        .create();
            default:
                return null;
        }
    }
}
