package com.example.rentify;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {

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

        String username = getIntent().getStringExtra("username");
        dRef = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        EditText lessorItemName = findViewById(R.id.lessorItemName);
        EditText fee = findViewById(R.id.addItemFee);
        EditText itemDescription = findViewById(R.id.itemDescription);
        EditText itemTimePeriod = findViewById(R.id.itemTimePeriod);
        Button addItemButton = findViewById(R.id.addItemButton);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        Button backButton = findViewById(R.id.backButton2);

        ArrayList<String> categories = new ArrayList<String>();
        dRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String existingCategoryName = categorySnapshot.child("categoryName").getValue(String.class);
                    categories.add(existingCategoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddItemActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

                        // Check if any input fields are filled
                        if ((enteredItemName.isEmpty()) || (enteredDescription.isEmpty()) || (enteredCost.isEmpty()) || (enteredTime.isEmpty())) {
                            Toast.makeText(AddItemActivity.this, "Name, Description, Cost and Time Period for Item required", Toast.LENGTH_SHORT).show();
                        }
                        else if (!(isAlpha(enteredItemName))) {
                            Toast.makeText(AddItemActivity.this, "Name of item must only be of letters", Toast.LENGTH_SHORT).show();
                        }
                        // enteredCost is already validated to accept only numeric/decimal values in EditText of UI
                        // enteredTime is already validated to accept only integer values in EditText of UI
                        // category is already validated to provide a dropdown menu of only existing categories
                        else {
                            Item item = new Item(username, enteredItemName, category, enteredDescription, Math.round(Double.parseDouble(enteredCost)*100)/100D, Integer.parseInt(enteredTime));
                            dRef.child("Lessors").child(username).child("Items").child(enteredItemName).setValue(item);

                            Toast.makeText(AddItemActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}

