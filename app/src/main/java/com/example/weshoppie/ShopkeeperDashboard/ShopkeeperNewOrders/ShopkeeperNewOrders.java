package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders.ProductStatus.SeeUnpackedProducts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopkeeperNewOrders extends AppCompatActivity implements SelectNewOrder{

    RecyclerView recyclerNewOrder;
    SearchView searchView;
    String userID;
    ArrayList<NewOrderModel> arrNewOrderModel;
    RecyclerNewProductAdapter recyclerNewProductAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_new_orders);

        searchView = findViewById(R.id.searchViewNewOrders);
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

        recyclerNewOrder = findViewById(R.id.recyclerNewOrder);
        recyclerNewOrder.setHasFixedSize(true);
        recyclerNewOrder.setLayoutManager(new LinearLayoutManager(this));
        userID = CurrentUser.getUid();

        db = FirebaseFirestore.getInstance();
        arrNewOrderModel = new ArrayList<NewOrderModel>();
        recyclerNewProductAdapter = new RecyclerNewProductAdapter(ShopkeeperNewOrders.this, arrNewOrderModel,this);
        recyclerNewOrder.setAdapter(recyclerNewProductAdapter);

        EventChangeListener();
    }

    private void filterList(String newText) {
        ArrayList<NewOrderModel> filteredList = new ArrayList<NewOrderModel>();
        for (NewOrderModel item : arrNewOrderModel){
            if (item.getCustomer_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such New Order exists", Toast.LENGTH_SHORT).show();
        } else {
            recyclerNewProductAdapter.setFilteredList(filteredList);
        }
    }

    private void EventChangeListener() {
        db.collection("Orders").whereEqualTo("Shopkeeper_ID",userID).whereEqualTo("Status","Unpacked")
                .whereEqualTo("Accepted", true).whereEqualTo("Delivered", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(ShopkeeperNewOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                NewOrderModel nom = dc.getDocument().toObject(NewOrderModel.class);
                                nom.setDocumentID(dc.getDocument().getId());
                                arrNewOrderModel.add(nom);
                            }
                            recyclerNewProductAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(NewOrderModel newOrderModel) {
        String orderID = newOrderModel.getDocumentID();
        Intent intent = new Intent(ShopkeeperNewOrders.this, SeeUnpackedProducts.class);
        intent.putExtra("OrderID",orderID);
        startActivity(intent);
        finish();
    }
}