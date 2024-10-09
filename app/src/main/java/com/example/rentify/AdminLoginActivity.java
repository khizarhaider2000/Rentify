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

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditText UsernameInput = findViewById(R.id.UsernameInput);
        EditText PasswordInput = findViewById(R.id.PasswordInput);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = UsernameInput.getText().toString();
                String enteredPassword = PasswordInput.getText().toString();

                // Check if the entered password matches the admin password
                if (enteredUsername.equals(ADMIN_USERNAME) && enteredPassword.equals(ADMIN_PWD)) {
                    Toast.makeText(AdminLoginActivity.this, "Admin logged in!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLoginActivity.this, PostLoginActivity.class);
                    intent.putExtra("accountType", "Admin");
                    intent.putExtra("username", ADMIN_USERNAME);
                    intent.putExtra("email", "admin@rentify.com");
                    intent.putExtra("name", "admin account");
                    startActivity(intent);
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Incorrect username/password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}