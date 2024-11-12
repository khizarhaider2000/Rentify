package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPostLoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_post_login);

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
                Toast.makeText(AdminPostLoginActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Check for categories button being clicked
        Button categoriesButton = findViewById(R.id.addButton);
        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
                startActivity(intent);
            }
        });

        // Check for user accounts button being clicked
        Button userAccountsButton = findViewById(R.id.userAccountsButton);
        userAccountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserAccountsActivity.class);
                startActivity(intent);
            }
        });

    }

}
