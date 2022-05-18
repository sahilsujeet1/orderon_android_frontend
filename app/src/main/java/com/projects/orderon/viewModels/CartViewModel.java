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
                                        Integer.parseInt(data.get("price").toString()),
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
        if(cartItem == null || cartItem.size() <= 0) cartItem = new ArrayList<>();

        if(cartItem.contains(item)) {
            int i = cartItem.indexOf(item);
            MenuItem y = cartItem.get(i);
            y.setQty(item.getQty());
            y.setNetPrice(item.getQty() * item.getPrice());
            cartItem.set(i,y);
            updateValue(item);
        } else {
            cartItem.add(item);
            sendToDB(item);
        }

        cart.setValue(cartItem);

    }

    public void removeItemFromCart(MenuItem item) {
        ArrayList<MenuItem> cartItem = cart.getValue();
        if(cartItem == null) cartItem = new ArrayList<>();

        int i = cartItem.indexOf(item);
        MenuItem y = cartItem.get(i);

        if(y.getQty() == 0) {
            cartItem.remove(i);
//                    remove from DB
            removeItem(item);
        } else {
            y.setQty(item.getQty());
            y.setNetPrice(item.getQty() * item.getPrice());
            cartItem.set(i, y);
            updateValue(item);
        }
        cart.setValue(cartItem);

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

    public void removeItem(MenuItem item) {

        if(cart.getValue().contains(item)) {
            ArrayList<MenuItem> cartItem = cart.getValue();
            cartItem.remove(item);
            cart.setValue(cartItem);
        }

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
                                    }
                                }
                            });
                        }
                    }
                });
    }

}
