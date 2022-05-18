package com.projects.orderon.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.orderon.R;
import com.projects.orderon.RecyclerViewInterface;
import com.projects.orderon.adapters.StoresListRecyclerViewAdapter;
import com.projects.orderon.models.Store;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoresList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoresList extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "StoresList";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int PERMISSION_ID = 44;
    private double currLat, currLong;

    boolean shouldRefreshOnResume;


    View view;

    ArrayList<Store> stores;
    String storeType;
    TextView storeTypeTitle;
    StoresListRecyclerViewAdapter storeAdapter;

    public StoresList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoresList1.
     */
    // TODO: Rename and change types and number of parameters
    public static StoresList newInstance(String param1, String param2) {
        StoresList fragment = new StoresList();
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

        view = inflater.inflate(R.layout.fragment_stores_list, container, false);
        storeTypeTitle = view.findViewById(R.id.storeTypeTitle);

        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        getParentFragmentManager().setFragmentResultListener("type", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                storeType = bundle.getString("type");
                storeTypeTitle.setText(storeType.substring(0,1).toUpperCase() + storeType.substring(1).toLowerCase());

                getLocationThenStores();
            }
        });

        return view;

    }

    boolean checkPermission() {
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return false;
        }
    }


    void getLocationThenStores() {

        if(checkPermission()) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null) {
                        currLat = location.getLatitude();
                        currLong = location.getLongitude();

                        RecyclerView recyclerView = view.findViewById(R.id.storeListRecyclerView);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        stores = new ArrayList<Store>();

                        final GeoLocation center = new GeoLocation(currLat, currLong);
                        final double radiusInM = 10 * 1000;

                        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
                        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
                        for (GeoQueryBounds b : bounds) {
                            Query q = db.collection(storeType)
                                    .orderBy("geohash")
                                    .startAt(b.startHash)
                                    .endAt(b.endHash);

                            tasks.add(q.get());
                        }

                        Tasks.whenAllComplete(tasks)
                                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                                    @Override
                                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                                        for (Task<QuerySnapshot> task : tasks) {
                                            QuerySnapshot snap = task.getResult();
                                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                                Map<String, Object> result = doc.getData();

                                                GeoPoint location= (GeoPoint) result.get("location");
                                                double lat = location.getLatitude();
                                                double lng = location.getLongitude();

                                                // We have to filter out a few false positives due to GeoHash
                                                // accuracy, but most will match
                                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                                if (distanceInM <= radiusInM) {
                                                    matchingDocs.add(doc);
                                                }
                                            }
                                        }

                                        for(DocumentSnapshot doc:matchingDocs) {
                                            Map<String, Object> store = doc.getData();
                                            stores.add(new Store(
                                                    doc.getId(),
                                                    store.get("name").toString(),
                                                    store.get("street").toString(),
                                                    store.get("category").toString(),
                                                    store.get("imgURL").toString()
                                            ));

                                            Log.d(TAG, "onComplete: " + store.get("name").toString());
                                        }
//

                                        storeAdapter = new StoresListRecyclerViewAdapter(view.getContext(), stores, StoresList.this);
                                        recyclerView.setAdapter(storeAdapter);
                                    }
                                });


                    } else {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new HomeFragment()).commit();
                        Toast.makeText(getContext(), "Current location fetching failed. Please retry.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        shouldRefreshOnResume = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(shouldRefreshOnResume) {
            getLocationThenStores();
        }
    }

    @Override
    public void onStoreClick(int position, Store store) {
        Bundle bundle = new Bundle();
        bundle.putString("storeType", store.getStoreType());
        bundle.putString("storeId", store.getStoreId());
        bundle.putString("name", store.getStoreName());
        bundle.putString("address", store.getStoreAddress());
        getParentFragmentManager().setFragmentResult("storeData", bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new StoreMenu())
                .addToBackStack(null).commit();

        Log.d("StoresList", "onItemClick: " + (position+1));
    }

    @Override
    public void onItemClick(int position) {}
}




