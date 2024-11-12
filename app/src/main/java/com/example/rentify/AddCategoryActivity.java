package com.example.rentify;

import android.os.Bundle;
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

public class AddCategoryActivity extends AppCompatActivity {

    DatabaseReference dRef;

    // Validates if input contains alphabets only (allowed to have spaces in between alphabets)
    private boolean isAlpha(String name) {
        String nameCleaned = name.strip();
        char[] chars = nameCleaned.toCharArray();
        for (char c : chars) {
            if ((!Character.isLetter(c)) && (!Character.isWhitespace(c))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dRef = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_category);

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditText categoryName = findViewById(R.id.categoryName);
        EditText categoryDescription = findViewById(R.id.categoryDescription);
        Button addCategoryButton = findViewById(R.id.addCategoryButton);

        // Add category button that adds a Category object to the database with a unique id
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredCategoryName = categoryName.getText().toString();
                String enteredCategoryDescription = categoryDescription.getText().toString();

                if ((enteredCategoryName.isEmpty()) || (enteredCategoryDescription.isEmpty())) {
                    Toast.makeText(AddCategoryActivity.this, "Name and Description for Category required", Toast.LENGTH_SHORT).show();
                } else if (!(isAlpha(enteredCategoryName))) {
                    Toast.makeText(AddCategoryActivity.this, "Name of category must only be of letters", Toast.LENGTH_SHORT).show();
                } else {
                    String id = FirebaseDatabase.getInstance().getReference("Categories").push().getKey();

                    dRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {

                        // Check if a category name already exists in database
                        // If not, adds Category to database
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean exists = false;

                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                                String existingCategoryName = categorySnapshot.child("categoryName").getValue(String.class);

                                if (enteredCategoryName.equalsIgnoreCase(existingCategoryName)) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (exists) {
                                Toast.makeText(AddCategoryActivity.this, "Category already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Category category = new Category(id, enteredCategoryName, enteredCategoryDescription);
                                dRef.child("Categories").child(id).setValue(category);

                                Toast.makeText(AddCategoryActivity.this, "Category created", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(AddCategoryActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }
}