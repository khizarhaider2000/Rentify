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


public class EditCategoriesActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    ListView categoriesView;
    private List<Category> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_category);

        databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        categoriesView = findViewById(R.id.categoryView);
        categoriesList = new ArrayList<>();

        categoriesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) categoriesList.get(i);
                showUpdateDeleteDialog(category.getId(),category.getCategoryName());
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

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoriesList.clear(); // Clear list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    categoriesList.add(category);
                }
                CategoryAdapter categoryAdapter = new CategoryAdapter(EditCategoriesActivity.this, categoriesList);
                categoriesView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    private void showUpdateDeleteDialog(String categoryId,String categoryName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_category, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextCatName = (EditText) dialogView.findViewById(R.id.editTextCatName);
        final EditText editTextDescription  = (EditText) dialogView.findViewById(R.id.editTextDescription);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteCat);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateCat);

        dialogBuilder.setTitle(categoryName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory(categoryId);
                b.dismiss();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = editTextCatName.getText().toString().trim();
                String description = editTextDescription.getText().toString();
                if (!TextUtils.isEmpty(catName)) {
                    updateCategory(categoryId,catName, description);
                    b.dismiss();
                } else {
                    Toast.makeText(EditCategoriesActivity.this, "Name for Category required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteCategory(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Categories").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Category Deleted", Toast.LENGTH_LONG).show();
    }

    private void updateCategory(String categoryId, String categoryName, String descreption) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Categories").child(categoryId);

        FirebaseDatabase.getInstance().getReference().child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean exists = false;

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String existingCategoryName = categorySnapshot.child("categoryName").getValue(String.class);

                    if (categoryName.equalsIgnoreCase(existingCategoryName)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    Toast.makeText(EditCategoriesActivity.this, "Category already exists", Toast.LENGTH_SHORT).show();
                } else {
                    Category category = new Category(categoryId, categoryName, descreption);
                    dR.setValue(category);

                    Toast.makeText(getApplicationContext(), "Updated Category", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditCategoriesActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
