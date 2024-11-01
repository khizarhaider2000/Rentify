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
    private static final Admin admin = new Admin(ADMIN_USERNAME,ADMIN_PWD, "Admin");

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
                // If successful, navigate to Admin Post Login Activity
                if (enteredUsername.equals(admin.getUsername()) && enteredPassword.equals(admin.getPassword())) {
                    Toast.makeText(AdminLoginActivity.this, "Admin logged in!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLoginActivity.this, AdminPostLoginActivity.class);
                    intent.putExtra("accountType", admin.getUserType());
                    intent.putExtra("username", admin.getUsername());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Incorrect username/password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}