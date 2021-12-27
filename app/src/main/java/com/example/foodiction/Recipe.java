package com.example.foodiction;
import android.graphics.Color;
import android.media.Image;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recipe {

    public enum Difficulty {Easy, Medium, Hard};

    String _id;
    String name;
    String description;
    Date creationDate;
    Date modifiedDate;
    ArrayList<Ingredient> ingredients;
    Image recipeImage;
    ArrayList<Instruction> instructions;
    List<Image> pictures;
    Color color;
    List<String> categories;
    Time makingDuration;
    Difficulty difficulty;

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
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
}


