package com.example.rentify;

public class User {
    private String userName;
    private String password;
    private String userType;


    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    public User(String userName, String password, String userType) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    // Getters and Setters
    public String getUsername() { return userName; }
    public void setUsername(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
}
