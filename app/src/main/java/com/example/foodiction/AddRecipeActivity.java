package com.example.foodiction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddRecipeActivity extends AppCompatActivity {

    private RecipeHandler recipeHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_activity);

        recipeHandler = new RecipeHandler();
    }

    public void addRecipe(View view) {
        String recipeName = ((TextView) findViewById(R.id.recipeNameTextBox)).getText().toString();
        String recipeDescription = ((TextView) findViewById(R.id.recipeDescTextBox)).getText().toString();
        boolean result = recipeHandler.addRecipe(recipeName, recipeDescription);
        if (result) {
            Toast.makeText(this, "Recipe added succesfully!", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            Toast.makeText(this, "There was an error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("FOODICTION", "Recipe stopped");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}