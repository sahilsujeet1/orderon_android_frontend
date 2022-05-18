package com.projects.orderon;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.net.URI;
import java.util.ArrayList;

import com.projects.orderon.models.CartItem;
import com.projects.orderon.models.MenuItem;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "cartRecyclerViewAdapter";

    private ArrayList<MenuItem> items = new ArrayList<>();
    private Context context;

    public CartRecyclerViewAdapter(Context context, ArrayList<MenuItem> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder holder");

        MenuItem item = items.get(position);
        holder.cartItemName.setText(item.getItemName().toString());
        holder.cartItemDesc.setText(item.getDescription());
        holder.cartItemPrice.setText("Rs. " + Integer.toString(item.getPrice()));
        holder.cartItemQty.setText(Integer.toString(item.getQty()));
        Glide.with(context).load(Uri.parse(item.getImageURL())).into(holder.cartItemImgUrl);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartItemName, cartItemDesc,
                cartItemPrice, cartItemQty;
        ShapeableImageView cartItemImgUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemDesc = itemView.findViewById(R.id.cartItemDesc);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            cartItemQty = itemView.findViewById(R.id.cartItemQuantity);
            cartItemImgUrl = itemView.findViewById(R.id.cartItemImage);
        }
    }
}
