package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.OrderHistory.OrderHistoryAdapter;
import com.example.weshoppie.CustomerDashboard.OrderHistory.OrderHistoryModel;
import com.example.weshoppie.CustomerDashboard.OrderHistory.SelectOrder;
import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SeeEditableOrders extends AppCompatActivity implements SelectOrder {
    String UserID, ShopId;
    Intent intent;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<OrderHistoryModel> orderHistoryModelArrayList;
    OrderHistoryAdapter orderHistoryAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_editable_orders);

        UserID = CurrentUser.getUid();

        searchView = findViewById(R.id.searchViewCustEditOrder);
        recyclerView = findViewById(R.id.recyclerEditOrder);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        orderHistoryModelArrayList = new ArrayList<OrderHistoryModel>();
        orderHistoryAdapter = new OrderHistoryAdapter(SeeEditableOrders.this, orderHistoryModelArrayList, this);
        recyclerView.setAdapter(orderHistoryAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("Orders")
                .whereEqualTo("Customer_ID", UserID)
                .whereEqualTo("Accepted",true)
                .whereEqualTo("Delivered", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(SeeEditableOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange documentChange: value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                OrderHistoryModel ohm = documentChange.getDocument().toObject(OrderHistoryModel.class);
                                ohm.setDocumentID(documentChange.getDocument().getId());
                                orderHistoryModelArrayList.add(ohm);
                            }
                            orderHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(OrderHistoryModel orderHistoryModel) {
        db.collection("Orders").document(orderHistoryModel.getDocumentID()).update("Accepted",false);
        intent = new Intent(SeeEditableOrders.this, EditOrderFromBill.class);
        intent.putExtra("BillNo",orderHistoryModel.getDocumentID());
        intent.putExtra("Shopkeeper_ID", orderHistoryModel.getShopkeeper_ID());
        startActivity(intent);
        finish();
    }
}