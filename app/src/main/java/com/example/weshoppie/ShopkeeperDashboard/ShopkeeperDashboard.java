package com.example.weshoppie.ShopkeeperDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust.AddedCustomers;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedProducts.ProductManage;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders.ShopkeeperNewOrders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class ShopkeeperDashboard extends AppCompatActivity {

    static final String TAG = "Shopkeeper Dashboard Activity";
    TextView Welcome, AddProduct, ConnectedCust, NewOrdersText, CartOrdersText;
    ImageView NewOrderNotification;
    String userid, Name;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser =mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onResume() {
        super.onResume();

        db.collection("Orders").whereEqualTo("Shopkeeper_ID", userid)
                .whereEqualTo("Status","Unpacked")
                .whereEqualTo("Accepted",true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (!task.getResult().isEmpty()){
                                NewOrderNotification.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_dashboard);

        AddProduct = findViewById(R.id.productAdd);
        ConnectedCust = findViewById(R.id.Connected_Cust);
        NewOrdersText = findViewById(R.id.new_Orders_text);
        CartOrdersText = findViewById(R.id.cart_orders_text);
        NewOrderNotification = findViewById(R.id.new_order_notification);

        userid = CurrentUser.getUid();

        Welcome = findViewById(R.id.Welcome);

        db.collection("Shopkeeper").whereEqualTo("Email", CurrentUser.getEmail()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        Name = Objects.requireNonNull(documentSnapshot.get("Owner_Name")).toString();
                                    }
                                    Welcome.setText("Welcome Back "+ Name ) ;
                                } else {
                                    Toast.makeText(ShopkeeperDashboard.this, "Network Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShopkeeperDashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopkeeperDashboard.this, ProductManage.class));
            }
        });

        ConnectedCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopkeeperDashboard.this, AddedCustomers.class));
            }
        });
        NewOrdersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopkeeperDashboard.this, ShopkeeperNewOrders.class));
            }
        });
    }
}