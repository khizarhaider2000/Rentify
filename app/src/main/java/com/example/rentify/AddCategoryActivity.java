package com.example.rentify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCategoryActivity extends AppCompatActivity {

    DatabaseReference dRef;

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
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

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredCategoryName = categoryName.getText().toString();
                String enteredCategoryDescription = categoryDescription.getText().toString();

                if ((enteredCategoryName.isEmpty()) || (enteredCategoryDescription.isEmpty())) {
                    Toast.makeText(AddCategoryActivity.this, "All text fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (!(isAlpha(enteredCategoryName))) {
                    Toast.makeText(AddCategoryActivity.this, "Name of category must only be of letters", Toast.LENGTH_SHORT).show();
                } else {
                    String id = FirebaseDatabase.getInstance().getReference("Categories").push().getKey();
                    dRef.child("Categories").child(id).child(enteredCategoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
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