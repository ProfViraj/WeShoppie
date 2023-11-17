package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrderPlace.SeeOrderPlaced;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        OrderID = fromAct.getStringExtra("BillNo");
        ShopID = fromAct.getStringExtra("ShopId");
        Date = fromAct.getStringExtra("Date");

        UserId = CurrentUser.getUid();

        BillNo.setText("Bill No: " + OrderID);
        OrderDate.setText("Date : " + Date);

        db.collection("Orders").document(OrderID).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    Total = task.getResult().get("Amount").toString();
                                    TotalCostBill.setText(Total + " Rs.");
                                }
                            }
                        });
        //Getting and Setting Shop Name
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
                            orderedProductAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}