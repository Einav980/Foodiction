package com.example.foodiction;

import android.net.Uri;
import android.text.format.Time;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Recipe {

    enum RecipeType {IMAGE, URL, DEFAULT}

    String id = "";
    String name = "";
    String displayName = "";
    String description = "";
    ArrayList<Ingredient> ingredients;
    ArrayList<Instruction> instructions;
    String category;
    String makingDuration = "";
    String userId;
    String imageUrl;
    String internetUrl;
    RecipeType type = RecipeType.DEFAULT;

    boolean is_liked = false;

    public Recipe(String displayName, String category, String description, String makingDuration, RecipeType type) {
        this.id = UUID.randomUUID().toString();
        this.name = displayName.toLowerCase();
        this.displayName = displayName;
        this.description = description;
        this.makingDuration = makingDuration;
        this.userId = MainActivity.getUserGuid();
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.category = category;
        this.type = type;
    }

    public Recipe(String displayName, String category, String description, String makingDuration) {
        this.id = UUID.randomUUID().toString();
        this.name = displayName.toLowerCase();
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.category = category;
        this.displayName = displayName;
        this.description = description;
        this.makingDuration = makingDuration;
        this.userId = MainActivity.getUserGuid();
    }

    public Recipe(Recipe recipe){
        this.id = recipe.getID();
        this.name = recipe.getName();
        this.ingredients = recipe.getIngredients();
        this.instructions = recipe.getInstructions();
        this.category = recipe.getCategory();
        this.displayName = recipe.getDisplayName();
        this.description = recipe.getDescription();
        this.makingDuration = recipe.getMakingDuration();
        this.userId = MainActivity.getUserGuid();
        this.type = recipe.getType();
    }

    public Recipe(){
        this.id = UUID.randomUUID().toString();
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.userId = MainActivity.getUserGuid();
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


    public void toggle_like(){
        this.is_liked = !this.is_liked;
    }

    public void setIs_liked(boolean is_liked) { this.is_liked = is_liked; }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<Instruction> instructions) { this.instructions = instructions;}

    @Override
    public String toString(){
        return String.format(
                "id: %s\n" +
                "name: %s\n" +
                "displayName: %s\n" +
                "description: %s\n" +
                "imageUrl: %s\n" +
                "ingredients: %s\n" +
                "instructions: %s\n" +
                "makingDuration: %s",
                this.id ,this.name, this.displayName, this.description, this.imageUrl, this.ingredients, this.instructions, this.makingDuration);
    }

    public void addInstruction(Instruction i){
        this.instructions.add(i);
    }

    public void removeInstruction(Instruction i){
        this.instructions.remove(i);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getInternetUrl() { return internetUrl; }

    public void setInternetUrl(String internetUrl) { this.internetUrl = internetUrl; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public RecipeType getType() {
        return type;
    }

    public void setType(RecipeType type) {
        this.type = type;
    }

}


