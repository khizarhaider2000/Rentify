package com.example.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    ListView CategoriesView;
    private List<User> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.category_edit);

        databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        CategoriesView = findViewById(R.id.categoryView);
        categoriesList = new ArrayList<>();

        CategoriesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                L lessor = (Lessor) categoriesList.get(i);
                showUpdateDeleteDialog(lessor.getUsername());
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
                    Lessor lessor = snapshot.getValue(Lessor.class);
                    categoriesList.add(lessor);
                }
                UserAdapter lessorAdapter = new UserAdapter(EditCategoriesActivity.this, categoriesList);
                lessorAccountsView.setAdapter(lessorAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    private void showUpdateDeleteDialog(final String username) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_delete_accounts, null);
        dialogBuilder.setView(dialogView);

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteButton);

        dialogBuilder.setTitle(username);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLessor(username);
                b.dismiss();
            }
        });
    }

    private void deleteLessor(String username) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Lessors").child(username);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Lessor Deleted", Toast.LENGTH_LONG).show();
    }
}
