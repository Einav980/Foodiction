package com.example.foodiction;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Ingredient implements Parcelable {
    String name = "";
    String displayName = "";
    String imageUrl = "";
    String amount = "";
    String id = "";

    public Ingredient(String displayName, String imageUrl) {
        this.id = UUID.randomUUID().toString();
        this.name = displayName.toLowerCase();
        this.displayName = displayName;
        this.imageUrl = imageUrl;
    }

    public Ingredient(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        id = data[0];
        name = data[1];
        displayName = data[2];
        imageUrl = data[3];
        amount = data[4];
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.id, this.name, this.displayName, this.imageUrl, this.amount});
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return String.format("name: %s, amount: %s", this.name, this.amount);
    }
}
