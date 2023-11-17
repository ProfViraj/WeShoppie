package com.example.weshoppie.CustomerDashboard.CustPlaceOrder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced.SeeEditableOrders;
import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrderPlace.PlaceOrderSellerList;
import com.example.weshoppie.R;

public class NewOrUndeliveredOrders extends AppCompatActivity {
    Button NewOrders, EditOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_or_undelivered_orders);

        NewOrders = findViewById(R.id.new_orders_button);
        EditOrders = findViewById(R.id.editable_undelivered_orders_button);

        NewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewOrUndeliveredOrders.this, PlaceOrderSellerList.class));
            }
        });

        EditOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewOrUndeliveredOrders.this, SeeEditableOrders.class));
            }
        });
    }
}