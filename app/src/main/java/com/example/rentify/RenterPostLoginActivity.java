package com.example.rentify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RenterPostLoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_renter_post_login);

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
                Toast.makeText(RenterPostLoginActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}

