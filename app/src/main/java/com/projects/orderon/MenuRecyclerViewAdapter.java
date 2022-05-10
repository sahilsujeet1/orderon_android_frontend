package com.projects.orderon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import models.MenuItem;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "menuItemListAdapter";

    private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    private Context context;

    ImageButton remove, add;

    public MenuRecyclerViewAdapter(Context ctx, ArrayList<MenuItem> itemsParameter) {
        this.items = itemsParameter;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "MenuItemsList onBindViewHolder holder");
        MenuItem item = items.get(position);
        holder.itemImage.setImageResource(item.getImageURL());
        holder.itemName.setText(item.getItemName());
        holder.itemDesc.setText(item.getDescription());
        holder.price.setText("Rs. " + Integer.toString(item.getPrice()));
        holder.qty.setText(Integer.toString(item.getQty()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemDesc;
        TextView price;
        TextView qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemDesc = itemView.findViewById(R.id.itemDesc);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.cartItemQuantity);

            remove = itemView.findViewById(R.id.cartRemoveItem);
            add = itemView.findViewById(R.id.cartAddItem);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    int qty = items.get(pos).getQty();
                    if(qty > 0) {
                        items.get(pos).setQty(--qty);
                        Log.d("Removeitem", "onClick: " + pos + " " + qty);
                        notifyItemChanged(pos);
                    }
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    int qty = items.get(pos).getQty();
                    if(qty >= 0) {
                        items.get(pos).setQty(++qty);
                        Log.d("Additem", "onClick: " + pos + " " + qty);
                        notifyItemChanged(pos);
                    }
                }
            });
        }
    }
}
