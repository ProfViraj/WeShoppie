package com.example.weshoppie.CustomerDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustAddedSeller.MySellers;
import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrUndeliveredOrders;
import com.example.weshoppie.CustomerDashboard.CustomOrders.CustomOrders;
import com.example.weshoppie.CustomerDashboard.OrderHistory.CustomerOrderHistory;
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

import java.util.Objects;

public class CustomerDashboardNew extends AppCompatActivity {
    TextView OrderHistory, PlaceOrder, MySeller, CustomOrder, Welcome;
    ImageView CustomOrderNoti, ProfileView;
    String userid, usermail, Name;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard_new);

        OrderHistory = findViewById(R.id.order_history_text);
        PlaceOrder = findViewById(R.id.place_order_text);
        MySeller = findViewById(R.id.my_seller_text);
        CustomOrder = findViewById(R.id.custom_orders_text);
        Welcome = findViewById(R.id.Welcome);
        CustomOrderNoti = findViewById(R.id.packed_order_notification);
        ProfileView = findViewById(R.id.profile_update);

        userid = CurrentUser.getUid();

        db.collection("Customer").document(userid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot ds = task.getResult();
                                    Welcome.setText("Welcome Back "+ ds.get("Name").toString() ) ;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerDashboardNew.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardNew.this, CustomerProfileUpdate.class));
            }
        });
        OrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardNew.this, CustomerOrderHistory.class));
            }
        });

        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardNew.this, NewOrUndeliveredOrders.class));
            }
        });

        MySeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardNew.this, MySellers.class));
            }
        });

        CustomOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardNew.this, CustomOrders.class));
            }
        });
        // to do
        db.collection("Orders").whereEqualTo("Customer_ID", userid)
                .whereEqualTo("Accepted", true).whereEqualTo("Status","Packed")
                .whereEqualTo("Delivered", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(CustomerDashboardNew.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                CustomOrderNoti.setVisibility(View.VISIBLE);
                            }
                            if (documentChange.getType() == DocumentChange.Type.REMOVED){
                                CustomOrderNoti.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }
}