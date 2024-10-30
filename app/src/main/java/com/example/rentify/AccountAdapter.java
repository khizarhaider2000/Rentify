package com.example.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AccountAdapter extends ArrayAdapter<Account> {
    private Activity context;
    private List<Account> accounts;

    public AccountAdapter(Activity context, List<Account> accounts) {
        super(context, R.layout.activity_account_info, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_account_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.nameTextView);
        TextView textViewEmail = listViewItem.findViewById(R.id.emailTextView);
        TextView textViewUsername = listViewItem.findViewById(R.id.userNameTextView);

        Account account = accounts.get(position);
        textViewName.setText("Name: " + account.getFirstName() + " " + account.getLastName());
        textViewEmail.setText("Email: " + account.getEmail());
        textViewUsername.setText(account.getUsername());

        return listViewItem;
    }
}
