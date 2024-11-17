package com.example.rentify;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditItemsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView lessorItemsRecyclerView;
    private ItemAdapterRecycler itemAdapterRecycler;
    private List<Item> lessorItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items); // Use your activity_edit_items.xml

        // Retrieve the username from the intent
        String username = getIntent().getStringExtra("username");

        // Initialize Firebase reference to the user's items
        databaseReference = FirebaseDatabase.getInstance().getReference("Lessors").child(username).child("Items");

        // Initialize RecyclerView and item list
        lessorItemsRecyclerView = findViewById(R.id.lessorItemsRecyclerView); // Updated ID from XML
        lessorItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lessorItemList = new ArrayList<>();

        // Back button functionality
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Fetch items from Firebase and update RecyclerView
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lessorItemList.clear(); // Clear the list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    lessorItemList.add(item); // Add each item to the list
                }

                // Initialize and bind the adapter
                itemAdapterRecycler = new ItemAdapterRecycler(EditItemsActivity.this, lessorItemList);
                lessorItemsRecyclerView.setAdapter(itemAdapterRecycler);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
