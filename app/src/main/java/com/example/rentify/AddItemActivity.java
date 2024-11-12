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

public class AddItemActivity extends AppCompatActivity {

    DatabaseReference dRef;

    // Validates if input contains alphabets only
    private boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String username = getIntent().getStringExtra("username");
        dRef = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backButton2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditText lessorItemName = findViewById(R.id.lessorItemName);
        EditText cost = findViewById(R.id.addItemCostTitle);
        EditText category = findViewById(R.id.addItemCategoryTitle);
        EditText itemDescription = findViewById(R.id.itemDescription);
        EditText itemTimePeriod = findViewById(R.id.itemTimePeriod);
        Button addItemButton = findViewById(R.id.addItemButton);

        // Add category button that adds a Category object to the database with a unique id
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredItemName = lessorItemName.getText().toString();
                String enteredDescription = itemDescription.getText().toString();
                String enteredCost = cost.getText().toString();
                String enteredCategory = category.getText().toString();
                String enteredTime = itemTimePeriod.getText().toString();

                if ((enteredItemName.isEmpty()) || (enteredDescription.isEmpty()) || (enteredItemName.isEmpty()) || (enteredTime.isEmpty())) {
                    Toast.makeText(AddItemActivity.this, "Name, Description and Cost for Item required", Toast.LENGTH_SHORT).show();
                } else if (!(isNumeric(enteredCost))) {
                    Toast.makeText(AddItemActivity.this, "Cost must be a number", Toast.LENGTH_SHORT).show();
                } else if (!(isNumeric(enteredTime))) {
                    Toast.makeText(AddItemActivity.this, "Time Period must be a number", Toast.LENGTH_SHORT).show();
                } else if (!(isAlpha(enteredItemName))) {
                    Toast.makeText(AddItemActivity.this, "Name of item must only be of letters", Toast.LENGTH_SHORT).show();
                } else {
                    dRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {

                        String validCategory;
                        // Check if a category name already exists in database
                        // If not, adds Category to database
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean exists = false;

                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                                String existingCategoryName = categorySnapshot.child("categoryName").getValue(String.class);

                                if (enteredCategory.equalsIgnoreCase(existingCategoryName)) {
                                    exists = true;
                                    validCategory = existingCategoryName;
                                    break;
                                }
                            }

                            if (!(exists)) {
                                Toast.makeText(AddItemActivity.this, "Category does not exists", Toast.LENGTH_SHORT).show();
                            } else {
                                int cost = Integer.parseInt(enteredCost);
                                Item item = new Item(enteredItemName, validCategory, enteredDescription, cost, Integer.parseInt(enteredTime));
                                dRef.child("Lessors").child(username).child("Items").child(enteredItemName).setValue(item);

                                Toast.makeText(AddItemActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(AddItemActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }
}
