package com.example.foodiction;

public class Ingredient {
    String name;
    String type;
    int imageId;

    public Ingredient(String name, String type, int imageId) {
        this.name = name;
        this.type = type;
        this.imageId = imageId;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return getName();
    }
}
