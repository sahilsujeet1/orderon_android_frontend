package com.projects.orderon.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.projects.orderon.R;
import com.projects.orderon.RecyclerViewInterface;
import com.projects.orderon.adapters.SelectAddressRecyclerViewAdapter;
import com.projects.orderon.models.Address;
import com.projects.orderon.models.MenuItem;
import com.projects.orderon.models.Store;
import com.projects.orderon.viewModels.CartViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressPayment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressPayment extends Fragment implements RecyclerViewInterface {

    private static final String TAG = "AddressPayment";
    private static final String MID = "NqTKuA63797783011362";
//    private static final String MID = "EscBDe97704260989472";

    private static final String CALLBACKURL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=";
//    private static final String CALLBACKURL = "http://localhost:3000/paytmGateway/verifyPayment/";
    private static final int paytmRequestCode = 5;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioGroup paymentModeGroup, addressRadioGroup;
    private RadioButton radioButton;
    private Button newAdd, orderOn;
    private ProgressBar progressBar;
    private View view;
    private FirebaseUser currentUser;
    private SelectAddressRecyclerViewAdapter addressAdapter;
    private ArrayList<Address> addresses;

    private CartViewModel cartViewModel;
    private Address selectedAddress;
    private String paymentMode = "";
    private int totalAmount;
    String custOrderID, sellerOrderID;
    private ArrayList<MenuItem> cartItems;
    Timestamp now;

    public AddressPayment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressPayment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressPayment newInstance(String param1, String param2) {
        AddressPayment fragment = new AddressPayment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        cartViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(CartViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_address_payment, container, false);

        orderOn = view.findViewById(R.id.checkoutOrderOn);
        newAdd = view.findViewById(R.id.newAddressBtn);
        progressBar = view.findViewById(R.id.addressPaymentProgressBar);

        getParentFragmentManager().setFragmentResultListener("cartAmount", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                totalAmount = bundle.getInt("amount");
                Log.d(TAG, "onFragmentResult: AMOUNT: " + totalAmount);
            }
        });

        paymentModeGroup = view.findViewById(R.id.paymentModeGroup);
        addressRadioGroup = view.findViewById(R.id.addressRadioGroup);

        int id = paymentModeGroup.getCheckedRadioButtonId();

        paymentModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                paymentMode = radioButton.getText().toString().equals("Cash On Delivery") ? "COD" : "Online" ;
                Log.d(TAG, "onCheckedChanged: " + radioButton.getText().toString());
            }
        });

        getAddresses();

        orderOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedAddress != null && paymentMode.length() > 0) {
                    placeOrder();
                }
            }
        });

        newAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NewAddress()).addToBackStack("AddressPayment").commit();
            }
        });

        return view;
    }

    public void getAddresses() {
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView savedAddRecyclerView = view.findViewById(R.id.selectAddRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        savedAddRecyclerView.setLayoutManager(layoutManager);
        savedAddRecyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                .collection("addresses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    addresses = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> address = document.getData();
                        addresses.add(new Address(document.getId(), address.get("fullName").toString(), address.get("street").toString(), address.get("city").toString(),
                                address.get("state").toString(), address.get("pin").toString(), address.get("mobile").toString()));
                    }

                    addressAdapter = new SelectAddressRecyclerViewAdapter(view.getContext(), addresses, AddressPayment.this);

                    savedAddRecyclerView.setAdapter(addressAdapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        selectedAddress = addresses.get(position);
        Log.d(TAG, "onItemClick: ADDRESS: " + selectedAddress.toString());
    }

    @Override
    public void onStoreClick(int position, Store store) {

    }

    @Override
    public void onResume() {
        super.onResume();
        addresses = new ArrayList<>();
        getAddresses();
    }

    private void placeOrder() {
        Log.d(TAG, "placeOrder: INSIDE PLACE ORDER");
        cartItems = cartViewModel.cart.getValue();
        custOrderID = generateID();
        sellerOrderID = custOrderID;

        progressBar.setVisibility(View.VISIBLE);

        if(paymentMode.equals("COD")) {
            ArrayList<Map<String, Object>> orderDetailsCust = orderSeller(cartItems, paymentMode, selectedAddress,currentUser.getUid(), null);
            String finalCustOrderID = orderCustomer(orderDetailsCust, currentUser.getUid(), paymentMode, selectedAddress, null);
        } else if(paymentMode.equals("Online")) {
            orderOnline();
        }
    }

    private String generateID() {
        Log.d(TAG, "generateID: INSIDE GENERATE ID");
        now = Timestamp.now();
        Date no = now.toDate();
        String mm = Integer.toString(no.getMonth()+1);
        String dd = Integer.toString(no.getDate());
        String yyyy = Integer.toString(no.getYear());
        String time = Long.toString(no.getTime());
        String id = "OD" + dd + "-" + mm + "-" + yyyy + "-" + time;

        return id;
    }

    private String randomID() {

        Log.d(TAG, "randomID: INSIDE RANDOM ID");

        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
        int string_length = 10;
        String randomstring = "";
        for (int i = 0; i < string_length; i++) {
            int rnum = (int) (Math.random() * chars.length());
            randomstring += chars.substring(rnum, rnum + 1);
        }
        return randomstring;
    }

    private ArrayList<Map<String, Object>> orderSeller(ArrayList<MenuItem> cart, String mode, Address address, String uid, Map<String, Object> txnDetails) {

        Log.d(TAG, "orderSeller: INSIDE ORDER SELLER");

        sellerOrderID = custOrderID + "-" + randomID();
        ArrayList<Map<String, Object>> orderDetails = new ArrayList<>();
        Map<String, ArrayList<MenuItem>> storesCart = new HashMap<>();
        for(MenuItem item:cart) {
            ArrayList<MenuItem> storeCart = storesCart.get(item.getStoreId());
            if(storeCart == null) {
                storeCart = new ArrayList<>();
            }
            storeCart.add(item);
            storesCart.put(item.getStoreId(), storeCart);
        }

        Log.d(TAG, "orderSeller: STORESCART: " + storesCart.toString());

        for(Map.Entry<String, ArrayList<MenuItem>> entry:storesCart.entrySet()) {
            int amount = 0;
            ArrayList<MenuItem> storeCart = entry.getValue();
            for(MenuItem item:storeCart)
                amount += item.getNetPrice();

            Map<String, Object> content = new HashMap<>();
            content.put("custId", uid);
            content.put("orderID", sellerOrderID);
            content.put("orderedAt", now);
            content.put("status", "new");
            content.put("mode", mode);
            content.put("txnDetails", txnDetails);
            content.put("paymentId", "");
            content.put("amount", amount);
            content.put("orderItems", storeCart);
            content.put("shippingAddress", address);
            content.put("fine", 0);

            Log.d(TAG, "orderSeller: CONTENT SLLLER: " + content.toString());

            orderDetails.add(content);

            FirebaseFirestore.getInstance().collection(storeCart.get(0).getStoreType())
                    .document(entry.getKey()).collection("orders").add(content).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                        Log.d(TAG, "onComplete: SELLER ORDER SAVED");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onFailure: " + e.toString());
                }
            });
        }
        Log.d(TAG, "orderSeller: ORDER DETAILS: " + orderDetails.toString());
        return orderDetails;
    }

    private String orderCustomer(ArrayList<Map<String, Object>> orderDetailsCust, String uid, String mode, Address selectedAddress, Map<String, Object> txnDetails) {

        Log.d(TAG, "orderCustomer: INSIDE ORDER CUSTOMER");
        Log.d(TAG, "orderCustomer: FROM SELLER" + orderDetailsCust.toString());

        Map<String, Object> content = new HashMap<>();

        content.put("orders", orderDetailsCust);
        content.put("totAmount", totalAmount);
        content.put("mode", mode);
        content.put("txnDetails", txnDetails);
        content.put("orderedAt", now);
        content.put("shippingAddress", selectedAddress);
        content.put("status", "ordered");
        content.put("fine", 0);

        Log.d(TAG, "orderSeller: CONTENT CUSTOMER: " + content.toString() + "CUSTordERid: " + custOrderID);

        FirebaseFirestore.getInstance().collection("users").document(uid)
                .collection("orders").document(custOrderID).set(content)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                            emptyCart();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new HomeFragment()).commit();
                            Log.d(TAG, "onComplete: CUSTOMER ORDER SAVED SUCCESS");
                        } else {
                            Log.d(TAG, "onComplete: ERROR OCCURRED DURING CUSTOMER");
                            Toast.makeText(getContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

        return custOrderID;
    }

    private void emptyCart() {
        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                .collection("cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    for(DocumentSnapshot doc:docs) {
                        doc.getReference().delete();
                    }

                    cartViewModel.cart.setValue(new ArrayList<>());
                }
            }
        });
    }

    private void orderOnline() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "https://orderon-backend.herokuapp.com/paytmGateway/genCheckSum/";
