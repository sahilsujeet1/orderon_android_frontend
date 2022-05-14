package com.projects.orderon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import com.projects.orderon.models.Store;

public class StoresListRecyclerViewAdapter extends RecyclerView.Adapter<StoresListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "storeListAdapter";
    private final RecyclerViewInterface recyclerViewInterface;

    private ArrayList<Store> stores = new ArrayList<Store>();
    private Context context;

    public StoresListRecyclerViewAdapter(Context ctx, ArrayList<Store> storesParameter, RecyclerViewInterface rVI) {
        this.stores = storesParameter;
        this.context = ctx;
        this.recyclerViewInterface = rVI;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_card, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "StoreList onBindViewHolder holder");
        Store store = stores.get(position);
        holder.image.setImageResource(store.getImageURL());
        holder.storeName.setText(store.getStoreName());
        holder.storeAddress.setText(store.getStoreAddress());
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView image;
        TextView storeName;
        TextView storeAddress;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface rVI) {
            super(itemView);
            image = itemView.findViewById(R.id.storeImage);
            storeName = itemView.findViewById(R.id.storeName);
            storeAddress = itemView.findViewById(R.id.storeAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rVI != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            rVI.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
