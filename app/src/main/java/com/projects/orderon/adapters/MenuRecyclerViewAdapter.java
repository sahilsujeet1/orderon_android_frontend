package com.projects.orderon.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.projects.orderon.R;
import com.projects.orderon.models.MenuItem;
import com.projects.orderon.viewModels.CartViewModel;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "menuItemListAdapter";

    private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    private Context context;

    private CartViewModel cartViewModel;

    ImageButton remove, add;
    MenuItem item = null;

    public MenuRecyclerViewAdapter(Context ctx, ArrayList<MenuItem> itemsParameter) {
        this.items = itemsParameter;
        this.context = ctx;

        cartViewModel = new ViewModelProvider((ViewModelStoreOwner) ctx).get(CartViewModel.class);
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
        Glide.with(context).load(Uri.parse(item.getImgURL())).into(holder.itemImage);
        holder.itemName.setText(item.getItem());
        holder.itemDesc.setText(item.getDescription());
        holder.price.setText("Rs. " + Integer.toString(item.getPrice()));
        holder.qty.setText(Integer.toString(item.getQuantity()));
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
                    MenuItem curr = items.get(pos);
                    int qty = curr.getQuantity();

                    if(qty > 0) {
                        curr.setQuantity(qty-1);
                        item = curr;
                        notifyItemChanged(pos);
                        cartViewModel.removeItemFromCart(item);
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
                    Log.d(TAG, "onClick: Position: " + pos);

                    if(qty == 0) {
                        curr.setQuantity(1);
                        item = curr;
                        notifyItemChanged(pos);
                    } else {
                        if(qty > 0) {
                            curr.setQuantity(qty+1);
                            item = curr;
                            notifyItemChanged(pos);
                        } else {

                        }
                    }
                    cartViewModel.addItemToCart(item);
                    Log.d(TAG, "onClick: Changed Item: " + item.getMenuItem().toString());

                }
            });
        }
    }
}
