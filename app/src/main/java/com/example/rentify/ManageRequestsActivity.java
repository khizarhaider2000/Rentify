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
import java.util.Objects;


public class ManageRequestsActivity extends AppCompatActivity {

    ListView requestListView;
    private List<Request> requestsList;
    private List<String> requestKeys;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_requests);

        String username = getIntent().getStringExtra("username");
        databaseReference = FirebaseDatabase.getInstance().getReference("Lessors").child(username).child("Requests");

        requestListView = findViewById(R.id.requestsList);
        requestsList = new ArrayList<>();
        requestKeys = new ArrayList<>();

        requestListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Request request = (Request) requestsList.get(i);
                String key = requestKeys.get(i);
                showDenyAcceptDialog(key, request.getRenterName(), request.getStatus(), request.getItemName());
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

    // Display requests in a list
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestsList.clear(); // Clear list to avoid duplication
                requestKeys.clear(); // Clear list of keys as well

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Request request = snapshot.getValue(Request.class);
                    String key = snapshot.getKey();

                    requestsList.add(request);
                    requestKeys.add(key); // Store the key for each item

                }
                ReceivedRequestAdapter receivedRequestAdapter = new ReceivedRequestAdapter(ManageRequestsActivity.this, requestsList);
                requestListView.setAdapter(receivedRequestAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ManageRequestsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Pop up to deny or accept a request for item
    private void showDenyAcceptDialog(String key, String renterName, String status, String itemName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_deny_accept_request, null);
        dialogBuilder.setView(dialogView);

        final Button acceptButton = (Button) dialogView.findViewById(R.id.acceptButton);
        final Button denyButton = (Button) dialogView.findViewById(R.id.denyButton);

        dialogBuilder.setTitle(itemName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest(key, renterName, status, itemName);
                b.dismiss();
            }
        });

        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                denyRequest(key, renterName, status, itemName);
                b.dismiss();
            }
        });

    }

    private void acceptRequest(String key, String renterName, String status, String itemName) {
        // Accepting request and updating status
        DatabaseReference databaseReferenceRenterReq = FirebaseDatabase.getInstance().getReference("Renters").child(renterName).child("Requests");

        if (status.equalsIgnoreCase("pending")) {
            databaseReference.child(key).child("status").setValue("accepted");
            databaseReferenceRenterReq.child(key).child("status").setValue("accepted");
            Toast.makeText(ManageRequestsActivity.this, "Request accepted!", Toast.LENGTH_SHORT).show();
        }
    }

    private void denyRequest(String key, String renterName, String status, String itemName) {
        // Denying request and updating status
        DatabaseReference databaseReferenceRenterReq = FirebaseDatabase.getInstance().getReference("Renters").child(renterName).child("Requests");

        if (status.equalsIgnoreCase("pending")) {
            databaseReference.child(key).child("status").setValue("denied");
            databaseReferenceRenterReq.child(key).child("status").setValue("denied");
            Toast.makeText(ManageRequestsActivity.this, "Request denied", Toast.LENGTH_SHORT).show();

        }
    }
}