package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.SeeOrderPlaced;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.SeeProducts;
import com.example.weshoppie.CustomerDashboard.CustomerDashboardNew;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class OrderPlaced extends AppCompatActivity {
    Intent fromAct;
    String OrderID, ShopID, Date, UserId, Total;
    TextView BillNo, OrderDate, ShopName, TotalCostBill;
    Button ConfirmOrder;
    RecyclerView recyclerView;
    OrderedProductAdapter orderedProductAdapter;
    ArrayList<OrderedProductModel> orderedProductModelArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        BillNo = findViewById(R.id.bill_no);
        OrderDate = findViewById(R.id.date);
        ShopName = findViewById(R.id.shop_name_bill);
        TotalCostBill = findViewById(R.id.total_cost_bill);
        ConfirmOrder = findViewById(R.id.confirm_order);

        fromAct = getIntent();
        ShopID = fromAct.getStringExtra("ShopId");
        Date = fromAct.getStringExtra("Date");

        UserId = CurrentUser.getUid();
        //Getting OrderID
        db.collection("Orders").whereEqualTo("Customer_ID",UserId)
                .whereEqualTo("Shopkeeper_ID",ShopID)
                .whereEqualTo("Time", Date)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                OrderID = documentSnapshot.getId();
                                Total = documentSnapshot.get("Amount").toString();
                            }
                            BillNo.setText("Bill No: " + OrderID);
                            TotalCostBill.setText(Total + " Rs.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderPlaced.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        OrderDate.setText("Date : " + Date);

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
                        Toast.makeText(OrderPlaced.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



        recyclerView = findViewById(R.id.product_details_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderedProductModelArrayList = new ArrayList<OrderedProductModel>();
        orderedProductAdapter = new OrderedProductAdapter(OrderPlaced.this, orderedProductModelArrayList);
        recyclerView.setAdapter(orderedProductAdapter);

        EventChangeListener();

        ConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderPlaced.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you want to confirm order?")
                        .setIcon(R.drawable.baseline_add_task_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Orders").document(OrderID).update("Accepted",true)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(OrderPlaced.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(OrderPlaced.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });
    }

    private void EventChangeListener() {
        db.collection("Orders").whereEqualTo("Customer_ID",UserId)
                .whereEqualTo("Shopkeeper_ID",ShopID)
                .whereEqualTo("Time", Date)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                OrderID = documentSnapshot.getId();
                            }
                            db.collection("Orders").document(OrderID).collection("Added_Products")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @SuppressLint("NotifyDataSetChanged")
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (error != null){
                                                Toast.makeText(OrderPlaced.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            for (DocumentChange documentChange : value.getDocumentChanges()){
                                                if (documentChange.getType() == DocumentChange.Type.ADDED){
                                                    OrderedProductModel opm = documentChange.getDocument().toObject(OrderedProductModel.class);
                                                    orderedProductModelArrayList.add(opm);
                                                }
                                                if (documentChange.getType() == DocumentChange.Type.MODIFIED){
                                                    @SuppressLint("UnsafeIntentLaunch") Intent i = getIntent();
                                                    overridePendingTransition(0, 0);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(i);
                                                }
                                                orderedProductAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderPlaced.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}