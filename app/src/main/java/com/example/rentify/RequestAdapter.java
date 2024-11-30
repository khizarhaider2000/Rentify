package com.example.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RequestAdapter extends ArrayAdapter<Request> {
    private Activity context;
    private List<Request> requests;

    // Constructor
    public RequestAdapter(Activity context, List<Request> requests) {
        super(context, R.layout.activity_request_info, requests);
        this.context = context;
        this.requests = requests;
    }

    // List view item to display category information
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_request_item, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.itemNameTextView);
        TextView textViewLessorName = listViewItem.findViewById(R.id.lessorNameTextView);
        TextView textViewStatus = listViewItem.findViewById(R.id.statusTextView);
        TextView textViewCategory = listViewItem.findViewById(R.id.categoryTextView);

        Request request = requests.get(position);

        textViewName.setText("Item Name: " + request.getItemName());
        textViewLessorName.setText("Lessor Name: " + request.getLessorName());   
        textViewStatus.setText("Status: " + request.getStatus());
        textViewCategory.setText("Category: " + request.getCategory());

        return listViewItem;
    }
}