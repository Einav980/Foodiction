package com.example.foodiction;

import android.os.Parcel;
import android.os.Parcelable;

public class Instruction implements Parcelable {
    String description;

    public Instruction(String description){
        this.description = description;
    }

    public Instruction(){}

    public Instruction(Parcel in){
        description = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Instruction createFromParcel(Parcel in) {
            return new Instruction(in);
        }

        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.description);
    }
}
