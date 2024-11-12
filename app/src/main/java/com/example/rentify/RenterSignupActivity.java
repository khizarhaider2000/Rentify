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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RenterSignupActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_renter_signup);

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Collect signup information from input fields and check for signup button being clicked
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
                    Toast.makeText(RenterSignupActivity.this, "All text fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (!(isAlpha(enteredFirstname)) || !(isAlpha(enteredLastname))) {
                    Toast.makeText(RenterSignupActivity.this, "Username & Lastname must only be of letters", Toast.LENGTH_SHORT).show();
                } else if (!enteredUsername.matches("[a-zA-Z0-9]*")) {
                    Toast.makeText(RenterSignupActivity.this, "Username can not have spaces or special characters", Toast.LENGTH_SHORT).show();
                }  else if (!(isValidEmailAddress(enteredEmail))){
                    Toast.makeText(RenterSignupActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                } else if (enteredPassword.length() < 6) {
                    Toast.makeText(RenterSignupActivity.this, "Password must be 6 or more characters", Toast.LENGTH_SHORT).show();
                }  else {
                    // Check if the username or email already exists in the database
                    dRef.child("Renters").child(enteredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(RenterSignupActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                            } else {
                                // Check if the email already exists
                                dRef.child("Renters").orderByChild("email").equalTo(enteredEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Toast.makeText(RenterSignupActivity.this, "Email is already associated with an account", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Database reference stores all lessor information under "Lessors"
                                            dRef.child("Renters").child(enteredUsername).child("userType").setValue("Renter");
                                            dRef.child("Renters").child(enteredUsername).child("userName").setValue(enteredUsername);
                                            dRef.child("Renters").child(enteredUsername).child("firstName").setValue(enteredFirstname);
                                            dRef.child("Renters").child(enteredUsername).child("lastName").setValue(enteredLastname);
                                            dRef.child("Renters").child(enteredUsername).child("email").setValue(enteredEmail);
                                            dRef.child("Renters").child(enteredUsername).child("password").setValue(enteredPassword);

                                            // Navigate to Post Login Activity along with renter information
                                            Toast.makeText(RenterSignupActivity.this, "Congrats on your Renter Account", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RenterSignupActivity.this, LessorPostLoginActivity.class);
                                            intent.putExtra("accountType", "Renter");
                                            intent.putExtra("username", enteredUsername);
                                            intent.putExtra("email", enteredEmail);
                                            intent.putExtra("name", enteredFirstname + " " + enteredLastname);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(RenterSignupActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(RenterSignupActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}

