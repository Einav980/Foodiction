package com.example.foodiction;

import android.graphics.Color;
import android.media.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recipe {

    public enum Difficulty {Easy, Medium, Hard};

    String id;
    String name;
    String description;
    Date creationDate;
    ArrayList<Ingredient> ingredients;
    Image recipeImage;
    ArrayList<Instruction> instructions;
    List<Image> pictures;
    Color color;
    List<String> categories;
    String makingDuration;
    //    Difficulty difficulty;
    boolean is_liked  =false;

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

    public String getID(){return this.id;}

    public boolean getIs_liked() { return is_liked; }

    public void setIs_liked(boolean is_liked) { this.is_liked = is_liked; }

}


