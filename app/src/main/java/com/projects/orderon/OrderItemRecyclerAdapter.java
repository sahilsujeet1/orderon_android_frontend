package com.projects.orderon;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import models.OrderItem;

public class OrderItemRecyclerAdapter extends RecyclerView.Adapter<OrderItemRecyclerAdapter.ViewHolder> {

    private static final String TAG = "orderItemAdapter";

    private ArrayList<OrderItem> items;

    public OrderItemRecyclerAdapter(ArrayList<OrderItem> itemsParameter) {
        this.items = itemsParameter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "SOrderHistoryItem onBindViewHolder holder");
        OrderItem item = items.get(position);
        Log.d(TAG, item.getItemName());
        holder.itemName.setText(item.getItemName());
        holder.seller.setText(item.getSeller());
        holder.qty.setText(Integer.toString(item.getQty()));
        holder.amount.setText(Integer.toString(item.getAmount()));
        holder.img.setImageResource(item.getImgURL());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, seller;
        TextView qty, amount;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.orderItemName);
            seller = itemView.findViewById(R.id.orderItemSeller);
            qty = itemView.findViewById(R.id.orderItemQty);
            amount = itemView.findViewById(R.id.orderItemAmount);
            img = itemView.findViewById(R.id.orderItemImage);

        }
    }
}