package com.projects.orderon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import models.Order;

public class OrderHistoryRecyclerAdapter extends RecyclerView.Adapter<OrderHistoryRecyclerAdapter.ViewHolder> {

    private static final String TAG = "orderHistoryAdapter";

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private ArrayList<Order> orders = new ArrayList<Order>();
    private Context context;

    public OrderHistoryRecyclerAdapter(Context ctx, ArrayList<Order> ordersParameter) {
        this.orders = ordersParameter;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_bundle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "OrderHistory onBindViewHolder holder");
        Order order = orders.get(position);
        Log.d(TAG, order.getItems().toString());
        holder.odId.setText("Order ID: " + order.getOrderId());
        holder.date.setText("Order Date: " + order.getDate());
        holder.address.setText("Shipping Add.: " + order.getAddress());
        holder.mode.setText("Payment Mode: " + order.getPaymentMode());

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.orderItemsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(order.getItems().size());

        OrderItemRecyclerAdapter orderItemRecyclerAdapter = new OrderItemRecyclerAdapter(order.getItems());
        holder.orderItemsRecyclerView.setLayoutManager(layoutManager);
        holder.orderItemsRecyclerView.setAdapter(orderItemRecyclerAdapter);
        holder.orderItemsRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView odId, date, address, mode;
        RecyclerView orderItemsRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            odId = itemView.findViewById(R.id.orderID);
            date = itemView.findViewById(R.id.orderDate);
            address = itemView.findViewById(R.id.shippingAddress);
            mode = itemView.findViewById(R.id.paymentMode);
            orderItemsRecyclerView = itemView.findViewById(R.id.orderItemsRecyclerView);

        }
    }
}