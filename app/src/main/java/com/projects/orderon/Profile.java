package com.projects.orderon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import models.Address;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private SavedAddressRecyclerAdapter addressAdapter;

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

        TextView logout = view.findViewById(R.id.logoutBtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Login()).addToBackStack("Login").commit();
            }
        });

        getSavedAddresses();
        return view;
    }

    void getSavedAddresses() {
        RecyclerView savedAddRecyclerView = view.findViewById(R.id.savedAddRecyclerView);

        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(new Address("Manpur Patwatoli", "Gaya", "Bihar", "Buniyadganj", "823003"));
        addresses.add(new Address("Chandigarh University", "Mohali", "Punjab", "SAS Nagar", "140413"));
        addresses.add(new Address("Manpur Patwatoli2", "Gaya2", "Biha2r", "Buniyadganj2", "823003"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        savedAddRecyclerView.setLayoutManager(layoutManager);
        savedAddRecyclerView.setItemAnimator(new DefaultItemAnimator());

        addressAdapter = new SavedAddressRecyclerAdapter(view.getContext(), addresses, this);
        savedAddRecyclerView.setAdapter(addressAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }
}