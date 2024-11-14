package com.example.rentify;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE_REQUEST_CODE = 100;

    DatabaseReference dRef;
    private ImageView itemImageView;
    private Button selectImageButton;
    private Uri imageUri; // Store the selected image URI for future use

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

        String username = getIntent().getStringExtra("username");
        dRef = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        // UI components
        EditText lessorItemName = findViewById(R.id.lessorItemName);
        EditText fee = findViewById(R.id.addItemFee);
        EditText itemDescription = findViewById(R.id.itemDescription);
        EditText itemTimePeriod = findViewById(R.id.itemTimePeriod);
        Button addItemButton = findViewById(R.id.addItemButton);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        Button backButton = findViewById(R.id.backButton2);
        itemImageView = findViewById(R.id.itemImageView);
        selectImageButton = findViewById(R.id.selectImageButton);

        // Select image button click listener
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
            }
        });

        // Load categories from Firebase and populate Spinner
        ArrayList<String> categories = new ArrayList<>();
        dRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String existingCategoryName = categorySnapshot.child("categoryName").getValue(String.class);
                    categories.add(existingCategoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddItemActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Back button functionality
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Add item button functionality within selected category
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String category = adapterView.getItemAtPosition(i).toString();
                addItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String enteredItemName = lessorItemName.getText().toString();
                        String enteredDescription = itemDescription.getText().toString();
                        String enteredCost = fee.getText().toString();
                        String enteredTime = itemTimePeriod.getText().toString();

                        // Validate inputs
                        if ((enteredItemName.isEmpty()) || (enteredDescription.isEmpty()) || (enteredCost.isEmpty()) || (enteredTime.isEmpty())) {
                            Toast.makeText(AddItemActivity.this, "Name, Description, Cost and Time Period for Item required", Toast.LENGTH_SHORT).show();
                        }
                        else if (!isAlpha(enteredItemName)) {
                            Toast.makeText(AddItemActivity.this, "Name of item must only be of letters", Toast.LENGTH_SHORT).show();
                        } else {
                            // Create item and save to Firebase
                            Item item = new Item(username, enteredItemName, category, enteredDescription, Math.round(Double.parseDouble(enteredCost) * 100) / 100D, Integer.parseInt(enteredTime));
                            dRef.child("Lessors").child(username).child("Items").child(enteredItemName).setValue(item);

                            // Check if imageUri is available
                            if (imageUri != null) {
                                // Code for uploading the imageUri to Firebase Storage can go here
                                // Currently, it's stored as a reference for future use
                            }

                            Toast.makeText(AddItemActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            itemImageView.setImageURI(imageUri); // Display the selected image in the ImageView
        }
    }
}
