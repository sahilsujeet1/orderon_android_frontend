package com.projects.orderon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
        Glide.with(context).load(store.getImageURL()).into(holder.image);
        //        holder.image.setImageBitmap(getImageBitmap(store.getImageURL()));
        holder.storeName.setText(store.getStoreName());
        holder.storeAddress.setText(store.getStoreAddress());
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
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
                            rVI.onStoreClick(pos, stores.get(pos));
                        }
                    }
                }
            });
        }
    }
}
