package com.projects.orderon.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.orderon.adapters.OrderHistoryRecyclerAdapter;
import com.projects.orderon.R;
import com.projects.orderon.models.Address;
import com.projects.orderon.models.Order;
import com.projects.orderon.models.OrderItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistory extends Fragment {

    private static final String TAG = "OrderHistory";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar progressBar;
    View view;

    ArrayList<Order> orders;
    OrderHistoryRecyclerAdapter orderHistoryAdapter;

    FirebaseFirestore db;
    FirebaseUser user;

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
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_history, container, false);
        progressBar = view.findViewById(R.id.ordersProgressBar);
        getOrderHistory();

        return view;
    }


    void getOrderHistory() {
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.orderHistoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        orders = new ArrayList<Order>();
        db.collection("users").document(user.getUid()).collection("orders").orderBy("orderedAt", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    ArrayList<Order> ordersToShow = new ArrayList<>();
                    for(DocumentSnapshot doc:docs) {
                        try {
                            Map<String, Object> data = doc.getData();
                            Timestamp orderedAt = (Timestamp) data.get("orderedAt");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            String date = sdf.format(orderedAt.toDate());

                            Map<String, Object> address = (Map<String, Object>) data.get("shippingAddress");
                            Address add = new Address(address.get("fullName").toString(),
                                    address.get("street").toString(),
                                    address.get("city").toString(),
                                    address.get("state").toString(),
                                    address.get("pin").toString(),
                                    address.get("mobile").toString()
                            );

                            ArrayList<OrderItem> items = new ArrayList<>();

                            List<Map<String, Object>> orders = (List<Map<String, Object>>) data.get("orders");
                            for(Map<String, Object> order: orders) {
                                List<Map<String, Object>> orderItems = (List<Map<String, Object>>) order.get("orderItems");

                                for(Map<String, Object> i:orderItems) {
                                    OrderItem item = new OrderItem(
                                            i.get("item").toString(),
                                            i.get("storeName").toString(),
                                            Integer.parseInt(i.get("quantity").toString()),
                                            Integer.parseInt(i.get("netPrice").toString()),
                                            i.get("imgURL").toString()
                                    );
                                    items.add(item);
                                }
                            }

                            Order order = new Order(
                                    doc.getId(),
                                    date,
                                    data.get("mode").toString(),
                                    add, items );

                            ordersToShow.add(order);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    orderHistoryAdapter = new OrderHistoryRecyclerAdapter(view.getContext(), ordersToShow);
                    recyclerView.setAdapter(orderHistoryAdapter);
                }

                progressBar.setVisibility(View.GONE);
            }
        });

    }
}