package com.projects.orderon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import models.Store;

public class StoresList extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ArrayList<Store> stores;
    String storeType;
    TextView storeTypeTitle;
    StoreListRecyclerViewAdapter storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_list);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackgroundColor(0);
        storeTypeTitle = findViewById(R.id.storeTypeTitle);

        Intent storeListIntent = this.getIntent();
        storeType = storeListIntent.getStringExtra("type");
        storeType = storeType.substring(0,1).toUpperCase() + storeType.substring(1).toLowerCase();
        storeTypeTitle.setText(storeType);
        getStores();

    }

    void getStores() {
        RecyclerView recyclerView = findViewById(R.id.storeListRecyclerView);

        stores = new ArrayList<Store>();
        stores.add(new Store("Paprika", "Tower Chowk, Gaya", "restaurant", R.drawable.curry));
        stores.add(new Store("Spice Affair", "Manpur Bridge, Gaya", "restaurant", R.drawable.biryani));
        stores.add(new Store("Taj Darbar", "Bodhgaya, Gaya", "restaurant", R.drawable.north_indian));
        stores.add(new Store("Grill Inn", "A.P. Colony, Gaya", "restaurant", R.drawable.salad));
        stores.add(new Store("Yo! China", "S.P Colony, Gaya", "restaurant", R.drawable.south_indian));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        storeAdapter = new StoreListRecyclerViewAdapter(this, stores);
        recyclerView.setAdapter(storeAdapter);
    }
}


class StoreListRecyclerViewAdapter extends RecyclerView.Adapter<StoreListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "storeListAdapter";

    private ArrayList<Store> stores = new ArrayList<Store>();
    private Context context;

    public StoreListRecyclerViewAdapter(Context ctx, ArrayList<Store> storesParameter) {
        this.stores = storesParameter;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_card, parent, false);
        return new ViewHolder(view);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.storeImage);
            storeName = itemView.findViewById(R.id.storeName);
            storeAddress = itemView.findViewById(R.id.storeAddress);
        }
    }
}
