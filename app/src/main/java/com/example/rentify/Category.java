package com.example.rentify;

public class Category {

    private String name;
    private String description;


    // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public String name() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
