package com.example.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Activity context;
    private List<Category> categories;

    public CategoryAdapter(Activity context, List<Category> categories) {
        super(context, R.layout.activity_category_info, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_category_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.catNameTextView);
        TextView textViewDescription = listViewItem.findViewById(R.id.categoryDescription);


        Category category = categories.get(position);
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
