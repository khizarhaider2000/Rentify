package com.example.rentify;

public class Category {

    private String categoryName;
    private String description;
    private  String id;


    // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    public Category() {}

    public Category(String id,String categoryName, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
    }

    /*public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }*/

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
