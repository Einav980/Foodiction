package com.example.foodiction;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Category implements Parcelable {
    private String id;
    private String name;

    Category(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    Category(){this.id = UUID.randomUUID().toString();};

    public Category(Parcel in){
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
    }
}
