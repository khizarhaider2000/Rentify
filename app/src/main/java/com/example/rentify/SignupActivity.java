package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Set up the back button to go back to the main activity
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void OnLessorButton(View view) {
        Intent intent = new Intent(getApplicationContext(), LessorActivity.class);
        startActivityForResult(intent, 0);
    }

    public void OnRenterButton(View view) {
        Intent intent = new Intent(getApplicationContext(), RenterActivity.class);
        startActivityForResult(intent, 0);
    }
}
