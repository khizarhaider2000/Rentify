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


public class EditLessorAccountsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    ListView lessorAccountsView;
    private List<User> lessorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_lessor_accounts);

        databaseReference = FirebaseDatabase.getInstance().getReference("Lessors");
        lessorAccountsView = findViewById(R.id.lessorAccountsView);
        lessorList = new ArrayList<>();

        // Check if any items (lessors) in the listview are long clicked
        lessorAccountsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Lessor lessor = (Lessor) lessorList.get(i);
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

    // Displays list of lessors
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lessorList.clear(); // Clear list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lessor lessor = snapshot.getValue(Lessor.class);
                    lessorList.add(lessor);
                }
                UserAdapter lessorAdapter = new UserAdapter(EditLessorAccountsActivity.this, lessorList);
                lessorAccountsView.setAdapter(lessorAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditLessorAccountsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Updates the list of lessors shown
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

    // Removes lessor from database
    private void deleteLessor(String username) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Lessors").child(username);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Lessor Deleted", Toast.LENGTH_LONG).show();
    }
}
