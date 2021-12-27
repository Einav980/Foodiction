package com.example.foodiction;

public class Instruction {
    String description;

    public Instruction(String description){
        this.description = description;
    }

    public Instruction(){}

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
}
