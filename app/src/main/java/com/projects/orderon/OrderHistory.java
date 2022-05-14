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

import com.projects.orderon.models.Order;
import com.projects.orderon.models.OrderItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    ArrayList<Order> orders;
    OrderHistoryRecyclerAdapter orderHistoryAdapter;

    public OrderHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistory newInstance(String param1, String param2) {
        OrderHistory fragment = new OrderHistory();
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
        view = inflater.inflate(R.layout.fragment_order_history, container, false);

        getOrderHistory();

        return view;
    }


    void getOrderHistory() {
        RecyclerView recyclerView = view.findViewById(R.id.orderHistoryRecyclerView);

        orders = new ArrayList<Order>();
        ArrayList<OrderItem> items1 = new ArrayList<OrderItem>();
        OrderItem i1 = new OrderItem("Amul Butter", "Grocery Store1", 1, 70, R.drawable.butter);
        OrderItem i2 = new OrderItem("Paneer Butter Masala", "Paprika", 1, 230, R.drawable.north_indian);

        items1.add(i1);
        items1.add(i2);

        ArrayList<OrderItem> items2 = new ArrayList<OrderItem>();
        items2.add(i1);
        items2.add(i2);

        orders.add(new Order("OD12345", "04-05-2022", "COD", "Gaya, Bihar", items1));
        orders.add(new Order("OD12345", "05-05-2022", "COD", "Gaya, Bihar", items2));

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        orderHistoryAdapter = new OrderHistoryRecyclerAdapter(view.getContext(), orders);
        recyclerView.setAdapter(orderHistoryAdapter);
    }
}