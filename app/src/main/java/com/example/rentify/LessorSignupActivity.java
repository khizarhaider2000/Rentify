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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LessorSignupActivity extends AppCompatActivity {

    DatabaseReference dRef;

    // Validates if input contains alphabets only
    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    // Validates email address
    public boolean isValidEmailAddress(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dRef = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lessor_signup);

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backButton2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Collect signup information from input fields and check for signup button being clicked
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
                    Toast.makeText(LessorSignupActivity.this, "All text fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (!(isAlpha(enteredFirstname)) || !(isAlpha(enteredLastname))) {
                    Toast.makeText(LessorSignupActivity.this, "Firstname & Lastname must only be of letters", Toast.LENGTH_SHORT).show();
                } else if (!(isValidEmailAddress(enteredEmail))){
                    Toast.makeText(LessorSignupActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                } else {
                    // Database reference stores all lessor information under "Lessors"
                    dRef.child("Lessors").child(enteredUsername).child("userType").setValue("Lessor");
                    dRef.child("Lessors").child(enteredUsername).child("firstName").setValue(enteredFirstname);
                    dRef.child("Lessors").child(enteredUsername).child("lastName").setValue(enteredLastname);
                    dRef.child("Lessors").child(enteredUsername).child("email").setValue(enteredEmail);
                    dRef.child("Lessors").child(enteredUsername).child("password").setValue(enteredPassword);

                    // Navigate to Post Login Activity along with lessor information
                    Toast.makeText(LessorSignupActivity.this, "Congrats on your Lessor Account", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LessorSignupActivity.this, PostLoginActivity.class);
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
