package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void OnLessorButton(View view) {
        // Navigate to the Lessor Signup Activity
        Intent intent = new Intent(getApplicationContext(), LessorSignupActivity.class);
        startActivityForResult(intent, 0);
    }

    public void OnRenterButton(View view) {
        // Navigate to the Renter Signup Activity
        Intent intent = new Intent(getApplicationContext(), RenterSignupActivity.class);
        startActivityForResult(intent, 0);
    }
}
