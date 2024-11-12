package com.example.rentify;

public class Item {

    private String lessorName;
    private String itemName;
    private String description;
    private double fee;
    private int timePeriod;
    private String category;


    // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    public Item() {}

    public Item(String lessorName, String itemName, String category, String description, double fee, int timePeriod) {
        this.lessorName = lessorName;
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.fee = fee;
        this.timePeriod = timePeriod;
    }

    // Getters and Setters
    public String getLessorName() { return lessorName; }
    public void setLessorName(String lessorName) { this.lessorName = lessorName; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public int getTimePeriod() { return timePeriod; }
    public void setTimePeriod(int timePeriod) { this.timePeriod = timePeriod; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category;}
}