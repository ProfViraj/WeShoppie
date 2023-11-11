package com.example.weshoppie.CustomerDashboard.CustPlaceOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustAddedSeller.MySellers;
import com.example.weshoppie.CustomerDashboard.CustAddedSeller.SelectSeller;
import com.example.weshoppie.CustomerDashboard.CustAddedSeller.SellerAdapter;
import com.example.weshoppie.CustomerDashboard.CustAddedSeller.SellerShow;
import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PlaceOrderSellerList extends AppCompatActivity implements SelectSeller {

    public static final String TAG = "Place Order Seller List";
    String userID;
    ProgressBar prgg;
    RecyclerView recyclerView;
    ArrayList<SellerShow> sellerShowArrayList;
    SellerAdapter sellerAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_seller_list);

        prgg = findViewById(R.id.prgg);

        recyclerView = findViewById(R.id.recyclerSellerPlaceOrder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userID = CurrentUser.getUid();

        db = FirebaseFirestore.getInstance();
        sellerShowArrayList = new ArrayList<SellerShow>();
        sellerAdapter = new SellerAdapter(PlaceOrderSellerList.this,sellerShowArrayList, this);
        recyclerView.setAdapter(sellerAdapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("Customer").document(userID).collection("My_Sellers").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){
                            Log.d(TAG, "onEvent: error: "+error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                SellerShow cs = dc.getDocument().toObject(SellerShow.class);
                                cs.setDocumentID(dc.getDocument().getId());
                                sellerShowArrayList.add(cs);
                            }
                            sellerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(SellerShow sellerShow) {

        String docId = sellerShow.getDocumentID();
        Intent intent = new Intent(PlaceOrderSellerList.this, SeeProducts.class);
        intent.putExtra("OwnerId", docId);
        startActivity(intent);
        finish();
    }
}