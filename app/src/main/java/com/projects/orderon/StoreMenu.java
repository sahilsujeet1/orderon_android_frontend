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

import models.MenuItem;
import models.Store;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreMenu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    MenuRecyclerViewAdapter menuAdapter;

    ArrayList<MenuItem> items;

    public StoreMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreMenu newInstance(String param1, String param2) {
        StoreMenu fragment = new StoreMenu();
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
        view = inflater.inflate(R.layout.fragment_store_menu, container, false);

        getItems();

        return view;
    }

    void getItems() {
        RecyclerView recyclerView = view.findViewById(R.id.menuRecyclerView);

        items = new ArrayList<MenuItem>();
        items.add(new MenuItem("Chicken Biryani", "Steamed rice with veggies or meat option with explosion of flavours", 160, R.drawable.biryani));
        items.add(new MenuItem("Salad", "Freshly chopped vegetables with a mix of Indian spices", 100, R.drawable.salad));
        items.add(new MenuItem("Paneer Butter Masala", "A little sweet paneer dish in a lot of butter", 230, R.drawable.north_indian));

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        menuAdapter = new MenuRecyclerViewAdapter(view.getContext(), items);
        recyclerView.setAdapter(menuAdapter);
    }
}