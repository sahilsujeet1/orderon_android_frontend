package com.projects.orderon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "homeRecyclerViewAdapter";

    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<Integer> itemURLs = new ArrayList<>();
    private Context context;

    public HomeRecyclerViewAdapter(Context context, ArrayList<String> itemNames, ArrayList<Integer> itemURLs) {
        this.itemNames = itemNames;
        this.itemURLs = itemURLs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_display_scroll_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder holder");

        holder.image.setImageResource(itemURLs.get(position));

        holder.itemName.setText(itemNames.get(position));
    }

    @Override
    public int getItemCount() {
        return itemNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView itemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.homeItemImage);
            itemName = itemView.findViewById(R.id.itemName);
        }
    }
}
