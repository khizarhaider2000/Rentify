package com.example.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private Activity context;
    private List<Item> items;

    // Constructor
    public ItemAdapter(Activity context, List<Item> items) {
        super(context, R.layout.activity_items_info, items);
        this.context = context;
        this.items = items;
    }

    // List view item to display category information
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_items_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.itemNameTextView);
        TextView textViewDescription = listViewItem.findViewById(R.id.desTextView);
        TextView textViewFee = listViewItem.findViewById(R.id.costTextView);
        TextView textViewCategory = listViewItem.findViewById(R.id.categoryTextView);
        TextView textViewTime = listViewItem.findViewById(R.id.timePeriodTextView);

        Item item = items.get(position);

        textViewName.setText("Item Name: " + item.getItemName());
        textViewDescription.setText("Description: " + item.getDescription());
        textViewFee.setText("Fee: $" + item.getFee());
        textViewCategory.setText("Category: " + item.getCategory());
        textViewTime.setText("Time Period: " + item.getTimePeriod() + " week(s)");

        return listViewItem;
    }
}