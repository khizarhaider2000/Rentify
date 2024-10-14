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

public class LessorActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_lessor);

        Button backButton = findViewById(R.id.backButton2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditText firstnameInput = findViewById(R.id.FirstnameInput);
        EditText lastnameInput = findViewById(R.id.LastnameInput);
        EditText usernameInput = findViewById(R.id.UsernameInput2);
        EditText emailInput = findViewById(R.id.EmailInput);
        EditText passwordInput = findViewById(R.id.PassSignUpText);
        Button signUpButton = findViewById(R.id.SignUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredFirstname = firstnameInput.getText().toString();
                String enteredLastname = lastnameInput.getText().toString();
                String enteredUsername = usernameInput.getText().toString();
                String enteredEmail = emailInput.getText().toString();
                String enteredPassword = passwordInput.getText().toString();

                if ((enteredEmail.isEmpty()) || (enteredFirstname.isEmpty()) || (enteredLastname.isEmpty()) || (enteredUsername.isEmpty()) || (enteredPassword.isEmpty())) {
                    Toast.makeText(LessorActivity.this, "All text fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (!(isAlpha(enteredFirstname)) || !(isAlpha(enteredLastname))) {
                    Toast.makeText(LessorActivity.this, "Username & Lastname must only be of letters", Toast.LENGTH_SHORT).show();
                } else if (!(isValidEmailAddress(enteredEmail))){
                    Toast.makeText(LessorActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LessorActivity.this, "Congrats on your Lessor Account", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LessorActivity.this, PostLoginActivity.class);
                    intent.putExtra("accountType", "Lessor");
                    intent.putExtra("username", enteredUsername);
                    intent.putExtra("email", enteredEmail);
                    intent.putExtra("name", enteredFirstname + " " + enteredLastname);
                    startActivity(intent);
                }
            }
        });

    }
}
