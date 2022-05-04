package com.projects.orderon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import models.Store;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoresList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoresList extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    ArrayList<Store> stores;
    String storeType;
    TextView storeTypeTitle;
    StoresListRecyclerViewAdapter storeAdapter;

    public StoresList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoresList1.
     */
    // TODO: Rename and change types and number of parameters
    public static StoresList newInstance(String param1, String param2) {
        StoresList fragment = new StoresList();
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

        view = inflater.inflate(R.layout.fragment_stores_list, container, false);
        storeTypeTitle = view.findViewById(R.id.storeTypeTitle);

        getParentFragmentManager().setFragmentResultListener("type", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                storeType = bundle.getString("type");
                storeType = storeType.substring(0,1).toUpperCase() + storeType.substring(1).toLowerCase();
                storeTypeTitle.setText(storeType);
            }
        });

        getStores();
        return view;

    }

    void getStores() {
        RecyclerView recyclerView = view.findViewById(R.id.storeListRecyclerView);

        stores = new ArrayList<Store>();
        stores.add(new Store("Paprika", "Tower Chowk, Gaya", "restaurant", R.drawable.curry));
        stores.add(new Store("Spice Affair", "Manpur Bridge, Gaya", "restaurant", R.drawable.biryani));
        stores.add(new Store("Taj Darbar", "Bodhgaya, Gaya", "restaurant", R.drawable.north_indian));
        stores.add(new Store("Grill Inn", "A.P. Colony, Gaya", "restaurant", R.drawable.salad));
        stores.add(new Store("Yo! China", "S.P Colony, Gaya", "restaurant", R.drawable.south_indian));

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        storeAdapter = new StoresListRecyclerViewAdapter(view.getContext(), stores, this);
        recyclerView.setAdapter(storeAdapter);
    }

    @Override
    public void onItemClick(int position) {

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new StoreMenu())
                .addToBackStack(null).commit();

        Log.d("StoresList", "onItemClick: " + (position+1));
    }
}


