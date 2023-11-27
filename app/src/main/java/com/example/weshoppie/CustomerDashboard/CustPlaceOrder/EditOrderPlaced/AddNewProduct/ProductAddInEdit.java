package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced.AddNewProduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced.EditOrderFromBill;
import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrderPlace.SeeProducts;
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
import java.util.HashMap;
import java.util.Map;

public class ProductAddInEdit extends AppCompatActivity implements SelectProductAddIN{
    String OrderId, UserId, ShopId;
    boolean isProductListEmpty=true;
    Intent fromAct;
    RecyclerView recyclerView;
    Button Confirm;
    ArrayList<ProductAddInModel> productAddInModelArrayList;
    ProductAddInAdapter productAddInAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add_in_edit);

        fromAct = getIntent();
        OrderId = fromAct.getStringExtra("BillNo");
        ShopId = fromAct.getStringExtra("Shopkeeper_ID");

        UserId = CurrentUser.getUid();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerAddNewProduct);
        Confirm = findViewById(R.id.confirm_add_product_in_edit);
        //Ending adding product activity *************************************************************************************
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProductListEmpty){
                    Toast.makeText(ProductAddInEdit.this, "Select at least one product", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
            }
        });
        //Implementing the recycler view *************************************************************************************************8

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productAddInModelArrayList = new ArrayList<ProductAddInModel>();
        productAddInAdapter = new ProductAddInAdapter(ProductAddInEdit.this, productAddInModelArrayList, this);
        recyclerView.setAdapter(productAddInAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        //Realtime updates for shopkeepers products ***********************************************************************************
        db.collection("Products").whereEqualTo("Shopkeeper_id",ShopId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(ProductAddInEdit.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                ProductAddInModel paim = dc.getDocument().toObject(ProductAddInModel.class);
                                paim.setDocumentID(dc.getDocument().getId());
                                productAddInModelArrayList.add(paim);
                            }
                            productAddInAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    //If the count is zero *************************************************************************************
    @Override
    public void onCountZero(ProductAddInModel productAddInModel) {
        Toast.makeText(this, "Please select the count", Toast.LENGTH_SHORT).show();
    }
    //Adding the product to the database
    @Override
    public void onProductAdd(ProductAddInModel productAddInModel) {
        db.collection("Orders").document(OrderId).collection("Added_Products")
                .whereEqualTo("Product_Name",productAddInModel.Product_Name)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                //Adding the new product ***************************************************************************
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductAddInEdit.this)
                                        .setTitle("Add Product")
                                        .setMessage("Do you want to add "+productAddInModel.Product_Name+ "?")
                                        .setIcon(R.drawable.baseline_add_24)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String[] SplitPrice = productAddInModel.Product_Price.split("\\s");
                                                int value = productAddInModel.getCountINT() * Integer.parseInt(SplitPrice[0]);
                                                productAddInModel.setTotal_Cost(String.valueOf(value));

                                                isProductListEmpty = false;
                                                Map<String,Object> ProductAddEditData = new HashMap<>();
                                                ProductAddEditData.put("Product_Name", productAddInModel.Product_Name);
                                                ProductAddEditData.put("Product_Price",productAddInModel.Product_Price);
                                                ProductAddEditData.put("Product_Price_Per",productAddInModel.Product_Price_per);
                                                ProductAddEditData.put("Brand",productAddInModel.Brand);
                                                ProductAddEditData.put("Count",productAddInModel.getCount());
                                                ProductAddEditData.put("Total_Cost",productAddInModel.getTotal_Cost());
                                                ProductAddEditData.put("Product_Status","Unpacked");

                                                db.collection("Orders").document(OrderId).collection("Added_Products")
                                                        .document(productAddInModel.documentID).set(ProductAddEditData)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(ProductAddInEdit.this, "Product Added", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });
                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                builder.show();
                            } else {
                                //If product is already added *************************************************************************************
                                Toast.makeText(ProductAddInEdit.this, "This product is already added... Please update the product", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductAddInEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}