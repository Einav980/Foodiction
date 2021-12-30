package com.example.foodiction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class RecipeIngredientsStep extends Fragment implements Step {
    final int INGREDIENT_LIST_REQUEST_CODE = 1;
    static ListView addedIngredientsListView;
    static AddedIngredientListAdapter adapter;
    static ArrayList<Ingredient> addedIngredients;
    static TextView noIngredientsTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_ingredients_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addedIngredients = new ArrayList<>();
        addedIngredientsListView = getView().findViewById(R.id.addedIngredientsListView);
        adapter = new AddedIngredientListAdapter(getContext(), addedIngredients, MainActivity.GlobalMode.EDIT);
        addedIngredientsListView.setAdapter(adapter);
        noIngredientsTextView = getView().findViewById(R.id.noIngredientsTextView);

        FloatingActionButton fab = view.findViewById(R.id.addIngredientButton);
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
            String name = data.getStringExtra("ingredientName");
            String imageUrl = data.getStringExtra("ingredientImage");
            String amount = data.getStringExtra("ingredientAmount");

            Ingredient i = new Ingredient(name, imageUrl);
            i.setAmount(amount);

            addedIngredients.add(i);

            addedIngredientsListView.setAdapter(adapter);
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
        addedIngredientsListView.setAdapter(adapter);
        if(addedIngredients.size() == 0){
            noIngredientsTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noIngredientsTextView.setVisibility(View.INVISIBLE);
        }
    }

}