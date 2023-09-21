package com.example.weshoppie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SelectUserType extends AppCompatActivity {


    Button customer_login;
    Button shopkeeper_login;
    Button logout;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        customer_login = findViewById(R.id.customer_login);
        shopkeeper_login = findViewById(R.id.shopkeeper_login);
        logout = findViewById(R.id.Logout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        customer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserType.this, RegisterCustomer.class);
                startActivity(intent);
                finish();
            }
        });

        shopkeeper_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectUserType.this, RegisterShopkeeper.class));
                finish();
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