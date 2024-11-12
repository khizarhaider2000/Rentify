package com.example.rentify;

public class Item {

    private String itemName;
    private String description;
    private int fee;
    private int timePeriod;
    private String category;


    // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    public Item() {}

    public Item(String itemName, String category, String description, int fee, int timePeriod) {
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.fee = fee;
        this.timePeriod = timePeriod;
    }

    // Getters and Setters
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getFee() { return fee; }
    public void setFee(int cost) { this.fee = fee; }

    public int getTimePeriod() { return timePeriod; }
    public void setTimePeriod(int timePeriod) { this.timePeriod = timePeriod; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category;}
}