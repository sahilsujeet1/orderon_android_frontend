package com.projects.orderon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
        BottomNavigationView bottomNavigationView;
        HomeRecyclerViewAdapter homeAdapter;
        FloatingActionButton storeTypeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackgroundColor(0);

        setFoodScrollView();
        setGroceriesScrollView();
        setPharmacyScrollView();

        storeTypeButton = findViewById(R.id.storeTypeButton);
        storeTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreTypeOptions storeTypeFragment = new StoreTypeOptions();
                storeTypeFragment.show(getSupportFragmentManager(), "storeTypeFragment");
            }
        });

    }

    void setFoodScrollView() {
        RecyclerView recyclerView = findViewById(R.id.foodRecyclerView);
        Integer[] images = {R.drawable.north_indian, R.drawable.salad, R.drawable.biryani, R.drawable.south_indian, R.drawable.curry};
        String[] names = {"North Indian", "Salad", "Biryani", "Curry"};
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList(names));
        ArrayList<Integer> urlsList = new ArrayList<>(Arrays.asList(images));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeAdapter = new HomeRecyclerViewAdapter(this, namesList, urlsList);
        recyclerView.setAdapter(homeAdapter);
    }

    void setGroceriesScrollView() {
        RecyclerView recyclerView = findViewById(R.id.groceriesRecyclerView);
        Integer[] images = {R.drawable.milk, R.drawable.vegies, R.drawable.aashirvad, R.drawable.butter, R.drawable.rice, R.drawable.pepper};
        String[] names = {"Milk", "Vegetables", "Flour", "Butter", "Rice", "Pepper"};
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList(names));
        ArrayList<Integer> urlsList = new ArrayList<>(Arrays.asList(images));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeAdapter = new HomeRecyclerViewAdapter(this, namesList, urlsList);
        recyclerView.setAdapter(homeAdapter);
    }

    void setPharmacyScrollView() {
        RecyclerView recyclerView = findViewById(R.id.pharmacyRecyclerView);
        Integer[] images = {R.drawable.paracetamol, R.drawable.crocin, R.drawable.revital, R.drawable.dettol, R.drawable.benadryl};
        String[] names = {"Paracetamol", "Crocin", "Revital", "Dettol", "Benadryl"};
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList(names));
        ArrayList<Integer> urlsList = new ArrayList<>(Arrays.asList(images));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeAdapter = new HomeRecyclerViewAdapter(this, namesList, urlsList);
        recyclerView.setAdapter(homeAdapter);
    }
}