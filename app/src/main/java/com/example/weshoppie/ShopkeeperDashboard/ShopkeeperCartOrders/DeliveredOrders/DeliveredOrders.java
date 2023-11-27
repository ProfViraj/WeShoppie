package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.DeliveredOrders;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DeliveredOrders extends AppCompatActivity implements SeeDeliveredOrders{
    String UserID;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<DeliveredOrdersModel> deliveredOrdersModelArrayList;
    DeliveredOrdersAdapter deliveredOrdersAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_orders);
        //Implementing search view *******************************************************8
        searchView = findViewById(R.id.searchViewDeliveredOrders);
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
        //Implementing Recycler view ****************************************************************************************
        recyclerView = findViewById(R.id.recyclerDeliveredOrderHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserID = CurrentUser.getUid();

        deliveredOrdersModelArrayList = new ArrayList<DeliveredOrdersModel>();
        deliveredOrdersAdapter = new DeliveredOrdersAdapter(DeliveredOrders.this, deliveredOrdersModelArrayList, this);
        recyclerView.setAdapter(deliveredOrdersAdapter);

        EventChangeListener();
    }
    //Realtime Updates for delivered orders *****************************************************************************
    private void EventChangeListener() {
        db.collection("Orders").whereEqualTo("Shopkeeper_ID", UserID)
                .whereEqualTo("Accepted", true).whereEqualTo("Delivered", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(DeliveredOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                DeliveredOrdersModel dom = documentChange.getDocument().toObject(DeliveredOrdersModel.class);
                                dom.setDocumentID(documentChange.getDocument().getId());
                                deliveredOrdersModelArrayList.add(dom);
                            }
                            deliveredOrdersAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    //Filtering the list as per search ********************************************************************************
    private void filterList(String newText) {
        ArrayList<DeliveredOrdersModel> filteredList = new ArrayList<DeliveredOrdersModel>();
        for (DeliveredOrdersModel item : deliveredOrdersModelArrayList){
            if (item.getCust_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such order exists", Toast.LENGTH_SHORT).show();
        } else {
            deliveredOrdersAdapter.setFilteredList(filteredList);
        }
    }
    //On order selected ***************************************************************************************
    @Override
    public void onOrderSelected(DeliveredOrdersModel deliveredOrdersModel) {
        Intent intent = new Intent(DeliveredOrders.this, SeeOrderDetails.class);
        intent.putExtra("BillNo",deliveredOrdersModel.getDocumentID());
        intent.putExtra("Date",deliveredOrdersModel.getTime());
        intent.putExtra("CustId", deliveredOrdersModel.getCustomer_ID());
        startActivity(intent);
    }
}