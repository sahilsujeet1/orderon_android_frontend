package com.projects.orderon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HomeRecyclerViewAdapter homeAdapter;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        view = inflater.inflate(R.layout.fragment_home, container, false);


        setFoodScrollView();
        setGroceriesScrollView();
        setPharmacyScrollView();

        return view;
    }

    void setFoodScrollView() {
        RecyclerView recyclerView = view.findViewById(R.id.foodRecyclerView);
        Integer[] images = {R.drawable.north_indian, R.drawable.salad, R.drawable.biryani, R.drawable.south_indian, R.drawable.curry};
        String[] names = {"North Indian", "Salad", "Biryani", "Curry"};
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList(names));
        ArrayList<Integer> urlsList = new ArrayList<>(Arrays.asList(images));

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeAdapter = new HomeRecyclerViewAdapter(view.getContext(), namesList, urlsList);
        recyclerView.setAdapter(homeAdapter);
    }

    void setGroceriesScrollView() {
        RecyclerView recyclerView = view.findViewById(R.id.groceriesRecyclerView);
        Integer[] images = {R.drawable.milk, R.drawable.vegies, R.drawable.aashirvad, R.drawable.butter, R.drawable.rice, R.drawable.pepper};
        String[] names = {"Milk", "Vegetables", "Flour", "Butter", "Rice", "Pepper"};
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList(names));
        ArrayList<Integer> urlsList = new ArrayList<>(Arrays.asList(images));

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeAdapter = new HomeRecyclerViewAdapter(view.getContext(), namesList, urlsList);
        recyclerView.setAdapter(homeAdapter);
    }

    void setPharmacyScrollView() {
        RecyclerView recyclerView = view.findViewById(R.id.pharmacyRecyclerView);
        Integer[] images = {R.drawable.paracetamol, R.drawable.crocin, R.drawable.revital, R.drawable.dettol, R.drawable.benadryl};
        String[] names = {"Paracetamol", "Crocin", "Revital", "Dettol", "Benadryl"};
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList(names));
        ArrayList<Integer> urlsList = new ArrayList<>(Arrays.asList(images));

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeAdapter = new HomeRecyclerViewAdapter(view.getContext(), namesList, urlsList);
        recyclerView.setAdapter(homeAdapter);
    }
}