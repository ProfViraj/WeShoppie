package com.example.weshoppie.ShopkeeperDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustomerDashboardNew;
import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust.AddedCustomers;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedProducts.ProductManage;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.DeliveredOrNot;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders.ShopkeeperNewOrders;
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

import java.util.Objects;

public class ShopkeeperDashboard extends AppCompatActivity {

    static final String TAG = "Shopkeeper Dashboard Activity";
    TextView Welcome, AddProduct, ConnectedCust, NewOrdersText, CartOrdersText;
    ImageView NewOrderNotification, ProfileUpdate;
    String userid, Name;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser =mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_dashboard);

        AddProduct = findViewById(R.id.productAdd);
        ConnectedCust = findViewById(R.id.Connected_Cust);
        NewOrdersText = findViewById(R.id.new_Orders_text);
        CartOrdersText = findViewById(R.id.cart_orders_text);
        NewOrderNotification = findViewById(R.id.new_order_notification);
        ProfileUpdate = findViewById(R.id.seller_profile_update);

        userid = CurrentUser.getUid();

        Welcome = findViewById(R.id.Welcome);
        //Getting the seller's name ***************************************************************************
        db.collection("Shopkeeper").document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot ds = task.getResult();
                            Welcome.setText("Welcome Back "+ ds.get("Owner_Name").toString() ) ;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShopkeeperDashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        CartOrdersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopkeeperDashboard.this, DeliveredOrNot.class));
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
        ProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopkeeperDashboard.this, ShopkeeperProfileUpdate.class));
            }
        });
        //Realtime update for the new orders ***********************************************************************
        db.collection("Orders").whereEqualTo("Shopkeeper_ID", userid)
                .whereEqualTo("Status","Unpacked")
                .whereEqualTo("Delivered", false)
                .whereEqualTo("Accepted",true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(ShopkeeperDashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                NewOrderNotification.setVisibility(View.VISIBLE);
                            }
                            if (documentChange.getType() == DocumentChange.Type.REMOVED){
                                NewOrderNotification.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}