package com.example.weshoppie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShopkeeperDashboard extends AppCompatActivity {

    Button AddProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_dashboard);

        AddProduct = findViewById(R.id.productAdd);

        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopkeeperDashboard.this, ProductAdd.class));
            }
        });
    }
}