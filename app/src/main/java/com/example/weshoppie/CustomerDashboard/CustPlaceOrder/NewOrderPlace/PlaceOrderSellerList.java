package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrderPlace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustAddedSeller.SelectSeller;
import com.example.weshoppie.CustomerDashboard.CustAddedSeller.SellerAdapter;
import com.example.weshoppie.CustomerDashboard.CustAddedSeller.SellerShow;
import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced.SeeEditableOrders;
import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlaceOrderSellerList extends AppCompatActivity implements SelectSeller {

    public static final String TAG = "Place Order Seller List";
    SearchView searchView;
    String userID, Number, Name, time;
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

        //Getting id's from the xml file
        prgg = findViewById(R.id.prgg);
        searchView = findViewById(R.id.searchViewCustPlaceOrderMySellers);
        searchView.clearFocus();

        userID = CurrentUser.getUid();
        db = FirebaseFirestore.getInstance();

        //Setting the time
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        time = formatter.format(currentTime);

        //Getting the customer Name and Number
        db.collection("Customer").document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Number = Objects.requireNonNull(task.getResult().get("Mobile_Number")).toString();
                            Name = Objects.requireNonNull(task.getResult().get("Name")).toString();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PlaceOrderSellerList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Setting the searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        //Setting the RecyclerView
        recyclerView = findViewById(R.id.recyclerSellerPlaceOrder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sellerShowArrayList = new ArrayList<SellerShow>();
        sellerAdapter = new SellerAdapter(PlaceOrderSellerList.this,sellerShowArrayList, this);
        recyclerView.setAdapter(sellerAdapter);
        EventChangeListener();

    }

    private void filterList(String newText) {
        ArrayList<SellerShow> filteredList = new ArrayList<SellerShow>();
        for (SellerShow item : sellerShowArrayList){
            if (item.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such seller added", Toast.LENGTH_SHORT).show();
        } else {
            sellerAdapter.setFilteredList(filteredList);
        }
    }
    //Realtime update for added sellers ***********************************************************************************************************
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
    //On seller selected *************************************************************************************
    @Override
    public void onItemClicked(SellerShow sellerShow) {

        Map<String,Object> BasicOrderData = new HashMap<>();
        BasicOrderData.put("Customer_ID",userID);
        BasicOrderData.put("Shopkeeper_ID",sellerShow.getDocumentID());
        BasicOrderData.put("Customer_Number",Number);
        BasicOrderData.put("Customer_Name",Name);
        BasicOrderData.put("Status","Unpacked");
        BasicOrderData.put("Accepted",false);
        BasicOrderData.put("Time",time);
        BasicOrderData.put("Amount","0");
        BasicOrderData.put("Delivered", false);
        BasicOrderData.put("Cancellation",false);
        BasicOrderData.put("Expected_Time","Null");
        //Check if the order is placed or in process ********************************************************************************
        db.collection("Orders")
                .whereEqualTo("Shopkeeper_ID", sellerShow.getDocumentID())
                .whereEqualTo("Customer_ID", userID)
                .whereEqualTo("Accepted",true)
                .whereEqualTo("Delivered", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                //Creating new order **************************************************************************************
                                db.collection("Orders").add(BasicOrderData)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()){
                                                    String docId = sellerShow.getDocumentID();
                                                    Intent intent = new Intent(PlaceOrderSellerList.this, SeeProducts.class);
                                                    intent.putExtra("OwnerId", docId);
                                                    intent.putExtra("BillNo", task.getResult().getId());
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PlaceOrderSellerList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                //If order is already placed ************************************************************************************
                                Toast.makeText(PlaceOrderSellerList.this, "Order is already Placed. Redirecting...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PlaceOrderSellerList.this, SeeEditableOrders.class));
                                finish();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PlaceOrderSellerList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}