//        String url = "http://localhost:3000/paytmGateway/genCheckSum";
        Map<String, String> params = new HashMap<>();
        params.put("orderID", custOrderID);
        params.put("custId", currentUser.getUid());
        params.put("amount", Integer.toString(totalAmount));
        JSONObject jsonParam = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonParam,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: JSON_RESPONSE: " + response.toString());
                    JSONObject resBody = response.getJSONObject("body");
                    JSONObject resHead = response.getJSONObject("head");
                    String clientId = currentUser.getUid();
                    String txnToken = resBody.getString("txnToken");

                    PaytmOrder paytmOrder = new PaytmOrder(custOrderID, MID, txnToken, Integer.toString(totalAmount), CALLBACKURL+custOrderID);

                    TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {

                        @Override
                        public void onTransactionResponse(@Nullable Bundle response) {
//                            Toast.makeText(getContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onActivityResult: Redirect Response: " + response.toString());
                            storeDataAfterOnlinePayment(null, response, "redirect");
                        }

                        @Override
                        public void networkNotAvailable() {

                        }

                        @Override
                        public void onErrorProceed(String s) {

                        }

                        @Override
                        public void clientAuthenticationFailed(String s) {

                        }

                        @Override
                        public void someUIErrorOccurred(String s) {

                        }

                        @Override
                        public void onErrorLoadingWebPage(int i, String s, String s1) {

                        }

                        @Override
                        public void onBackPressedCancelTransaction() {

                        }

                        @Override
                        public void onTransactionCancel(String s, Bundle bundle) {

                        }
                    });

                    transactionManager.setAppInvokeEnabled(true);
                    transactionManager.setShowPaymentUrl("https://securegw-stage.paytm.in/theia/api/v1/showPaymentPage");
                    transactionManager.startTransaction(getActivity(), paytmRequestCode);
                    if(clientId.length() > 0)
                        transactionManager.startTransactionAfterCheckingLoginStatus(getActivity(), clientId, paytmRequestCode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: Error in sending request" + error.toString());
            }
        }
        );

        requestQueue.add(jsonObjectRequest);

    }

    private void storeDataAfterOnlinePayment(@Nullable String response, @Nullable Bundle res, String respType) {

        try {
            if(respType.equals("app")) {
                JSONObject resp = new JSONObject(response.toString());
                if(resp.getString("STATUS").equals("TXN_SUCCESS")) {
                    Map<String, Object> txnDetails = new HashMap<>();
                    txnDetails.put("bankTransId", resp.getString("BANKTXNID"));
                    txnDetails.put("transDate", resp.getString("TXNDATE"));
                    txnDetails.put("transId", resp.getString("TXNID"));

                    Log.d(TAG, "storeDataAfterOnlinePayment: SUCCESS");

                ArrayList<Map<String, Object>> orderDetailsCust = orderSeller(cartItems, paymentMode, selectedAddress,currentUser.getUid(), txnDetails);
                String finalCustOrderID = orderCustomer(orderDetailsCust, currentUser.getUid(), paymentMode, selectedAddress, txnDetails);
                } else {
                    Toast.makeText(getContext(), "An error occurred. Amount will be refunded if deducted.", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new Cart()).commit();
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                if(res != null && res.getString("STATUS").equals("TXN_SUCCESS")) {
                    Map<String, Object> txnDetails = new HashMap<>();
                    txnDetails.put("bankTransId", res.getString("BANKTXNID"));
                    txnDetails.put("transDate", res.getString("TXNDATE"));
                    txnDetails.put("transId", res.getString("TXNID"));

                    Log.d(TAG, "storeDataAfterOnlinePayment: SUCCESS");

                    ArrayList<Map<String, Object>> orderDetailsCust = orderSeller(cartItems, paymentMode, selectedAddress,currentUser.getUid(), txnDetails);
                    String finalCustOrderID = orderCustomer(orderDetailsCust, currentUser.getUid(), paymentMode, selectedAddress, txnDetails);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == paytmRequestCode && data != null) {
            Log.d(TAG, "onActivityResult: " + data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"));
            storeDataAfterOnlinePayment(data.getStringExtra("response"), null, "app");
        }
    }
}