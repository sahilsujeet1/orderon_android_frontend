package com.projects.orderon;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.projects.orderon.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Login";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private EditText emailI, passwordI;
    private TextView signup;
    private String email, password;
    private Button loginBtn;
    private ImageButton googleBtn, fbBtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_login, container, false);

        emailI = view.findViewById(R.id.loginEmailInput);
        passwordI = view.findViewById(R.id.loginPasswordInput);
        loginBtn = view.findViewById(R.id.loginButton);
        signup = view.findViewById(R.id.signUpBtn);
        googleBtn = view.findViewById(R.id.googleLoginButton);
        fbBtn = view.findViewById(R.id.fbLoginButton);
        progressBar = view.findViewById(R.id.loginProgressBar);

        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailI.getText().toString();
                password = passwordI.getText().toString();

                if(validate(view)) {
                    loginWithEmailPassword();
                }
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Signup()).addToBackStack("Signup").commit();
            }
        });

        return view;
    }

    public boolean validate(View view) {
        boolean isValid = false;

        if(!TextUtils.isEmpty(email)) {
            if(!TextUtils.isEmpty(password))
                isValid = true;
        }
        return isValid;
    }

    public void loginWithEmailPassword() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(view.getContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(view.getContext(), "Logging in failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void googleLogin() {


        Log.i(TAG, "googleLogin: Inside googleTest");

        oneTapClient = Identity.getSignInClient(getContext());

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            Log.i(TAG, "googleLogin: Inside googleTest success");
                            getActivity().startIntentSenderForResult(result.getPendingIntent().getIntentSender(),
                                    REQ_ONE_TAP, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.toString());
                        e.printStackTrace();
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: inside onActivityResult");


        switch (requestCode) {
            case REQ_ONE_TAP:
                try {

                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        Log.d(TAG, "Got ID token." + idToken);

                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Log.d(TAG, "signInWithCredential:success" + user.toString());
                                            progressBar.setVisibility(View.VISIBLE);
                                            saveUserDataForGoogleLogin(user);

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            progressBar.setVisibility(View.GONE);
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    // ...
                }
                break;
        }
    }

    public void saveUserDataForGoogleLogin(FirebaseUser user) {
        Log.i(TAG, "saveUserDataForGoogleLogin: Inside saveUserData : user" + user.getDisplayName().toString());
        User usr = new User(user.getDisplayName().toString(), user.getEmail().toString(),
                "customer", user.getPhotoUrl().toString(), user.getUid(), Timestamp.now());

        Log.i(TAG, "saveUserDataForGoogleLogin: User: " + usr.getName().toString());

        DocumentReference doc = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
        Log.i(TAG, "saveUserDataForGoogleLogin: doc" + doc.toString());

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
//                        Store user data in firestore
                        Log.i(TAG, "onComplete: Document not exists");
                        doc.set(usr.getUser()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getView().getContext(), "User created successfully",
                                        Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onSuccess: User data stored");
                                progressBar.setVisibility(View.GONE);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new Profile()).commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.i(TAG, "onFailure: User data not stored");
                                Toast.makeText(getView().getContext(), "User data not stored!",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new Profile()).commit();
                            }
                        });
                    } else {
                        Log.i(TAG, "onComplete: Document exists");
                        progressBar.setVisibility(View.GONE);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new Profile()).commit();
                    }
                } else {

                    progressBar.setVisibility(View.GONE);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new Login()).commit();
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}