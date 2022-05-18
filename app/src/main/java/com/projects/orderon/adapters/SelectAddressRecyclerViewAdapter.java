package com.projects.orderon.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.projects.orderon.R;
import com.projects.orderon.RecyclerViewInterface;
import com.projects.orderon.models.Address;

public class SelectAddressRecyclerViewAdapter extends RecyclerView.Adapter<SelectAddressRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "selectAddListAdapter";
    private final RecyclerViewInterface recyclerViewInterface;

    private ArrayList<Address> addresses = new ArrayList<Address>();
    private Context context;
    int selectedPosition = -1;

    public SelectAddressRecyclerViewAdapter(Context ctx, ArrayList<Address> addressParameter, RecyclerViewInterface rVI) {
        this.addresses = addressParameter;
        this.context = ctx;
        this.recyclerViewInterface = rVI;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_card_radio, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "SavedAddressList onBindViewHolder holder");
        Address address = addresses.get(position);
        holder.address.setText(address.getStreet() + ", " + address.getCity() + ","
                + address.getState() + " - " + address.getPin());
        holder.mobile.setText("Mob: " + address.getMobile());

        holder.radioButton.setChecked(position
                == selectedPosition);

        holder.radioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                    {
                        if (b) {
                            selectedPosition = holder.getAdapterPosition();
                            recyclerViewInterface.onItemClick(selectedPosition);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton deleteBtn;
        TextView address, mobile;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface rVI) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.radioDeleteSavedAddBtn);
            address = itemView.findViewById(R.id.radioFullAddress);
            mobile = itemView.findViewById(R.id.radioMobNum);
            radioButton = itemView.findViewById(R.id.addressRadioButton);

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
