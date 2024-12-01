package com.example.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReceivedRequestAdapter extends ArrayAdapter<Request> {

    private Activity context;
    private List<Request> requests;

    // Constructor
    public ReceivedRequestAdapter(Activity context, List<Request> requests) {
        super(context, R.layout.activity_received_request_info, requests);
        this.context = context;
        this.requests = requests;
    }

    // List view item to display request information
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_received_request_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.itemNameTextView);
        TextView textViewLessorName = listViewItem.findViewById(R.id.renterNameTextView);
        TextView textViewStatus = listViewItem.findViewById(R.id.statusTextView);
        TextView textViewCategory = listViewItem.findViewById(R.id.categoryTextView);

        Request request = requests.get(position);

        textViewName.setText("Item Name: " + request.getItemName());
        textViewLessorName.setText("Renter Name: " + request.getRenterName());
        textViewStatus.setText("Status: " + request.getStatus());
        textViewCategory.setText("Category: " + request.getCategory());

        // Set the color of the status based on its value
        String status = request.getStatus();
        if ("accepted".equalsIgnoreCase(status)) {
            textViewStatus.setTextColor(context.getResources().getColor(R.color.status_accepted)); // Green
        } else if ("denied".equalsIgnoreCase(status)) {
            textViewStatus.setTextColor(context.getResources().getColor(R.color.status_denied)); // Red
        }

        return listViewItem;
    }

}