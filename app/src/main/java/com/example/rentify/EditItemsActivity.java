package com.example.rentify;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

        final EditText editTextItemName = (EditText) dialogView.findViewById(R.id.editTextItemName);
        final EditText editTextItemFee = (EditText) dialogView.findViewById(R.id.editTextFee);
        final EditText editTextItemCategory = (EditText) dialogView.findViewById(R.id.editTextCategory);
        final EditText editTextItemTimePeriod = (EditText) dialogView.findViewById(R.id.editTextTimePeriod);
        final EditText editTextDescription = (EditText) dialogView.findViewById(R.id.editTextDescription);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteItem);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateItem);

        dialogBuilder.setTitle(itemName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(lessorName, itemName);
                b.dismiss();
            }
        });


//        buttonUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String catName = editTextCatName.getText().toString().trim();
//                String description = editTextDescription.getText().toString();
//                if (!TextUtils.isEmpty(catName) && !TextUtils.isEmpty(description)) {
//                    updateCategory(categoryId,catName, description);
//                    b.dismiss();
//                } else {
//                    Toast.makeText(EditCategoriesActivity.this, "Name and Description for Category required", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    // Removes item from database
    private void deleteItem(String lessorName, String itemName) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Lessors").child(lessorName).child("Items").child(itemName);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
    }

    // Updates item information in database
    //private void updateCategory(){}

}