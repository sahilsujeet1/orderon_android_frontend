package com.projects.orderon;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreTypeOptions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreTypeOptions extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button restaurant;
    Button groceries;
    Button pharmacy;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreTypeOptions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Store_Type_Options.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreTypeOptions newInstance(String param1, String param2) {
        StoreTypeOptions fragment = new StoreTypeOptions();
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
        View view = inflater.inflate(R.layout.fragment_store_type_options, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        restaurant = view.findViewById(R.id.restaurantType);
        groceries = view.findViewById(R.id.groceriesType);
        pharmacy = view.findViewById(R.id.pharmacyType);

        openStoreActivity(restaurant, "restaurant");
        openStoreActivity(groceries, "groceries");
        openStoreActivity(pharmacy, "pharmacy");

        return view;
    }


    void openStoreActivity(Button btn, String type) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                getParentFragmentManager().setFragmentResult("type", bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new StoresList()).addToBackStack("storeTypeMenu").commit();
                dismiss();

            }
        });
    }
}