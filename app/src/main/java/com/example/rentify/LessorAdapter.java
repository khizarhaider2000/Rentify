package com.example.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LessorAdapter extends ArrayAdapter<Lessor> {
    private Activity context;
    List<Lessor> lessors;

    public LessorAdapter(Activity context, List<Lessor> lessors) {
        super(context, R.layout.activity_lessor_account_info, lessors);
        this.context = context;
        this.lessors = lessors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_lessor_account_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.nameTextView);
        TextView textViewEmail = listViewItem.findViewById(R.id.emailTextView);
        TextView textViewUsername = listViewItem.findViewById(R.id.userNameTextView);

        Lessor lessor = lessors.get(position);
        textViewName.setText("Name: " + lessor.getFirstName() + " " + lessor.getLastName());
        textViewEmail.setText("Email: " + lessor.getEmail());
        textViewUsername.setText(lessor.getUsername());

        return listViewItem;
    }
}