package com.example.foodiction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class RecipeIngredientsStep extends Fragment implements Step {
    final int INGREDIENT_LIST_REQUEST_CODE = 1;
    static RecyclerView addedIngredientsRecyclerView;
    static RecyclerView.Adapter adapter;
    static ArrayList<Ingredient> addedIngredients;
    static TextView noIngredientsTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(savedInstanceState != null){
            addedIngredients = savedInstanceState.getParcelableArrayList("SavedIngredients");
            Log.i("Foodciction", "Ingredients Loaded!");
        }
        return inflater.inflate(R.layout.fragment_recipe_ingredients_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addedIngredients = new ArrayList<>();
        addedIngredientsRecyclerView = getView().findViewById(R.id.addedIngredientsRecyclerView);
        adapter = new AddedIngredientListAdapter(addedIngredients,getContext(),MainActivity.GlobalMode.EDIT);
        noIngredientsTextView = getView().findViewById(R.id.no_ingredients_textview);
        addedIngredientsRecyclerView.setAdapter(adapter);
        addedIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fab = view.findViewById(R.id.add_ingredient_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredient(view);
            }
        });
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == IngredientsListActivity.RESULT_OK)
        {
            Ingredient returnedIngredient = data.getParcelableExtra("ingredientObject");
            addedIngredients.add(returnedIngredient);
            AddRecipeActivity.currentCreatedRecipe.setIngredients(addedIngredients);
            addedIngredientsRecyclerView.setAdapter(adapter);
        }

        if(addedIngredients.size() == 0){
            noIngredientsTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noIngredientsTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void addIngredient(View v){
        Intent ingredientsListActivity = new Intent(getContext(), IngredientsListActivity.class);
        startActivityForResult(ingredientsListActivity, INGREDIENT_LIST_REQUEST_CODE);
    }

    public static void removeIngredient(int position){
        addedIngredients.remove(position);
        addedIngredientsRecyclerView.setAdapter(adapter);
        if(addedIngredients.size() == 0){
            noIngredientsTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noIngredientsTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("SavedIngredients", addedIngredients);
        super.onSaveInstanceState(outState);
        Log.i("Foodiction", "Instance saved");
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            addedIngredients = savedInstanceState.getParcelableArrayList("SavedIngredients");
            Log.i("Foodiction", "Instance Loaded");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        {
            addedIngredients = savedInstanceState.getParcelableArrayList("SavedIngredients");
            addedIngredientsRecyclerView.setAdapter(adapter);
            Log.i("Foodiction", "Instance Loaded");
        }
    }
}