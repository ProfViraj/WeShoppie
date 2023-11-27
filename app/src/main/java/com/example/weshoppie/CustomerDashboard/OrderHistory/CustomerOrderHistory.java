package com.example.weshoppie.CustomerDashboard.OrderHistory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.OrderHistory.SeeOrderDetails.SeeOrderDetails;
import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.UndeliveredOrders.ShopkeeperOrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomerOrderHistory extends AppCompatActivity implements SelectOrder{
    String userID , Order_ID;
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
        setContentView(R.layout.activity_customer_order_history);
        //Setting the search View **********************************************************
        searchView = findViewById(R.id.searchViewCustOrderHistory);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        //Setting the recycler view ***************************************************************************888888888
        recyclerView = findViewById(R.id.recyclerOrderHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userID = CurrentUser.getUid();

        db = FirebaseFirestore.getInstance();
        orderHistoryModelArrayList = new ArrayList<OrderHistoryModel>();
        orderHistoryAdapter = new OrderHistoryAdapter(CustomerOrderHistory.this,orderHistoryModelArrayList, this);
        recyclerView.setAdapter(orderHistoryAdapter);

        EventChangeListener();
    }

    private void filterList(String newText) {
        ArrayList<OrderHistoryModel> filteredList = new ArrayList<OrderHistoryModel>();
        for (OrderHistoryModel orderHistoryModel : orderHistoryModelArrayList){
            if (orderHistoryModel.getShop_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(orderHistoryModel);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such Shop Exists", Toast.LENGTH_SHORT).show();
        } else {
            orderHistoryAdapter.setFilteredList(filteredList);
        }
    }

    private void EventChangeListener() {
        //Realtime updates for the delivered orders ***************************************************************************
        db.collection("Orders").whereEqualTo("Customer_ID", userID)
                .whereEqualTo("Accepted", true).whereEqualTo("Delivered",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(CustomerOrderHistory.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                OrderHistoryModel ohm = dc.getDocument().toObject(OrderHistoryModel.class);
                                ohm.setDocumentID(dc.getDocument().getId());
                                orderHistoryModelArrayList.add(ohm);
                            }
                            orderHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(OrderHistoryModel orderHistoryModel) {
        //On order Selected ****************************************************************************
        Intent intent = new Intent(CustomerOrderHistory.this, SeeOrderDetails.class);
        intent.putExtra("BillNo",orderHistoryModel.getDocumentID());
        intent.putExtra("Date",orderHistoryModel.getTime());
        intent.putExtra("ShopId", orderHistoryModel.getShopkeeper_ID());
        startActivity(intent);

    }
}