package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LessorPostLoginActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lessor_post_login);

        // Collect the username and account type information to update TextViews on layout
        String username = getIntent().getStringExtra("username");
        String accountType = getIntent().getStringExtra("accountType");

        TextView usernameTextView = findViewById(R.id.usernameText);
        TextView accountTypeTextView = findViewById(R.id.accountTypeText);

        usernameTextView.setText(username + "!");
        accountTypeTextView.setText(accountType);

        // Check for logout button being clicked
        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LessorPostLoginActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Button to navigate to adding item page
        Button addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessorPostLoginActivity.this, AddItemActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        // Button to navigate to editing/deleting items page
        Button editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessorPostLoginActivity.this, EditItemsActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }

}
