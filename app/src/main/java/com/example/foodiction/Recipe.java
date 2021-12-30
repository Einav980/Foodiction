package com.example.foodiction;
import android.graphics.Color;
import android.media.Image;
import android.text.format.Time;

import java.util.Date;
import java.util.List;

public class Recipe {

    public enum Difficulty {Easy, Medium, Hard};

    String _id;
    String name;
    String description;
    Date creationDate;
    Date modifiedDate;
    List<Ingredient> ingredients;
    Image recipeImage;
    List<ProcedureStep> procedureSteps;
    List<Image> pictures;
    Color color;
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

    public Recipe(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMakingDuration() { return makingDuration; }

    public void setMakingDuration(String makingDuration) { this.makingDuration = makingDuration; }
}


