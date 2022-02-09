package com.example.foodiction;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddInternetRecipeStepAdapter extends AbstractFragmentStepAdapter {

    List<Step> steps;
    public AddInternetRecipeStepAdapter(FragmentManager fm, Context context) {
        super(fm, context);
        steps = new ArrayList<Step>();
        steps.add(new RecipeDetailsStep());
        steps.add(new RecipeDurationStep());
    }
    @Override
    public Step createStep(int position) {
        return steps.get(position);
    }

    @Override
    public int getCount() {
        return steps.size();
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
                        .setTitle("Duration")
                        .create();
            default:
                return null;
        }
    }
}
