package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.DeliveredOrders.DeliveredOrders;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.UndeliveredOrders.ShopkeeperCartOrders;

public class DeliveredOrNot extends AppCompatActivity {
    Button Delivered, Undelivered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_or_not);

        Delivered = findViewById(R.id.delivered_orders_button);
        Undelivered = findViewById(R.id.undelivered_orders_button);
        //Delivered orders *************************************************************************************
        Delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveredOrNot.this, DeliveredOrders.class));
            }
        });
        //Undelivered Orders ************************************************************************************
        Undelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveredOrNot.this, ShopkeeperCartOrders.class));
            }
        });
    }
}