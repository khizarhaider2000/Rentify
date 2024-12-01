package com.example.rentify;

public class Request {

    private String lessorName;
    private String itemName;
    private String category;
    private String status;
    private String renterName;


    // Default constructor
    public Request() {}

    public Request(String lessorName, String itemName, String category, String status, String renterName) {
        this.lessorName = lessorName;
        this.itemName = itemName;
        this.status = status;
        this.category = category;
        this.renterName = renterName;
    }

    // Getters and Setters
    public String getLessorName() { return lessorName; }
    public void setLessorName(String lessorName) { this.lessorName = lessorName; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category;}

    public String getRenterName() { return renterName; }
    public void setRenterName(String renterName) { this.renterName = renterName;}
}