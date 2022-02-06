package com.example.foodiction;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecipeHandler {
    private FirebaseAuth mAuth;
    static List<Recipe> recipes;


    public RecipeHandler(){
        mAuth = FirebaseAuth.getInstance();
        recipes = new ArrayList<>();
    }

    public void addRecipe(Recipe recipe){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipes = database.getReference("recipes");
        recipes.child(recipe.getID()).setValue(recipe);
//        DatabaseReference df = recipes.child(recipe.getID());
//        if(df == null){
//            Log.i("Foodiction", "Erro!!!");
//        }
//        else
//        {
//            Log.i("Foodiction", df.toString());
//        }
    }

    //TODO add all the relevant function to go to the DB

    public boolean deleteRecipe(String recipe_UUID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipes = database.getReference("recipes");
        recipes.child(recipe_UUID).removeValue();
        return true;
    }

    public boolean addToFavorite(String recipe_UUID , boolean change){
        Map<String, Object> updates = new HashMap<>();
        updates.put("is_liked", change);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipes = database.getReference("recipes");
        recipes.child(recipe_UUID).updateChildren(updates);
        return true;
    }

//    public boolean addToFavoriteRecipes(Recipe recipe){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference recipes = database.getReference("favorites");
//        recipes.child(recipe.getID()).setValue(recipe);
//        return true;
//    }
//
//    public boolean removeFromFavoriteRecipes(String recipe_UUID){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference recipes = database.getReference("favorites");
//        recipes.child(recipe_UUID).removeValue();
//        return true;
//    }

    public boolean getSpecificRecipe(){
        return true;
    }

    public boolean editSpecificRecipe(){return true;}

}
