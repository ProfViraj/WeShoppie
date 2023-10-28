package com.example.weshoppie.CustomerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustAddedSeller.MySellers;
import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class CustomerDashboardNew extends AppCompatActivity {

    TextView OrderHistory, PlaceOrder, MySeller, Welcome;
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
        Welcome = findViewById(R.id.Welcome);

        db.collection("Customer").whereEqualTo("Email",CurrentUser.getEmail()).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Name = Objects.requireNonNull(documentSnapshot.get("Name")).toString();
                            }
                            Welcome.setText("Welcome Back "+ Name ) ;
                        } else {
                            Toast.makeText(CustomerDashboardNew.this, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        OrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        MySeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardNew.this, MySellers.class));
            }
        });

    }
}