package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RenterActivity extends AppCompatActivity {
    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidEmailAddress(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_renter);

        Button backButton = findViewById(R.id.backButton3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditText firstnameInput = findViewById(R.id.firstnameInput);
        EditText lastnameInput = findViewById(R.id.lastnameInput);
        EditText usernameInput = findViewById(R.id.usernameInput2);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.PassSignUpText);
        Button signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredFirstname = firstnameInput.getText().toString();
                String enteredLastname = lastnameInput.getText().toString();
                String enteredUsername = usernameInput.getText().toString();
                String enteredEmail = emailInput.getText().toString();
                String enteredPassword = passwordInput.getText().toString();

                if ((enteredEmail.isEmpty()) || (enteredFirstname.isEmpty()) || (enteredLastname.isEmpty()) || (enteredUsername.isEmpty()) || (enteredPassword.isEmpty())) {
                    Toast.makeText(RenterActivity.this, "All text fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (!(isAlpha(enteredFirstname)) || !(isAlpha(enteredLastname))) {
                    Toast.makeText(RenterActivity.this, "Username & Lastname must only be of letters", Toast.LENGTH_SHORT).show();
                } else if (!(isValidEmailAddress(enteredEmail))){
                    Toast.makeText(RenterActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RenterActivity.this, "Congrats on your Renter Account", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RenterActivity.this, PostLoginActivity.class);
                    intent.putExtra("accountType", "Renter");
                    intent.putExtra("username", enteredUsername);
                    intent.putExtra("email", enteredEmail);
                    intent.putExtra("name", enteredFirstname + " " + enteredLastname);
                    startActivity(intent);
                }
            }
        });

    }
}

