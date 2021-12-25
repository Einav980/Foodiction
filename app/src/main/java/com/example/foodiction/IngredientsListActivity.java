package com.example.foodiction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class IngredientsListActivity extends AppCompatActivity {
    ArrayList<Ingredient> ingredientsList;
    ListView ingredientsListView;

    // TODO:
    // getAllIngredients(); from db

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_list);

        int ingredientImage = R.drawable.ingredient_eggs;
        int flourImage = R.drawable.ingredient_flour;

        Ingredient i = new Ingredient("Eggs", "Test", ingredientImage);
        Ingredient f = new Ingredient("Flour", "Test", flourImage);

        ingredientsList = new ArrayList<>();
        ingredientsList.add(i);
        ingredientsList.add(f);

        IngredientsListAdapter adapter = new IngredientsListAdapter(getApplicationContext(), ingredientsList);

        ingredientsListView = findViewById(R.id.ingredientsListView);
        ingredientsListView.setAdapter(adapter);
        ingredientsListView.setClickable(true);

        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent resultIntent = new Intent();

                resultIntent.putExtra("ingredientName", ingredientsList.get(i).name);
                resultIntent.putExtra("ingredientType", ingredientsList.get(i).type);
                resultIntent.putExtra("ingredientImage", ingredientsList.get(i).imageId);
                setResult(IngredientsListActivity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}