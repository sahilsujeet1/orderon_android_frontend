package com.projects.orderon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.orderon.models.CartItem;
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

    private int totalQty, totalAmount;
    private TextView quantity, amount;
    private CartViewModel cartViewModel;

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

        getCartItems();

        return view;
    }

    public void getCartItems() {
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
                    totalAmount += (item.getPrice() * item.getQty());
                    totalQty += item.getQty();
                }

                amount.setText(Integer.toString(totalAmount));
                quantity.setText(Integer.toString(totalQty));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getCartItems();
    }
}