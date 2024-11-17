package com.example.rentify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ItemAdapterRecycler extends RecyclerView.Adapter<ItemAdapterRecycler.ViewHolder> {

    private Context context;
    private List<Item> itemList;

    public ItemAdapterRecycler(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);

        // Bind data
        holder.itemNameTextView.setText(item.getItemName());
        holder.itemCategoryTextView.setText("Category: " + item.getCategory());
        holder.itemFeeTextView.setText("Fee: $" + item.getFee());
        holder.itemDescriptionTextView.setText("Description: " + item.getDescription());

        // Long-click listener for deleting items
        holder.itemView.setOnLongClickListener(v -> {
            deleteItem(item.getLessorName(), item.getItemName());
            return true;
        });

        // Click listener for editing items
        holder.itemView.setOnClickListener(v -> {
            showEditDialog(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, itemCategoryTextView, itemFeeTextView, itemDescriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemCategoryTextView = itemView.findViewById(R.id.itemCategoryTextView);
            itemFeeTextView = itemView.findViewById(R.id.itemFeeTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
        }
    }

    private void deleteItem(String lessorName, String itemName) {
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Lessors").child(lessorName).child("Items").child(itemName);
        dRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to Delete Item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(Item item) {
        // Show the custom dialog for editing items
        EditItemDialog dialog = new EditItemDialog(context, item, updatedItem -> {
            DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Lessors").child(item.getLessorName()).child("Items");

            // Check if the item name has changed
            if (!item.getItemName().equals(updatedItem.getItemName())) {
                // Delete the old item
                dRef.child(item.getItemName()).removeValue();
            }

            // Add/Update the new item
            dRef.child(updatedItem.getItemName()).setValue(updatedItem).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to Update Item", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show();
    }

}
