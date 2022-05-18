package com.projects.orderon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.projects.orderon.fragments.Cart;
import com.projects.orderon.fragments.HomeFragment;
import com.projects.orderon.fragments.OrderHistory;
import com.projects.orderon.fragments.Profile;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
        BottomNavigationView bottomNavigationView;
        FloatingActionButton storeTypeButton;

        private static final String TAG = "MainActivity";
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private SignInClient oneTapClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackgroundColor(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.miHome);

        mAuth = FirebaseAuth.getInstance();

        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.add(R.id.fragment_container, new HomeFragment());
        fragTrans.commit();

        storeTypeButton = findViewById(R.id.storeTypeButton);
        storeTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreTypeOptions storeTypeFragment = new StoreTypeOptions();
                storeTypeFragment.show(getSupportFragmentManager(), "storeTypeFragment");
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.miHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                return true;

            case R.id.miOrderHistory:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderHistory()).commit();
                return true;

            case R.id.miCart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Cart()).commit();
                return true;

            case R.id.miUser:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
                return true;
        }

        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


}