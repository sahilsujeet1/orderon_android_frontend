package com.projects.orderon.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.orderon.R;
import com.projects.orderon.RecyclerViewInterface;
import com.projects.orderon.adapters.SavedAddressRecyclerAdapter;
import com.projects.orderon.models.Address;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "Profile";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerViewInterface recyclerViewInterface;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private View view;
    private SavedAddressRecyclerAdapter addressAdapter;

    private ShapeableImageView userDP;
    private TextView userName, userEmail, logout;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        logout = view.findViewById(R.id.logoutBtn);
        userDP = view.findViewById(R.id.profileImage);
        userEmail = view.findViewById(R.id.profileEmail);
        userName = view.findViewById(R.id.profileName);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Login()).commit();
            }
        });

//        getProfile();
//        getSavedAddresses();
        return view;
    }

    public void getProfile() {
        if(currentUser != null) {
            if(currentUser.getPhotoUrl() != null)
                Glide.with(getContext()).load(currentUser.getPhotoUrl()).into(userDP);
            userName.setText(currentUser.getDisplayName());
            userEmail.setText(currentUser.getEmail());
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d(TAG, "onStart: " + currentUser.getDisplayName());
            getProfile();
            getSavedAddresses();

            currentUser.reload();

        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Login()).addToBackStack("profile").commit();
        }
    }

    void getSavedAddresses() {
        RecyclerView savedAddRecyclerView = view.findViewById(R.id.savedAddRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        savedAddRecyclerView.setLayoutManager(layoutManager);
        savedAddRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Address> addresses = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                .collection("addresses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> address = document.getData();
                        addresses.add(new Address(address.get("fullName").toString(), address.get("street").toString(), address.get("city").toString(),
                                address.get("state").toString(), address.get("pin").toString(), address.get("mobile").toString()));
                    }

                    addressAdapter = new SavedAddressRecyclerAdapter(view.getContext(), addresses, recyclerViewInterface);

                    savedAddRecyclerView.setAdapter(addressAdapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

}