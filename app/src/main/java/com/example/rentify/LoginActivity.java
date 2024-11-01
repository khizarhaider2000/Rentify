package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText UsernameInput;
    private EditText PasswordInput;
    private Button loginButton;
    private Button backButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Find the views
        UsernameInput = findViewById(R.id.UsernameInput);
        PasswordInput = findViewById(R.id.PasswordInput);
        loginButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.backButton);

        // Back button functionality
        backButton.setOnClickListener(view -> finish());

        // Login button functionality
        loginButton.setOnClickListener(view -> {
            String enteredUsername = UsernameInput.getText().toString().trim();
            String enteredPassword = PasswordInput.getText().toString().trim();

            // Check for empty fields
            if (enteredUsername.isEmpty()) {
                UsernameInput.setError("Username is required");
                UsernameInput.requestFocus();
                return;
            }

            if (!enteredUsername.matches("[a-zA-Z0-9]*")) {
                UsernameInput.setError("Username can not have spaces or special characters");
                UsernameInput.requestFocus();
                return;
            }

            else if (enteredPassword.isEmpty()) {
                PasswordInput.setError("Password is required");
                PasswordInput.requestFocus();
                return;
            }

            // Authenticate user by checking both Lessors and Renters nodes
            authenticateUser(enteredUsername, enteredPassword);
        });
    }

    private void authenticateUser(String username, String password) {
        // First, check in the Lessors node
        DatabaseReference lessorsReference = FirebaseDatabase.getInstance().getReference("Lessors");
        lessorsReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lessor found, validate password
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(password)) {
                        Toast.makeText(LoginActivity.this, "Welcome back, " + username, Toast.LENGTH_SHORT).show();
                        navigateToPostLogin(username, "Lessor");
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If not found in Lessors, check in Renters
                    checkInRenters(username, password);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkInRenters(String username, String password) {
        DatabaseReference rentersReference = FirebaseDatabase.getInstance().getReference("Renters");
        rentersReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Renter found, validate password
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(password)) {
                        Toast.makeText(LoginActivity.this, "Welcome back, " + username, Toast.LENGTH_SHORT).show();
                        navigateToPostLogin(username, "Renter");
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User not found. Please check your username.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToPostLogin(String username, String accountType) {
        Intent intent = new Intent(LoginActivity.this, PostLoginActivity.class);
        intent.putExtra("accountType", accountType);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
