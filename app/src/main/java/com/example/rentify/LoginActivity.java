package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Collect login information from input fields and check for login button being clicked
        EditText UsernameInput = findViewById(R.id.UsernameInput);
        EditText PasswordInput = findViewById(R.id.PasswordInput);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = UsernameInput.getText().toString();
                String enteredPassword = PasswordInput.getText().toString();

                // User authentication from database not fully implemented (will be done for Deliverable 2)
                // Temporarily, any user can login and navigate to Post Login Activity
                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All input fields must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Welcome back, " + enteredUsername, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, PostLoginActivity.class);
                    intent.putExtra("accountType", "User");
                    intent.putExtra("username", enteredUsername);
                    startActivity(intent);
                }
            }
        });

    }
}