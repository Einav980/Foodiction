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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class RecipeIngredientsStep extends Fragment implements Step {
    final int INGREDIENT_LIST_REQUEST_CODE = 1;
    ListView addedIngredientsListView;
    AddedIngredientListAdapter adapter;
    ArrayList<Ingredient> addedIngredients;
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
        adapter = new AddedIngredientListAdapter(getContext(), addedIngredients);
        addedIngredientsListView.setAdapter(adapter);

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
        String name = data.getStringExtra("ingredientName");
        int imageId = data.getIntExtra("ingredientImage", 0);
        Ingredient i = new Ingredient(name, "test", imageId);
        addedIngredients.add(i);
        addedIngredientsListView.setAdapter(adapter);
        Log.i("TEST", name);
    }

    public void addIngredient(View v){
        Intent ingredientsListActivity = new Intent(getContext(), IngredientsListActivity.class);
        startActivityForResult(ingredientsListActivity, INGREDIENT_LIST_REQUEST_CODE);
    }

}