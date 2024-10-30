package com.example.rentify;

public class Renter extends Account {
    // Default constructor required for calls to DataSnapshot.getValue(Renter.class)
    public Renter() {
        super();
    }

    public Renter(String firstName, String lastName, String email, String password, String username, String accountType) {
        super(firstName, lastName, email, password, username, accountType);
    }
}