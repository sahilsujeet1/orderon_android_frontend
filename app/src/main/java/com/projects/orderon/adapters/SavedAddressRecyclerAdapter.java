package com.projects.orderon.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.orderon.R;
import com.projects.orderon.RecyclerViewInterface;
import com.projects.orderon.models.Address;

public class SavedAddressRecyclerAdapter extends RecyclerView.Adapter<SavedAddressRecyclerAdapter.ViewHolder> {

    private static final String TAG = "savedAddListAdapter";
    private final RecyclerViewInterface recyclerViewInterface;
    private FirebaseUser user;

    private ArrayList<Address> addresses = new ArrayList<Address>();
    private Context context;

    public SavedAddressRecyclerAdapter(Context ctx, ArrayList<Address> addressParameter, RecyclerViewInterface rVI) {
        this.addresses = addressParameter;
        this.context = ctx;
        this.recyclerViewInterface = rVI;
        user = FirebaseAuth.getInstance().getCurrentUser();
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
        holder.address.setText(address.getFullName() + ", " + address.getStreet() + ", " + address.getCity() + ","
        + address.getState() + " - " + address.getPin());
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
                    int pos = getAdapterPosition();
                    FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                            .collection("addresses").document(addresses.get(pos).getID()).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            addresses.remove(addresses.get(pos));
                            notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(itemView.getContext(), "Address deletion failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
