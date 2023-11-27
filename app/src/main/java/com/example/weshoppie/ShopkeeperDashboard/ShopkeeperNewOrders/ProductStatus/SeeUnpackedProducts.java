package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperNewOrders.ProductStatus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeeUnpackedProducts extends AppCompatActivity implements SelectUnpackedProduct{

    Intent fromAct;
    SearchView searchView;
    String OrderID;
    ArrayList<UnpackedProductsModel> unpackedProductsModelArrayList;
    RecyclerView recyclerUnpackedProduct;
    FloatingActionButton PackingDone;
    UnpackedProductAdapter unpackedProductAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_unpacked_products);

        fromAct = getIntent();
        OrderID = fromAct.getStringExtra("OrderID");
        //implementing search view *******************************************************************
        searchView = findViewById(R.id.searchViewUnpackedProducts);
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
        //On packing done ****************************************************************************
        PackingDone = findViewById(R.id.packing_done);
        PackingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if all the packing is complete *************************************************************
                db.collection("Orders").document(OrderID)
                        .collection("Added_Products")
                        .whereEqualTo("Product_Status","Unpacked")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if (!task.getResult().isEmpty()){
                                        Toast.makeText(SeeUnpackedProducts.this, "Packing Not Complete", Toast.LENGTH_SHORT).show();
                                    } else {
                                        db.collection("Orders").document(OrderID)
                                                .update("Status","Packed");
                                        finish();
                                    }
                                }
                            }
                        });
                finish();
            }
        });
        //Implementing recycler view **************************************************************************************************
        recyclerUnpackedProduct = findViewById(R.id.recyclerUnpackedProducts);
        recyclerUnpackedProduct.setHasFixedSize(true);
        recyclerUnpackedProduct.setLayoutManager(new LinearLayoutManager(this));

        unpackedProductsModelArrayList = new ArrayList<UnpackedProductsModel>();
        unpackedProductAdapter = new UnpackedProductAdapter(SeeUnpackedProducts.this, unpackedProductsModelArrayList, this);
        recyclerUnpackedProduct.setAdapter(unpackedProductAdapter);
        Toast.makeText(this, OrderID, Toast.LENGTH_SHORT).show();

        CheckIfAllPacked();
    }
    //Filtering the search list ***************************************************************
    private void filterList(String newText) {
        ArrayList<UnpackedProductsModel> filteredList = new ArrayList<UnpackedProductsModel>();
        for (UnpackedProductsModel item : unpackedProductsModelArrayList){
            if (item.getProduct_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such Product is Unpacked", Toast.LENGTH_SHORT).show();
        } else {
            unpackedProductAdapter.setFilteredList(filteredList);
        }
    }
    //Check if all packed *************************************************************************
    private void CheckIfAllPacked() {

        db.collection("Orders").document(OrderID).collection("Added_Products")
                .whereEqualTo("Product_Status","Unpacked").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                //If all packed ***************************************************************************************************
                                db.collection("Orders").document(OrderID).update("Status","Packed").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SeeUnpackedProducts.this, "Packing Done", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SeeUnpackedProducts.this, "Packing Remaining", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SeeUnpackedProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                EventChangeListener();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeUnpackedProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void EventChangeListener() {
        //Realtime updates for Unpacked products ************************************************************************************
        db.collection("Orders").document(OrderID).collection("Added_Products")
                .whereEqualTo("Product_Status","Unpacked").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(SeeUnpackedProducts.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED){
                        UnpackedProductsModel upm = dc.getDocument().toObject(UnpackedProductsModel.class);
                        upm.setDocumentID(dc.getDocument().getId());
                        unpackedProductsModelArrayList.add(upm);
                    }
                    unpackedProductAdapter.notifyDataSetChanged();
                }
            }
        });

    }
    //On packing the item *************************************************************************************************
    @Override
    public void onItemClicked(UnpackedProductsModel unpackedProductsModel) {
        Map<String,Object> ProductStatus = new HashMap<>();
        ProductStatus.put("Product_Status","Packed");
        db.collection("Orders").document(OrderID).collection("Added_Products")
                        .document(unpackedProductsModel.getDocumentID()).update(ProductStatus)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SeeUnpackedProducts.this, "Product Packed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SeeUnpackedProducts.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeUnpackedProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}