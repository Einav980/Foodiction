package com.example.foodiction;

import android.net.Uri;
import android.text.format.Time;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recipe {



    public enum Difficulty {Easy, Medium, Hard;};
    String id;
    String name;
    String description;
    String creationDate;
    ArrayList<Ingredient> ingredients;
    ArrayList<Instruction> instructions;
    List<String> categories;
    String makingDuration = "";
    String userId;
    String imageUrl;
    //    Difficulty difficulty;
    boolean is_liked  =false;
    public Recipe(String name, String description, String makingDuration) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.makingDuration = makingDuration;
        this.userId = MainActivity.getUserGuid();
        this.creationDate = new Time().toString();
    }

    public Recipe(Recipe recipe){
        this.id = recipe.getID();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.makingDuration = recipe.getMakingDuration();
        this.userId = MainActivity.getUserGuid();
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

    public void setIs_liked(boolean is_liked) { this.is_liked = is_liked; }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<Instruction> instructions) { this.instructions = instructions;}

    @Override
    public String toString(){
        return String.format("name: %s\ndescription: %s\nimageUrl: %s\ningredients: %s",this.name,this.description, this.imageUrl, this.ingredients);
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
}


