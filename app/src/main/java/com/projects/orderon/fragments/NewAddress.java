package com.projects.orderon.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projects.orderon.R;
import com.projects.orderon.models.Address;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAddress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAddress extends Fragment {

    private static final String TAG = "NewAddress";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText fullName, mobile, street, city, state, pincode;
    private Button saveBtn;
    private ProgressBar progressBar;
    private View view;

    private FirebaseUser user;

    public NewAddress() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAddress.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAddress newInstance(String param1, String param2) {
        NewAddress fragment = new NewAddress();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_address, container, false);

        fullName = view.findViewById(R.id.addFullNameInput);
        mobile = view.findViewById(R.id.addMobileInput);
        state = view.findViewById(R.id.addStateInput);
        street = view.findViewById(R.id.addStreetInput);
        city = view.findViewById(R.id.addCityInput);
        pincode = view.findViewById(R.id.addPincodeInput);
        saveBtn = view.findViewById(R.id.addSaveButton);
        progressBar = view.findViewById(R.id.addressProgressBar);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate(view)) {
                    save();
                }
            }
        });

        return view;
    }

    public boolean validate(View view) {
        boolean isValid = false;

        if(!TextUtils.isEmpty(fullName.getText().toString())) {
            if(!TextUtils.isEmpty(mobile.getText().toString()))
                if(!TextUtils.isEmpty(street.getText().toString()))
                    if(!TextUtils.isEmpty(city.getText().toString()))
                        if(!TextUtils.isEmpty(state.getText().toString()))
                            if(!TextUtils.isEmpty(pincode.getText().toString()))
                                isValid = true;
        }
        return isValid;
    }

    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        Address address = new Address(
                fullName.getText().toString(),
                street.getText().toString(),
                city.getText().toString(),
                state.getText().toString(),
                pincode.getText().toString(),
                mobile.getText().toString()
        );

        FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid()).collection("addresses").add(address)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getContext(), "Address saved successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });

    }
}