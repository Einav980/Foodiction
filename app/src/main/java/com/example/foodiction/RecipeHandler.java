package com.example.foodiction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipeHandler {

    private FirebaseAuth mAuth;

    public RecipeHandler(){
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean addRecipe(String name, String description){
        if(name.isEmpty())
        {
            return false;
        }
        Recipe r = new Recipe(name, description);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipes = database.getReference("recipes");
        String newRecipeKey = recipes.push().getKey();
        recipes.child(newRecipeKey).setValue(r);
        return true;
    }

}
