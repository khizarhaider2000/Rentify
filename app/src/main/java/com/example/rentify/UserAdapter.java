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

    // Constructor
    public UserAdapter(Activity context, List<User> users) {
        super(context, R.layout.activity_account_info, users);
        this.context = context;
        this.users = users;
    }

    // List view item to display user information
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_account_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.nameTextView);
        TextView textViewEmail = listViewItem.findViewById(R.id.emailTextView);
        TextView textViewUsername = listViewItem.findViewById(R.id.userNameTextView);

        User user = users.get(position);
        textViewUsername.setText(user.getUsername());

        if (user instanceof Renter) {
            Renter renter = (Renter) user;
            textViewName.setText("Name: " + renter.getFirstName() + " " + renter.getLastName());
            textViewEmail.setText("Email: " + renter.getEmail());
        } else if (user instanceof Lessor) {
            Lessor lessor = (Lessor) user;
            textViewName.setText("Name: " + lessor.getFirstName() + " " + lessor.getLastName());
            textViewEmail.setText("Email: " + lessor.getEmail());
        } else {
            textViewName.setText("Name: N/A");
            textViewEmail.setText("Email: N/A");
        }

        return listViewItem;
    }
}
