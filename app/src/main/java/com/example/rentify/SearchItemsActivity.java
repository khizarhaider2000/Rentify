package com.example.rentify;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchItemsActivity extends AppCompatActivity {

    ListView itemListView;
    private List<Item> items;
    DatabaseReference databaseReference;
    DatabaseReference dRef;
    private List<Item> itemList;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_items);

        databaseReference = FirebaseDatabase.getInstance().getReference("Items");
        dRef = FirebaseDatabase.getInstance().getReference("Categories");

        itemListView = findViewById(R.id.itemListView);
        itemList = new ArrayList<>();

        Spinner itemSpinner = findViewById(R.id.itemSpinner);
        Button backButton = findViewById(R.id.backButton3);
        Button searchItemButton = findViewById(R.id.searchButton2);
        EditText itemName = findViewById(R.id.itemName);


        ArrayList<String> categories = new ArrayList<String>();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String existingCategoryName = categorySnapshot.child("categoryName").getValue(String.class);
                    categories.add(existingCategoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchItemsActivity.this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                itemSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Add item button within selected category
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enteredItemName = itemName.getText().toString();
                        String category = adapterView.getItemAtPosition(i).toString();
                        itemList.clear(); // Clear list to avoid duplication

                        if (enteredItemName.isEmpty()) {
                            databaseReference.child(category).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Item item = snapshot.getValue(Item.class);
                                        itemList.add(item);
                                    }

                                    if (itemList.isEmpty()) {
                                        Toast.makeText(SearchItemsActivity.this, "No items listed under " + category, Toast.LENGTH_SHORT).show();
                                    }

                                    else {
                                        ItemAdapter itemAdapter = new ItemAdapter(SearchItemsActivity.this, itemList);
                                        itemListView.setAdapter(itemAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(SearchItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else if (!(isAlpha(enteredItemName))) {
                            Toast.makeText(SearchItemsActivity.this, "Name of item must only be of letters", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            databaseReference.child(category).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Item item = snapshot.getValue(Item.class);
                                        itemList.add(item);
                                    }

                                    List<Item> searchedItemList = new ArrayList<>();
                                    for (Item item: itemList) {
                                        if ((item.getItemName().toLowerCase()).startsWith(enteredItemName.toLowerCase())) {
                                            searchedItemList.add(item);
                                        }
                                    }

                                    if (searchedItemList.isEmpty()) {
                                        Toast.makeText(SearchItemsActivity.this, "No item found ", Toast.LENGTH_SHORT).show();
                                    }
                                    ItemAdapter itemAdapter = new ItemAdapter(SearchItemsActivity.this, searchedItemList);
                                    itemListView.setAdapter(itemAdapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(SearchItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
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
}
