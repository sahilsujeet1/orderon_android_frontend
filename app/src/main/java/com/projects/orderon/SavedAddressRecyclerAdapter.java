package com.projects.orderon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.projects.orderon.models.Address;

public class SavedAddressRecyclerAdapter extends RecyclerView.Adapter<SavedAddressRecyclerAdapter.ViewHolder> {

    private static final String TAG = "savedAddListAdapter";
    private final RecyclerViewInterface recyclerViewInterface;

    private ArrayList<Address> addresses = new ArrayList<Address>();
    private Context context;

    public SavedAddressRecyclerAdapter(Context ctx, ArrayList<Address> addressParameter, RecyclerViewInterface rVI) {
        this.addresses = addressParameter;
        this.context = ctx;
        this.recyclerViewInterface = rVI;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_card, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "SavedAddressList onBindViewHolder holder");
        Address address = addresses.get(position);
        holder.address.setText(address.getStreet() + ", " + address.getCity() + ","
        + address.getState() + " - " + address.getPincode());
        holder.mobile.setText("Mob: " + address.getMobile());
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton deleteBtn;
        TextView address, mobile;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface rVI) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.deleteSavedAddBtn);
            address = itemView.findViewById(R.id.fullAddress);
            mobile = itemView.findViewById(R.id.mobNum);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Remove btn clicked");
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
