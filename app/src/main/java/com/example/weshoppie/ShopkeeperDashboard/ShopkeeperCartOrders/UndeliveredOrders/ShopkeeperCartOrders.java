package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperCartOrders.UndeliveredOrders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.OrderHistory.SeeOrderDetails.SeeOrderDetails;
import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Objects;

public class ShopkeeperCartOrders extends AppCompatActivity implements SeeShopOrders {
    String userID , Order_ID;
    SearchView searchView;
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
        //Implementing search view **********************************************************************8
        searchView = findViewById(R.id.searchViewCartOrders);
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
        //Implementing recycler view ********************************************************************************************************
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

    private void filterList(String newText) {
        ArrayList<ShopkeeperOrderModel> filteredList = new ArrayList<ShopkeeperOrderModel>();
        for (ShopkeeperOrderModel item : shopkeeperOrderModelArrayList){
            if (item.getCust_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such Customer Order Exists", Toast.LENGTH_SHORT).show();
        } else {
            shopkeeperOrderAdapter.setFilteredList(filteredList);
        }
    }
    //Realtime updates for undelivered orders *****************************************************************
    private void EventChangeListener() {
        db.collection("Orders").whereEqualTo("Shopkeeper_ID", userID)
                .whereEqualTo("Accepted", true).whereEqualTo("Delivered", false)
                .whereEqualTo("Cancellation", false)
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
                            if (dc.getType() == DocumentChange.Type.REMOVED){
                                ShopkeeperOrderModel ohm = dc.getDocument().toObject(ShopkeeperOrderModel.class);
                                shopkeeperOrderModelArrayList.remove(ohm);
                            }
                            shopkeeperOrderAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    //On selecting orders *************************************************************************************88
    @Override
    public void onOrderSelected(ShopkeeperOrderModel shopkeeperOrderModel) {
        Intent intent = new Intent(ShopkeeperCartOrders.this, SeeOrderDetails.class);
        intent.putExtra("BillNo",shopkeeperOrderModel.getDocumentID());
        intent.putExtra("Date",shopkeeperOrderModel.getTime());
        intent.putExtra("CustId", shopkeeperOrderModel.getCustomer_ID());
        startActivity(intent);
    }

    //On delivery done **********************************************************************************************8
    @Override
    public void onDeliveryDone(ShopkeeperOrderModel shopkeeperOrderModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopkeeperCartOrders.this)
                .setTitle("Confirmation")
                .setMessage("Confirm to update delivery status to Done?")
                .setIcon(R.drawable.baseline_add_task_24)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Check if the order is packed **********************************************************************************************
                        db.collection("Orders").document(shopkeeperOrderModel.getDocumentID())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot ds = task.getResult();
                                            String Status = ds.getString("Status");
                                            if (Objects.equals(Status, "Unpacked")){
                                                Toast.makeText(ShopkeeperCartOrders.this, "Order is not packed yet", Toast.LENGTH_SHORT).show();
                                            } else {
                                                //If order is packed then can update delivery ***************************************************************
                                                db.collection("Orders").document(shopkeeperOrderModel.getDocumentID())
                                                        .update("Delivered",true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(ShopkeeperCartOrders.this, "Delivery Done", Toast.LENGTH_SHORT).show();
                                                                @SuppressLint("UnsafeIntentLaunch") Intent intent = getIntent();
                                                                overridePendingTransition(0, 0);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                finish();
                                                                overridePendingTransition(0, 0);
                                                                startActivity(intent);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(ShopkeeperCartOrders.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ShopkeeperCartOrders.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}