package com.projects.orderon.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import com.projects.orderon.R;
import com.projects.orderon.models.MenuItem;
import com.projects.orderon.viewModels.CartViewModel;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "cartRecyclerViewAdapter";

    private ArrayList<MenuItem> items = new ArrayList<>();
    private Context context;
    private MenuItem item = null;
    private CartViewModel cartViewModel;

    public CartRecyclerViewAdapter(Context context, ArrayList<MenuItem> items) {
        this.items = items;
        this.context = context;
        cartViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CartViewModel.class);

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
        holder.cartItemName.setText(item.getItem().toString());
        holder.cartItemDesc.setText(item.getDescription());
        holder.cartItemPrice.setText("Rs. " + Integer.toString(item.getPrice()));
        holder.cartItemQty.setText(Integer.toString(item.getQuantity()));
        Glide.with(context).load(Uri.parse(item.getImgURL())).into(holder.cartItemImgUrl);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartItemName, cartItemDesc,
                cartItemPrice, cartItemQty;
        ShapeableImageView cartItemImgUrl;
        ImageButton remove, add, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemDesc = itemView.findViewById(R.id.cartItemDesc);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            cartItemQty = itemView.findViewById(R.id.cartItemQuantity);
            cartItemImgUrl = itemView.findViewById(R.id.cartItemImage);
            remove = itemView.findViewById(R.id.cartRemoveItem);
            add = itemView.findViewById(R.id.cartAddItem);
            delete = itemView.findViewById(R.id.cartItemDeleteButton);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    MenuItem curr = items.get(pos);
                    int qty = curr.getQuantity();

                    if(qty > 0) {
                        curr.setQuantity(qty-1);
                        item = curr;
                        cartViewModel.removeItemFromCart(item);
                        notifyItemChanged(pos);
                        Log.d(TAG, "onClick: Changed Item: " + item.getMenuItem().toString());
                    } else {
                        Toast.makeText(context, "Can't remove any further", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    MenuItem curr = items.get(pos);

                    int qty = curr.getQuantity();
                    if(qty > 0) {
                        curr.setQuantity(qty+1);
                        item = curr;
                        notifyItemChanged(pos);
                    }
                    cartViewModel.addItemToCart(item);
                    Log.d(TAG, "onClick: Changed Item: " + item.getMenuItem().toString());

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    MenuItem curr = items.get(pos);
                    cartViewModel.removeItem(curr);
                }
            });
        }
    }
}
