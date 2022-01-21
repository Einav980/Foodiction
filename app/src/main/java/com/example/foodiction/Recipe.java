package com.example.foodiction;

import android.graphics.Color;
import android.media.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recipe {

    public enum Difficulty {Easy, Medium, Hard};

    String _id;
    String name;
    String description;
    Date creationDate;
    ArrayList<Ingredient> ingredients;
    Image recipeImage;
    ArrayList<Instruction> instructions;
    List<Image> pictures;
    List<String> categories;
    String makingDuration;
    Difficulty difficulty;

    public Recipe(String name, String description, String makingDuration) {
        this.name = name;
        this.description = description;
        this.makingDuration = makingDuration;
    }
    public Recipe(Recipe recipe){
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.makingDuration = recipe.getMakingDuration();
    }

    public Recipe(){
        ingredients = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMakingDuration() { return makingDuration; }

    public void setMakingDuration(String makingDuration) { this.makingDuration = makingDuration; }
}


