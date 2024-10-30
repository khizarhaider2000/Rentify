package com.example.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RenterAdapter extends ArrayAdapter<Renter> {
    private Activity context;
    List<Renter> renters;

    public RenterAdapter(Activity context, List<Renter> renters) {
        super(context, R.layout.activity_renter_account_info, renters);
        this.context = context;
        this.renters = renters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_renter_account_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.nameTextView2);
        TextView textViewEmail = listViewItem.findViewById(R.id.emailTextView2);
        TextView textViewUsername = listViewItem.findViewById(R.id.userNameTextView2);

        Renter renter = renters.get(position);
        textViewName.setText("Name: " + renter.getFirstName() + " " + renter.getLastName());
        textViewEmail.setText("Email: " + renter.getEmail());
        textViewUsername.setText(renter.getUsername());

        return listViewItem;
    }
}