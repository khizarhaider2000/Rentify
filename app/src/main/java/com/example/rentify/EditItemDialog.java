package com.example.rentify;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditItemDialog extends Dialog {

    public interface OnItemUpdateListener {
        void onItemUpdated(Item updatedItem);
    }

    public EditItemDialog(Context context, Item item, OnItemUpdateListener listener) {
        super(context);
        setContentView(R.layout.dialog_edit_item);

        // UI components
        EditText itemNameEditText = findViewById(R.id.editItemName);
        EditText itemFeeEditText = findViewById(R.id.editItemFee);
        EditText itemDescriptionEditText = findViewById(R.id.editItemDescription);
        EditText itemTimeEditText = findViewById(R.id.editItemTimePeriod);
        Spinner categorySpinner = findViewById(R.id.editItemCategorySpinner);
        Button updateButton = findViewById(R.id.updateItemButton);
        Button cancelButton = findViewById(R.id.cancelEditButton);

        // Populate fields with current item data
        itemNameEditText.setText(item.getItemName());
        itemFeeEditText.setText(String.valueOf(item.getFee()));
        itemDescriptionEditText.setText(item.getDescription());
        itemTimeEditText.setText(String.valueOf(item.getTimePeriod()));

        // Fetch categories and populate spinner
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");
        ArrayList<String> categories = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Load categories from Firebase
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.clear(); // Clear the list to avoid duplication
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("categoryName").getValue(String.class);
                    if (categoryName != null) {
                        categories.add(categoryName);
                    }
                }
                adapter.notifyDataSetChanged();

                // Set the spinner to the current item's category
                int position = categories.indexOf(item.getCategory());
                if (position >= 0) {
                    categorySpinner.setSelection(position);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to load categories: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Update action
        updateButton.setOnClickListener(v -> {
            String updatedName = itemNameEditText.getText().toString().trim();
            String updatedFee = itemFeeEditText.getText().toString().trim();
            String updatedDescription = itemDescriptionEditText.getText().toString().trim();
            String updatedTime = itemTimeEditText.getText().toString().trim();
            String updatedCategory = categorySpinner.getSelectedItem() != null
                    ? categorySpinner.getSelectedItem().toString()
                    : "";

            // Validate input
            if (updatedName.isEmpty() || updatedFee.isEmpty() || updatedDescription.isEmpty() || updatedTime.isEmpty() || updatedCategory.isEmpty()) {
                Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create updated item object
            Item updatedItem = new Item(item.getLessorName(), updatedName, updatedCategory, updatedDescription, Double.parseDouble(updatedFee), Integer.parseInt(updatedTime));

            // Return updated item to the listener
            listener.onItemUpdated(updatedItem);
            dismiss();
        });

        // Cancel action
        cancelButton.setOnClickListener(v -> dismiss());
    }
}
