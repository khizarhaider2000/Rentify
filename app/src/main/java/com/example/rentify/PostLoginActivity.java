package com.example.rentify;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostLoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        String name = getIntent().getStringExtra("name");
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String accountType = getIntent().getStringExtra("accountType");

        // Find the TextViews in the layout
        TextView nameTextView = findViewById(R.id.nameText);
        TextView usernameTextView = findViewById(R.id.usernameText);
        TextView emailTextView = findViewById(R.id.emailText);
        TextView accountTypeTextView = findViewById(R.id.accountTypeText);

        // Set the text for each TextView
        accountTypeTextView.setText("Account Type: " + accountType);
        nameTextView.setText("Name: " + name);
        usernameTextView.setText("Username: " + username);
        emailTextView.setText("Email: " + email);
    }
}
