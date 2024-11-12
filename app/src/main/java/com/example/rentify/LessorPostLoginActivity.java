package com.example.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class LessorPostLoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    ListView lessorItemsListView;
    private List<Item> lessorItemList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lessor_post_login);

        // Collect the username and account type information to update TextViews on layout
        String username = getIntent().getStringExtra("username");
        String accountType = getIntent().getStringExtra("accountType");

        databaseReference = FirebaseDatabase.getInstance().getReference("Lessors").child(username).child("Items");
        lessorItemsListView = findViewById(R.id.lessorItemsList);
        lessorItemList = new ArrayList<>();

        TextView usernameTextView = findViewById(R.id.usernameText);
        TextView accountTypeTextView = findViewById(R.id.accountTypeText);

        usernameTextView.setText(username + "!");
        accountTypeTextView.setText(accountType);

        // Check for logout button being clicked
        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LessorPostLoginActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Button to navigate to adding category page
        Button addButton = findViewById(R.id.addbutton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessorPostLoginActivity.this, AddItemActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    // Displays list of categories
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
                ItemAdapter itemAdapter = new ItemAdapter(LessorPostLoginActivity.this, lessorItemList);
                lessorItemsListView.setAdapter(itemAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LessorPostLoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
