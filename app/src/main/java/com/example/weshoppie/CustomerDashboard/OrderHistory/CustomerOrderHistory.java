package com.example.weshoppie.CustomerDashboard.OrderHistory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.weshoppie.R;

public class CustomerOrderHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_history);
        Toast.makeText(this, "Order History", Toast.LENGTH_SHORT).show();
    }
}