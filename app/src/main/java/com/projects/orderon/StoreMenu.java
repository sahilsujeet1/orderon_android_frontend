package com.projects.orderon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.orderon.models.MenuItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreMenu extends Fragment {

    private static final String TAG = "StoreMenu";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;

    View view;
    MenuRecyclerViewAdapter menuAdapter;

    ArrayList<MenuItem> items;
    String storeType, storeId, storeName, storeAddress;

    TextView name, address;

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
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store_menu, container, false);

        name = view.findViewById(R.id.storeNameTitle);
        address = view.findViewById(R.id.storeAddressTitle);

        getParentFragmentManager().setFragmentResultListener("storeData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                storeType = bundle.getString("storeType");
                storeId = bundle.getString("storeId");
                storeName = bundle.getString("name");
                storeAddress = bundle.getString("address");

                Log.d(TAG, "onFragmentResult: " + storeId + " " + storeName + " " + storeType + " " + storeAddress);

                name.setText(storeName);
                address.setText(storeAddress);

                getItems();
            }
        });



        return view;
    }

    void getItems() {
        RecyclerView recyclerView = view.findViewById(R.id.menuRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        items = new ArrayList<MenuItem>();

        db.collection(storeType).document(storeId).collection("menu").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for(DocumentSnapshot doc:docs) {
                                Map<String, Object> data = doc.getData();
                                items.add(new MenuItem(doc.getId(), data.get("item").toString(),
                                        data.get("description").toString(), Integer.parseInt(data.get("price").toString()),
                                        Integer.parseInt(data.get("price").toString()), Integer.parseInt(data.get("quantity").toString()),
                                        data.get("imgURL").toString(), data.get("storeId").toString()));
                            }
                            menuAdapter = new MenuRecyclerViewAdapter(view.getContext(), items);
                            recyclerView.setAdapter(menuAdapter);
                        } else {
                            Toast.makeText(getContext(), "Failed to fetch menu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}