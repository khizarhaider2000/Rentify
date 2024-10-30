package com.example.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private Activity context;
    private List<User> users;

    public UserAdapter(Activity context, List<User> users) {
        super(context, R.layout.activity_account_info, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_account_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.nameTextView);
        TextView textViewEmail = listViewItem.findViewById(R.id.emailTextView);
        TextView textViewUsername = listViewItem.findViewById(R.id.userNameTextView);

        User user = users.get(position);
        textViewName.setText("Name: " + user.getFirstName() + " " + user.getLastName());
        textViewEmail.setText("Email: " + user.getEmail());
        textViewUsername.setText(user.getUsername());

        return listViewItem;
    }
}
