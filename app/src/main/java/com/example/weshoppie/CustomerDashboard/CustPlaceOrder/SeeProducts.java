package com.example.weshoppie.CustomerDashboard.CustPlaceOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.SeeOrderPlaced.OrderPlaced;
import com.example.weshoppie.CustomerDashboard.CustomerDashboardNew;
import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SeeProducts extends AppCompatActivity implements SelectCount{

    public static final String TAG = "See Products Activity";
    boolean isProductListEmpty=true;
    int AmountInt;
    Intent fromAct;
    String ShopId, UserId, Number, Name, time, AmountStr;
    String OrderID="empty";
    TextView Shopname;
    Button Placeorder, CancelOrder;
    ArrayList<SelectProductModel> arrSelectProducts;
    RecyclerView recyclerView;
    SelectProductAdapter selectProductAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_products);

        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        time = formatter.format(currentTime);

        fromAct = getIntent();
        ShopId = fromAct.getStringExtra("OwnerId");
        UserId = CurrentUser.getUid();

        Shopname = findViewById(R.id.Shopname);
        Placeorder = findViewById(R.id.place_order);
        CancelOrder = findViewById(R.id.cancel_order);

        db.collection("Shopkeeper").document(ShopId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String shop_name_retrieved = Objects.requireNonNull(task.getResult().get("Shop_Name")).toString();
                            Shopname.setText(shop_name_retrieved);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, "Shop Name not retrieved", Toast.LENGTH_SHORT).show();
                    }
                });

        Placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProductListEmpty){
                    Toast.makeText(SeeProducts.this, "Select at least one product", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SeeProducts.this, OrderPlaced.class);
                intent.putExtra("ShopId", ShopId);
                intent.putExtra("Date", time);
                startActivity(intent);
                finish();
            }
        });
        CancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrder();
            }
        });

        recyclerView = findViewById(R.id.recyclerSellerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrSelectProducts = new ArrayList<SelectProductModel>();
        selectProductAdapter = new SelectProductAdapter(SeeProducts.this, arrSelectProducts,this);

        recyclerView.setAdapter(selectProductAdapter);



        db.collection("Customer").document(UserId).get()
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
                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        EventChangeListener();
    }

    private void DeleteOrder() {

        db.collection("Orders").whereEqualTo("Customer_ID",UserId)
                .whereEqualTo("Shopkeeper_ID",ShopId)
                .whereEqualTo("Status","Unpacked")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    OrderID = documentSnapshot.getId();
                                }
                                db.collection("Orders").document(OrderID).delete();
                            }
                            startActivity(new Intent(SeeProducts.this, CustomerDashboardNew.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void EventChangeListener() {
        db.collection("Products").whereEqualTo("Shopkeeper_id",ShopId).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            Log.d(TAG, "onEvent: "+ error.getMessage());
                            Toast.makeText(SeeProducts.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        assert value != null;
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                SelectProductModel spm = dc.getDocument().toObject(SelectProductModel.class);
                                spm.setDocumentID(dc.getDocument().getId());
                                arrSelectProducts.add(spm);
                            }
                            selectProductAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onQuantitySelected(SelectProductModel selectProductModel) {
        isProductListEmpty = false;
        Map<String,Object> BasicOrderData = new HashMap<>();
        BasicOrderData.put("Customer_ID",UserId);
        BasicOrderData.put("Shopkeeper_ID",ShopId);
        BasicOrderData.put("Customer_Number",Number);
        BasicOrderData.put("Customer_Name",Name);
        BasicOrderData.put("Status","Unpacked");
        BasicOrderData.put("Accepted",false);
        BasicOrderData.put("Time",time);
        BasicOrderData.put("Amount","0");

        Map<String,Object> ProductOrderData = new HashMap<>();
        ProductOrderData.put("Product_Name", selectProductModel.Product_Name);
        ProductOrderData.put("Product_Price",selectProductModel.Product_Price);
        ProductOrderData.put("Product_Price_Per",selectProductModel.Product_Price_per);
        ProductOrderData.put("Brand",selectProductModel.Brand);
        ProductOrderData.put("Count",selectProductModel.getCountstr());
        ProductOrderData.put("Total_Cost",selectProductModel.getCoststr());
        ProductOrderData.put("Product_Status","Unpacked");

        db.collection("Orders").whereEqualTo("Customer_ID",UserId)
                .whereEqualTo("Shopkeeper_ID",ShopId)
                .whereEqualTo("Status","Unpacked")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final String[] Order = new String[1];
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                db.collection("Orders").add(BasicOrderData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()){
                                            Order[0] = task.getResult().getId();
                                            db.collection("Orders").document(task.getResult().getId()).collection("Added_Products")
                                                    .document(selectProductModel.documentID).set(ProductOrderData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                db.collection("Orders").document(Order[0])
                                                                        .update("Amount",selectProductModel.getCoststr());
                                                            } else {
                                                                Toast.makeText(SeeProducts.this, "FirstDataNotAdded", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(SeeProducts.this, "CannotAdd", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                    OrderID = documentSnapshot.getId();
                                }
                                db.collection("Orders").document(OrderID).collection("Added_Products")
                                        .document(selectProductModel.documentID).set(ProductOrderData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SeeProducts.this, "ProductAdded", Toast.LENGTH_SHORT).show();
                                                db.collection("Orders").document(OrderID).get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                AmountInt = Integer.parseInt(task.getResult().get("Amount").toString());
                                                                AmountInt = AmountInt + Integer.parseInt(selectProductModel.getCoststr());
                                                                AmountStr = String.valueOf(AmountInt);

                                                                db.collection("Orders").document(OrderID)
                                                                        .update("Amount",AmountStr);
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(SeeProducts.this, "DataCannotBeFetch", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onQuantityNotSelected(SelectProductModel selectProductModel) {

        db.collection("Orders").document(OrderID).collection("Added_Products")
                .document(selectProductModel.getDocumentID()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SeeProducts.this, "Unselected", Toast.LENGTH_SHORT).show();
                        db.collection("Orders").document(OrderID).collection("Added_Products").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            if (task.getResult().isEmpty()){
                                                isProductListEmpty = true;
                                                db.collection("Orders").document(OrderID).delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(SeeProducts.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCountToast() {
        Toast.makeText(SeeProducts.this, "Please select an appropriate count", Toast.LENGTH_SHORT).show();
    }
}