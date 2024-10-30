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


public class EditRenterAccountsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    ListView renterAccountsView;
    private List<Renter> renterList;
    private RenterAdapter renterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_renter_accounts);

        databaseReference = FirebaseDatabase.getInstance().getReference("Renters");
        renterAccountsView = findViewById(R.id.renterAccountsView);
        renterList = new ArrayList<>();

        renterAccountsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Renter renter = renterList.get(i);
                showUpdateDeleteDialog(renter.getUsername(), renter.getEmail());
                return true;
            }
        });

        // Check for back button being clicked
        Button backButton = findViewById(R.id.backbutton2);

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
                renterList.clear(); // Clear list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Renter renter = snapshot.getValue(Renter.class);
                    renterList.add(renter);
                }
                RenterAdapter renterAdapter = new RenterAdapter(EditRenterAccountsActivity.this, renterList);
                renterAccountsView.setAdapter(renterAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    private void showUpdateDeleteDialog(final String username, final String email) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_delete_renter_accounts, null);
        dialogBuilder.setView(dialogView);

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteRenterButton);

        dialogBuilder.setTitle(username);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRenter(username, email);
                b.dismiss();
            }
        });
    }

    private void deleteRenter(String username, String email) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Renters").child(username);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Renter Deleted", Toast.LENGTH_LONG).show();
    }
}
