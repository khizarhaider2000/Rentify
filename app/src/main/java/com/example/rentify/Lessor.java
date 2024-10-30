package com.example.rentify;

public class Lessor extends Account {
    // Default constructor required for calls to DataSnapshot.getValue(Lessor.class)
    public Lessor() {
        super();
    }

    public Lessor(String firstName, String lastName, String email, String password, String username, String accountType) {
        super(firstName, lastName, email, password, username, accountType);
    }
}
