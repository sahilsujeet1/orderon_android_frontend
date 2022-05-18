package com.projects.orderon.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.orderon.R;
import com.projects.orderon.RecyclerViewInterface;
import com.projects.orderon.adapters.SavedAddressRecyclerAdapter;
import com.projects.orderon.adapters.SelectAddressRecyclerViewAdapter;
import com.projects.orderon.models.Address;
import com.projects.orderon.models.MenuItem;
import com.projects.orderon.models.Store;
import com.projects.orderon.viewModels.CartViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressPayment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressPayment extends Fragment implements RecyclerViewInterface {

    private static final String TAG = "AddressPayment";

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

    private View view;
    private FirebaseUser currentUser;
    private SelectAddressRecyclerViewAdapter addressAdapter;
    private ArrayList<Address> addresses;

    private CartViewModel cartViewModel;
    private Address selectedAddress;
    private String paymentMode = "";
    private int totalAmonut;
    String custOrderID, sellerOrderID;
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

        getParentFragmentManager().setFragmentResultListener("cartAmount", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                totalAmonut = bundle.getInt("amount");
                Log.d(TAG, "onFragmentResult: AMOUNT: " + totalAmonut);
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

        return view;
    }

    public void getAddresses() {
        RecyclerView savedAddRecyclerView = view.findViewById(R.id.selectAddRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        savedAddRecyclerView.setLayoutManager(layoutManager);
        savedAddRecyclerView.setItemAnimator(new DefaultItemAnimator());

         addresses = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                .collection("addresses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> address = document.getData();
                        addresses.add(new Address(address.get("fullName").toString(), address.get("street").toString(), address.get("city").toString(),
                                address.get("state").toString(), address.get("pin").toString(), address.get("mobile").toString()));
                    }

                    addressAdapter = new SelectAddressRecyclerViewAdapter(view.getContext(), addresses, AddressPayment.this);

                    savedAddRecyclerView.setAdapter(addressAdapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
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

    private void placeOrder() {
        Log.d(TAG, "placeOrder: INSIDE PLACE ORDER");
        ArrayList<MenuItem> cartItems = cartViewModel.cart.getValue();

//        Map<String, Object> orderDetails = new HashMap<>();
//        orderDetails.put("shippingAddress", selectedAddress);
//        orderDetails.put("mode", paymentMode);
//        orderDetails.put("uid", currentUser.getUid());
//        orderDetails.put("amount", totalAmonut);

        custOrderID = generateID();
        sellerOrderID = custOrderID;

        ArrayList<Map<String, Object>> orderDetailsCust = orderSeller(cartItems, paymentMode, selectedAddress,currentUser.getUid(), "");
        String finalCustOrderID = orderCustomer(orderDetailsCust, currentUser.getUid(), paymentMode, selectedAddress, "");


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

    private void codCheckout() {

    }

    private ArrayList<Map<String, Object>> orderSeller(ArrayList<MenuItem> cart, String mode, Address address, String uid, String txnDetails) {

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
                    .document(entry.getKey()).set(content).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
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

    private String orderCustomer(ArrayList<Map<String, Object>> orderDetailsCust, String uid, String mode, Address selectedAddress, String txnDetails) {

        Log.d(TAG, "orderCustomer: INSIDE ORDER CUSTOMER");
        Log.d(TAG, "orderCustomer: FROM SELLER" + orderDetailsCust.toString());

        Map<String, Object> content = new HashMap<>();

        content.put("orders", orderDetailsCust);
        content.put("totAmount", totalAmonut);
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
//                            Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: CUSTOMER ORDER SAVED SUCCESS");
                        } else {
                            Log.d(TAG, "onComplete: ERROR OCCURRED DURING CUSTOMER");
                            Toast.makeText(getContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return custOrderID;
    }

//    public void onlineCheckout() {
////        PaytmPaymentsUtilRepository paymentsUtilRepository = PaytmSDK.getPaymentsUtilRepository()
//    }
//
//    void initiateTransaction() {
//
//        try {
//            JSONObject paytmParams = new JSONObject();
//
//            JSONObject body = new JSONObject();
//            body.put("requestType", "Payment");
//            body.put("mid", "YOUR_MID_HERE");
//            body.put("websiteName", "YOUR_WEBSITE_NAME");
//            body.put("orderId", "ORDERID_98765");
//            body.put("callbackUrl", "https://<callback URL to be used by merchant>");
//
//            JSONObject txnAmount = new JSONObject();
//            txnAmount.put("value", "1.00");
//            txnAmount.put("currency", "INR");
//
//            JSONObject userInfo = new JSONObject();
//            userInfo.put("custId", "CUST_001");
//            body.put("txnAmount", txnAmount);
//            body.put("userInfo", userInfo);
//
//
//            String checksum = PaytmChecksum.generateSignature(body.toString(), "YOUR_MERCHANT_KEY");
//
//            JSONObject head = new JSONObject();
//            head.put("signature", checksum);
//
//            paytmParams.put("body", body);
//            paytmParams.put("head", head);
//
//            String post_data = paytmParams.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//        /* for Staging */
//        URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
//
//        /* for Production */
//// URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
//
//        try {
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
//            requestWriter.writeBytes(post_data);
//            requestWriter.close();
//            String responseData = "";
//            InputStream is = connection.getInputStream();
//            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
//            if ((responseData = responseReader.readLine()) != null) {
//                System.out.append("Response: " + responseData);
//            }
//            responseReader.close();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//
//
//
//    }
}