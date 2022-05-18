package com.projects.orderon.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.orderon.models.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartViewModel extends ViewModel {

    private static final String TAG = "CartViewModel";

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference firebaseCart = FirebaseFirestore.getInstance().collection("users").document(user.getUid())
            .collection("cart");
    public MutableLiveData<ArrayList<MenuItem>> cart = new MutableLiveData<>();

    public void getCart() {
        firebaseCart.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();

                            ArrayList<MenuItem> cartItems = new ArrayList<>();
                            if(cartItems == null) cartItems = new ArrayList<>();
                            for(DocumentSnapshot doc:docs) {
                                Map<String, Object> data = doc.getData();
                                cartItems.add(new MenuItem(
                                        data.get("id").toString(),
                                        data.get("item").toString(),
                                        data.get("description").toString(),
                                        Integer.parseInt(data.get("netPrice").toString()),
                                        Integer.parseInt(data.get("quantity").toString()),
                                        data.get("imgURL").toString(),
                                        data.get("storeId").toString()
                                ));
                            }
                            cart.setValue(cartItems);
                        } else {
                            Log.d(TAG, "onComplete: Failed to fetch cart!");
                        }
                    }
                });
    }

    public void addItemToCart(MenuItem item) {
        ArrayList<MenuItem> cartItem = cart.getValue();
        if(cartItem == null) cartItem = new ArrayList<>();
        boolean found = false;

        if(cartItem.size() > 0) {
            for(MenuItem x:cartItem) {
                if(x.getItemId() == item.getItemId()) {
                    x.setQty(item.getQty());
                    found = true;
                }
            }
        }

        if(found) {
//            update value in DB
            updateValue(item);
        } else {
            cartItem.add(item);
            cart.setValue(cartItem);
//            send to DB
            sendToDB(item);
        }

        Log.d(TAG, "addItemToCart: " + cart.getValue().get(0).getMenuItem().toString());


    }

    public void removeItemFromCart(MenuItem item) {
        ArrayList<MenuItem> cartItem = cart.getValue();
        if(cartItem == null) cartItem = new ArrayList<>();

        for(MenuItem x:cartItem) {
            if(x.getItemId() == item.getItemId()) {
                x.setQty(item.getQty());

                if(x.getQty() == 0) {
                    cartItem.remove(cartItem.indexOf(x));
                    cart.setValue(cartItem);
//                    remove from DB
                    removeItem(item);
                } else {
//                    update DB
                    updateValue(item);
                }

            }
        }
    }

    private void sendToDB(MenuItem item) {
        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .collection("cart").add(item.getMenuItem());
    }

    private void updateValue(MenuItem item) {

        firebaseCart.whereEqualTo("id",item.getItemId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            String updateId = docs.get(0).getId();

                            firebaseCart.document(updateId).set(item.getMenuItem()).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Updating value failed!");
                                }
                            });
                        }

                    }
                });
    }

    private void removeItem(MenuItem item) {
        firebaseCart.whereEqualTo("id",item.getItemId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            String deleteId = docs.get(0).getId();

                            firebaseCart.document(deleteId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete()) {
                                        ArrayList<MenuItem> cartItem = cart.getValue();
                                        if(cartItem == null) cartItem = new ArrayList<>();
                                        cartItem.remove(cartItem.indexOf(item));
                                        cart.setValue(cartItem);
                                    }
                                }
                            });
                        }
                    }
                });
    }


}
