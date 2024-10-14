package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PostLoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_login);

        String name = getIntent().getStringExtra("name");
        String accountType = getIntent().getStringExtra("accountType");

        // Find the TextViews in the layout
        TextView nameTextView = findViewById(R.id.nameText);
        TextView accountTypeTextView = findViewById(R.id.accountTypeText);

        // Set the text for each TextView
        nameTextView.setText(name + "!");
        accountTypeTextView.setText(accountType);

        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
