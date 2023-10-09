package com.example.weshoppie.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectUserType extends AppCompatActivity {


    String userid;
    Button customer_login;
    Button shopkeeper_login;
    Button logout;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        customer_login = findViewById(R.id.customer_login);
        shopkeeper_login = findViewById(R.id.shopkeeper_login);
        logout = findViewById(R.id.Logout);

        userid = user.getUid();

        customer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Customer").document(userid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists()){
                                    Toast.makeText(SelectUserType.this, "Under Work", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(SelectUserType.this, CustomerDashboard.class));
                                }
                                else {
                                    Intent intent = new Intent(SelectUserType.this, RegisterCustomer.class);
                                    startActivity(intent);
                                }
                            }
                        });

            }
        });

        shopkeeper_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Shopkeeper").document(userid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists()){
                                    startActivity(new Intent(SelectUserType.this, ShopkeeperDashboard.class));
                                }
                                else {
                                    Intent intent = new Intent(SelectUserType.this, RegisterShopkeeper.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectUserType.this, "Signing out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SelectUserType.this, LoginPage.class));
                finish();
            }
        });
    }
}