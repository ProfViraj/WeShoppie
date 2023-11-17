package com.example.weshoppie.CustomerDashboard.OrderHistory.SeeOrderDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SeeOrderDetails extends AppCompatActivity {
    TextView BillNumber, DateText, ShopName, TotalCost;
    String BillNo, Date, ShopID, UserId, CustID;
    ArrayList<OrderSeeModel> orderSeeModelArrayList;
    Intent fromAct;
    RecyclerView recyclerView;
    OrderSeeAdapter orderSeeAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_order_details);

        fromAct = getIntent();
        ShopID = fromAct.getStringExtra("ShopId");
        Date = fromAct.getStringExtra("Date");
        BillNo = fromAct.getStringExtra("BillNo");
        CustID = fromAct.getStringExtra("CustId");

        BillNumber = findViewById(R.id.bill_no_see);
        DateText = findViewById(R.id.date_see);
        ShopName = findViewById(R.id.shop_name_see);
        TotalCost = findViewById(R.id.total_cost_see);

        BillNumber.setText("Bill No: "+BillNo);
        DateText.setText("Date: "+Date);
        if (ShopID != null){
            db.collection("Shopkeeper").document(ShopID).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                ShopName.setText(task.getResult().get("Shop_Name").toString());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SeeOrderDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            db.collection("Customer").document(CustID).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                ShopName.setText(task.getResult().get("Name").toString());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SeeOrderDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        UserId = CurrentUser.getUid();
        db.collection("Orders").document(BillNo).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            TotalCost.setText(task.getResult().get("Amount").toString());
                        } else {
                            Toast.makeText(SeeOrderDetails.this, "Unable to retrieve the cost", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeOrderDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        recyclerView = findViewById(R.id.order_details_see_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderSeeModelArrayList = new ArrayList<OrderSeeModel>();
        orderSeeAdapter = new OrderSeeAdapter(SeeOrderDetails.this, orderSeeModelArrayList);
        recyclerView.setAdapter(orderSeeAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("Orders").document(BillNo).collection("Added_Products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(SeeOrderDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                OrderSeeModel opm = documentChange.getDocument().toObject(OrderSeeModel.class);
                                orderSeeModelArrayList.add(opm);
                            }
                            orderSeeAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}