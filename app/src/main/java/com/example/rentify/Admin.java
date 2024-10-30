package com.example.rentify;

public class Admin extends User{

    private final String userType;

    // Default constructor for Admin
    public Admin(String userName, String password, String userType) {
        super(userName, password);
        this.userType = userType;
    }

    // Get method
    public String getUserType() { return userType; }

}
