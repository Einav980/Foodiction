package com.example.foodiction;

import java.util.UUID;

public class Category {
    private String id;
    private String name;

    Category(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    Category(){this.id = UUID.randomUUID().toString();};

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
