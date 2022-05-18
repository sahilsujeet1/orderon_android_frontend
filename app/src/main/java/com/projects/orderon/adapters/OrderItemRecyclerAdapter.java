package com.projects.orderon.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.projects.orderon.R;
import com.projects.orderon.models.OrderItem;

public class OrderItemRecyclerAdapter extends RecyclerView.Adapter<OrderItemRecyclerAdapter.ViewHolder> {

    private static final String TAG = "orderItemAdapter";

    private ArrayList<OrderItem> items;
    Context context;

    public OrderItemRecyclerAdapter(Context ctx, ArrayList<OrderItem> itemsParameter) {
        this.items = itemsParameter;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "OrderHistoryItem onBindViewHolder holder");
        OrderItem item = items.get(position);
        Log.d(TAG, item.getItemName());
        holder.itemName.setText(item.getItemName());
        holder.seller.setText(item.getSeller());
        holder.qty.setText("Qty: " + Integer.toString(item.getQty()));
        holder.amount.setText("Price: Rs." + Integer.toString(item.getAmount()));
        Glide.with(context).load(Uri.parse(item.getImgURL())).into(holder.img);
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