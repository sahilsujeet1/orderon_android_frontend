package com.projects.orderon.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projects.orderon.adapters.CartRecyclerViewAdapter;
import com.projects.orderon.R;
import com.projects.orderon.models.MenuItem;
import com.projects.orderon.viewModels.CartViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cart extends Fragment {

    private static final String TAG = "Cart";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CartRecyclerViewAdapter cartAdapter;
    private View view;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ProgressBar progressBar;
    private int totalQty, totalAmount;
    private TextView quantity, amount;
    private CartViewModel cartViewModel;
    private Button checkout;

    public Cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cart.
     */
    // TODO: Rename and change types and number of parameters
    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
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
        cartViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(CartViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        amount = view.findViewById(R.id.cartTotalAmountValue);
        quantity = view.findViewById(R.id.cartTotalQtyValue);
        checkout = view.findViewById(R.id.checkout);
        progressBar = view.findViewById(R.id.cartProgressBar);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("amount", totalAmount);
                getParentFragmentManager().setFragmentResult("cartAmount", bundle);


                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, new AddressPayment()).commit();
            }
        });

        getCartItems();

        return view;
    }

    public void getCartItems() {
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.cartRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        cartViewModel.getCart();
        cartViewModel.cart.observe((LifecycleOwner) getContext(), new Observer<ArrayList<MenuItem>>() {
            @Override
            public void onChanged(ArrayList<MenuItem> cartItems) {
                cartAdapter = new CartRecyclerViewAdapter(view.getContext(), cartItems);
                recyclerView.setAdapter(cartAdapter);

                totalAmount = 0;
                totalQty = 0;
                for(MenuItem item: cartItems) {
                    totalAmount += (item.getPrice() * item.getQuantity());
                    totalQty += item.getQuantity();
                }

                amount.setText(Integer.toString(totalAmount));
                quantity.setText(Integer.toString(totalQty));
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getCartItems();
    }
}