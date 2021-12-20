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
        Intent intent = new Intent(this, AddImageRecipeActivity.class);
        startActivity(intent);
    }

    public void startAddRecipeActivity(View view) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }
}