package com.example.foodiction;

public class Ingredient {
    String name;
    String type;

    public Ingredient(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Ingredient(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
