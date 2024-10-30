package com.example.rentify;

public class Account {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userName;
    private String userType;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Account() {}

    public Account(String firstName, String lastName, String email, String password, String username, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userName = username;
        this.userType = accountType;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return userName; }
    public void setUsername(String username) { this.userName = username; }

    public String getAccountType() { return userType; }
    public void setAccountType(String accountType) { this.userType = accountType; }
}
