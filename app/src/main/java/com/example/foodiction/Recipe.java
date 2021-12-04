package com.example.foodiction;
import io.realm.Realm;

public class Recipe {
    private String name;
    private String description;


    public Recipe(String name , String description){
        this.name= name;
        this.description = description;
    }

    public Recipe(){}

    public String getName(){return name;}
    public String getDescription(){return description;}
}


