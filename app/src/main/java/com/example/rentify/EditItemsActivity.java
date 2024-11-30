package com.example.rentify;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EditItemsActivity extends AppCompatActivity {
    private DatabaseReference globalItemsReference;

    private DatabaseReference databaseReference;
    private DatabaseReference itemsReference;
    DatabaseReference dRef;
    ListView lessorItemsListView;
    private List<Item> lessorItemList;
    private List<String> itemKeys;

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
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_items);

        // Collect the username for lessor-specific reference
        String username = getIntent().getStringExtra("username");

        // Lessor-specific reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Lessors").child(username).child("Items");

        // Global "Items" node reference
        globalItemsReference = FirebaseDatabase.getInstance().getReference("Items");

        lessorItemsListView = findViewById(R.id.lessorItemsList);
        lessorItemList = new ArrayList<>();
        itemKeys = new ArrayList<>();

        lessorItemsListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Item item = lessorItemList.get(i);
            String key = itemKeys.get(i);
            showUpdateDeleteDialog(key, item);
            return true;
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Attach a single ValueEventListener to the Items node
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the lists to prevent duplication
                lessorItemList.clear();
                itemKeys.clear();

                // Loop through all children and populate the lists
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    String key = snapshot.getKey();

                    // Add item and key to the lists
                    lessorItemList.add(item);
                    itemKeys.add(key);
                }

                // Update the ListView
                updateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateListView() {
        // Check if adapter is already set
        if (lessorItemsListView.getAdapter() == null) {
            ItemAdapter itemAdapter = new ItemAdapter(EditItemsActivity.this, lessorItemList);
            lessorItemsListView.setAdapter(itemAdapter);
        } else {
            // Notify the adapter of dataset changes
            ((ItemAdapter) lessorItemsListView.getAdapter()).notifyDataSetChanged();
        }
    }






    private void showUpdateDeleteDialog(String key, Item item) {
        itemsReference = FirebaseDatabase.getInstance().getReference("Items").child(item.getCategory()).child(item.getItemName());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_item, null);
        dialogBuilder.setView(dialogView);

        dRef = FirebaseDatabase.getInstance().getReference();

        // UI components
        final EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        final EditText editTextItemFee = dialogView.findViewById(R.id.editTextFee);
        final EditText editTextItemTimePeriod = dialogView.findViewById(R.id.editTextTimePeriod);
        final EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        final Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);
        final Spinner timeUnitSpinner = dialogView.findViewById(R.id.timeUnitSpinner);
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDeleteItem);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdateItem);

        dialogBuilder.setTitle(item.getItemName());
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Pre-fill data
        editTextItemName.setText(item.getItemName());
        editTextItemFee.setText(String.valueOf(item.getFee()));
        editTextItemTimePeriod.setText(String.valueOf(item.getTimePeriod()));
        editTextDescription.setText(item.getDescription());

        // Populate category spinner
        ArrayList<String> categories = new ArrayList<>();
        dRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String existingCategoryName = categorySnapshot.child("categoryName").getValue(String.class);
                    categories.add(existingCategoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditItemsActivity.this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                categorySpinner.setAdapter(adapter);

                // Pre-select category
                int categoryPosition = adapter.getPosition(item.getCategory());
                categorySpinner.setSelection(categoryPosition);
                Log.d("onDataChange", "Items updated: " + lessorItemList.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Populate time unit spinner
        ArrayAdapter<String> timeUnitAdapter = new ArrayAdapter<>(
                EditItemsActivity.this,
                android.R.layout.simple_spinner_item,
                new String[]{"Hours", "Days", "Weeks"}
        );
        timeUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeUnitSpinner.setAdapter(timeUnitAdapter);

        // Pre-select time unit
        int timeUnitPosition = timeUnitAdapter.getPosition(item.getTimeUnit());
        timeUnitSpinner.setSelection(timeUnitPosition);

        buttonDelete.setOnClickListener(view -> {
            deleteItem(key);
            b.dismiss();
        });

        buttonUpdate.setOnClickListener(view -> {
            String enteredItemName = editTextItemName.getText().toString().trim();
            String enteredFee = editTextItemFee.getText().toString().trim();
            String enteredTime = editTextItemTimePeriod.getText().toString().trim();
            String enteredDescription = editTextDescription.getText().toString();
            String category = categorySpinner.getSelectedItem().toString();
            String timeUnit = timeUnitSpinner.getSelectedItem().toString();

            if (enteredItemName.isEmpty() || enteredDescription.isEmpty() || enteredFee.isEmpty() || enteredTime.isEmpty()) {
                Toast.makeText(EditItemsActivity.this, "Name, Description, Cost and Time Period for Item required", Toast.LENGTH_SHORT).show();
            } else if (!isAlpha(enteredItemName)) {
                Toast.makeText(EditItemsActivity.this, "Name of item must only be of letters", Toast.LENGTH_SHORT).show();
            } else {
                updateItem(key, item.getLessorName(), enteredItemName, category, enteredDescription, enteredFee, enteredTime, timeUnit);
                b.dismiss();
            }
        });
    }

    private void deleteItem(String key) {
        // Remove from lessor-specific path
        databaseReference.child(key).removeValue();

        // Remove from global Items node
        globalItemsReference.child(key).removeValue();

        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
    }


    private void updateItem(String key, String username, String itemName, String category, String description, String fee, String timePeriod, String timeUnit) {
        DatabaseReference dR = databaseReference.child(key);
        Item updatedItem = new Item(username, itemName, category, description, Math.round(Double.parseDouble(fee) * 100) / 100D, Integer.parseInt(timePeriod), timeUnit);

        dR.setValue(updatedItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Updated Item", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to Update Item", Toast.LENGTH_LONG).show();
            }
        });

        itemsReference.removeValue();
        dRef.child("Items").child(category).child(updatedItem.getItemName()).setValue(updatedItem);
    }
}
