package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.OrderHistory.CustomerOrderHistory;
import com.example.weshoppie.CustomerDashboard.OrderHistory.OrderHistoryAdapter;
import com.example.weshoppie.CustomerDashboard.OrderHistory.OrderHistoryModel;
import com.example.weshoppie.CustomerDashboard.OrderHistory.SeeOrderDetails.SeeOrderDetails;
import com.example.weshoppie.CustomerDashboard.OrderHistory.SelectOrder;
import com.example.weshoppie.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopkeeperCartOrders extends AppCompatActivity implements SeeShopOrders {
    String userID , Order_ID;
    RecyclerView recyclerView;
    ArrayList<ShopkeeperOrderModel> shopkeeperOrderModelArrayList;
    ShopkeeperOrderAdapter shopkeeperOrderAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_cart_orders);

        recyclerView = findViewById(R.id.recyclerOrderHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userID = CurrentUser.getUid();

        db = FirebaseFirestore.getInstance();
        shopkeeperOrderModelArrayList = new ArrayList<ShopkeeperOrderModel>();
        shopkeeperOrderAdapter = new ShopkeeperOrderAdapter(ShopkeeperCartOrders.this,shopkeeperOrderModelArrayList, this);
        recyclerView.setAdapter(shopkeeperOrderAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("Orders").whereEqualTo("Shopkeeper_ID", userID)
                .whereEqualTo("Accepted", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(ShopkeeperCartOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                ShopkeeperOrderModel ohm = dc.getDocument().toObject(ShopkeeperOrderModel.class);
                                ohm.setDocumentID(dc.getDocument().getId());
                                shopkeeperOrderModelArrayList.add(ohm);
                            }
                            shopkeeperOrderAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onOrderSelected(ShopkeeperOrderModel shopkeeperOrderModel) {
        Intent intent = new Intent(ShopkeeperCartOrders.this, SeeOrderDetails.class);
        intent.putExtra("BillNo",shopkeeperOrderModel.getDocumentID());
        intent.putExtra("Date",shopkeeperOrderModel.getTime());
        intent.putExtra("CustId", shopkeeperOrderModel.getCustomer_ID());
        startActivity(intent);
    }
}