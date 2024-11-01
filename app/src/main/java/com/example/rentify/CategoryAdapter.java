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

    // Constructor
    public CategoryAdapter(Activity context, List<Category> categories) {
        super(context, R.layout.activity_category_info, categories);
        this.context = context;
        this.categories = categories;
    }

    // List view item to display category information
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_category_info, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.catNameTextView);
        TextView textViewDescription = listViewItem.findViewById(R.id.descriptionTextView);


        Category category = categories.get(position);
        textViewName.setText(category.getCategoryName());
        textViewDescription.setText(category.getDescription());

        return listViewItem;
    }
}
