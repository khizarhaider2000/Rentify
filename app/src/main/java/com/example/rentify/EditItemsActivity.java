package com.example.rentify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EditItemsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    ListView lessorItemsListView;
    private List<Item> lessorItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_items);

        // Collect the username
        String username = getIntent().getStringExtra("username");

        databaseReference = FirebaseDatabase.getInstance().getReference("Lessors").child(username).child("Items");
        lessorItemsListView = findViewById(R.id.lessorItemsList);
        lessorItemList = new ArrayList<>();

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Displays list of items
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lessorItemList.clear(); // Clear list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    lessorItemList.add(item);
                }
                ItemAdapter itemAdapter = new ItemAdapter(EditItemsActivity.this, lessorItemList);
                lessorItemsListView.setAdapter(itemAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}