package com.example.rentify;

public class Lessor extends User {
    private String firstName;
    private String lastName;
    private String email;

    // Default constructor required for calls to DataSnapshot.getValue(Lessor.class)
    public Lessor() {
        super();
    }

    public Lessor(String firstName, String lastName, String email, String password, String userName, String userType) {
        super(userName, password, userType);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

}
