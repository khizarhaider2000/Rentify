package com.example.rentify;

import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EditItemsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    DatabaseReference dRef;
    ListView lessorItemsListView;
    private List<Item> lessorItemList;

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
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_items);

        // Collect the username
        String username = getIntent().getStringExtra("username");

        databaseReference = FirebaseDatabase.getInstance().getReference("Lessors").child(username).child("Items");
        lessorItemsListView = findViewById(R.id.lessorItemsList);
        lessorItemList = new ArrayList<>();

        // Check if any items in the listview are long clicked
        lessorItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) lessorItemList.get(i);
                showUpdateDeleteDialog(item.getItemName(),item.getDescription(), item.getFee(), item.getTimePeriod(), item.getCategory(), item.getLessorName());
                return true;
            }
        });

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

    // Updates the list of items shown
    private void showUpdateDeleteDialog(String itemName,String itemDescription, double itemFee, int itemTimePeriod, String itemCategory, String lessorName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_item, null);
        dialogBuilder.setView(dialogView);

        dRef = FirebaseDatabase.getInstance().getReference();

        final EditText editTextItemName = (EditText) dialogView.findViewById(R.id.editTextItemName);
        final EditText editTextItemFee = (EditText) dialogView.findViewById(R.id.editTextFee);
        Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);
        final EditText editTextItemTimePeriod = (EditText) dialogView.findViewById(R.id.editTextTimePeriod);
        final EditText editTextDescription = (EditText) dialogView.findViewById(R.id.editTextDescription);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteItem);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateItem);

        dialogBuilder.setTitle(itemName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        ArrayList<String> categories = new ArrayList<String>();
        dRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String existingCategoryName = categorySnapshot.child("categoryName").getValue(String.class);
                    categories.add(existingCategoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditItemsActivity.this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditItemsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(lessorName, itemName);
                b.dismiss();
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String category = adapterView.getItemAtPosition(i).toString();
                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enteredItemName = editTextItemName.getText().toString().trim();
                        String enteredFee = editTextItemFee.getText().toString().trim();
                        String enteredTime = editTextItemTimePeriod.getText().toString().trim();
                        String enteredDescription = editTextDescription.getText().toString();

                        if ((enteredItemName.isEmpty()) || (enteredDescription.isEmpty()) || (enteredFee.isEmpty()) || (enteredTime.isEmpty())) {
                            Toast.makeText(EditItemsActivity.this, "Name, Description, Cost and Time Period for Item required", Toast.LENGTH_SHORT).show();
                        } else if (!(isAlpha(enteredItemName))) {
                            Toast.makeText(EditItemsActivity.this, "Name of item must only be of letters", Toast.LENGTH_SHORT).show();
                        }
                        // enteredFee is already validated to accept only numeric/decimal values in EditText of UI
                        // enteredTime is already validated to accept only integer values in EditText of UI

                        else {
                            updateItem(lessorName, itemName,enteredItemName, category, enteredDescription, enteredFee, enteredTime);
                            b.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Removes item from database
    private void deleteItem(String username, String itemName) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Lessors").child(username).child("Items").child(itemName);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
    }

    // Updates item information in database
    private void updateItem(String username, String oldItemName, String itemName, String category, String description, String fee, String timePeriod){

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Lessors").child(username).child("Items");
        // Create a new item with the updated name and other details
        Item updatedItem = new Item(username, itemName, category, description, Math.round(Double.parseDouble(fee)*100)/100D, Integer.parseInt(timePeriod));

        // Add the updated item with the new item name
        dR.child(itemName).setValue(updatedItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Delete the old item if the new item was successfully created
                if (!oldItemName.equalsIgnoreCase(itemName)) {
                    dR.child(oldItemName).removeValue();
                }
                Toast.makeText(getApplicationContext(), "Updated Item", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to Update Item", Toast.LENGTH_LONG).show();
            }
        });
    }

}