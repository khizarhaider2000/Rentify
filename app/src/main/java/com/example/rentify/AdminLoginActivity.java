package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PWD = "XPI76SZUqyCjVxgnUjm0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Collect admin login information from input fields and check for login button being clicked
        EditText UsernameInput = findViewById(R.id.UsernameInput);
        EditText PasswordInput = findViewById(R.id.PasswordInput);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = UsernameInput.getText().toString();
                String enteredPassword = PasswordInput.getText().toString();

                // Check if the entered inputs match the admin username and password
                // If successful, navigate to Post Login Activity
                if (enteredUsername.equals(ADMIN_USERNAME) && enteredPassword.equals(ADMIN_PWD)) {
                    Toast.makeText(AdminLoginActivity.this, "Admin logged in!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLoginActivity.this, AdminPostLoginActivity.class);
                    intent.putExtra("accountType", "Admin");
                    intent.putExtra("username", ADMIN_USERNAME);
                    intent.putExtra("email", "admin@rentify.com");
                    intent.putExtra("name", "Admin Name");
                    startActivity(intent);
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Incorrect username/password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}