package com.example.rentify;

public class User {
    private String userName;
    private String password;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() { return userName; }
    public void setUsername(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
