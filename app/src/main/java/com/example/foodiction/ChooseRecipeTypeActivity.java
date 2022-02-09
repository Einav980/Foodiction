package com.example.foodiction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseRecipeTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_recipe_type);
    }

    public void startAddImageRecipeActivity(View view) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        intent.putExtra("create_recipe_type", Recipe.RecipeType.IMAGE);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void startAddRecipeActivity(View view) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        intent.putExtra("create_recipe_type", Recipe.RecipeType.DEFAULT);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

//    public void startAddInternetRecipeActivity(View view) {
//        Intent intent = new Intent(this, AddInternetRecipeActivity.class);
//        startActivity(intent);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//    }
}