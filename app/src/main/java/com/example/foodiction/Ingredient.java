package com.example.foodiction;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient {
    String name;
    String imageUrl;
    String amount = "";

    public Ingredient(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Ingredient(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("name", getName());
            jsonObject.put("imageUrl", getImageUrl());

            return jsonObject.toString();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
            return "";
        }

    }
}
