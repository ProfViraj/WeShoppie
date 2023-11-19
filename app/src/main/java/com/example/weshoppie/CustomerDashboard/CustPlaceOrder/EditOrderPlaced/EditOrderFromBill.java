package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.EditOrderPlaced.AddNewProduct.ProductAddInEdit;
import com.example.weshoppie.CustomerDashboard.CustomerDashboardNew;
import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditOrderFromBill extends AppCompatActivity implements SelectEditOrder{
    SearchView searchView;
    TextView BillEdit;
    Intent fromAct;
    static int Amount;
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    Button Cancel, Confirm;
    String UserID, OrderID, ShopID;
    ArrayList<EditOrderModel> editOrderModelArrayList;
    EditOrderAdapter editOrderAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_from_bill);

        UserID = CurrentUser.getUid();
        fromAct = getIntent();
        OrderID = fromAct.getStringExtra("BillNo");
        ShopID = fromAct.getStringExtra("Shopkeeper_ID");
        Amount=0;

        db = FirebaseFirestore.getInstance();
        BillEdit = findViewById(R.id.bill_edit);
        searchView = findViewById(R.id.searchProduct);
        recyclerView = findViewById(R.id.recyclerOrderedProducts);
        actionButton = findViewById(R.id.product_add_float);
        Cancel = findViewById(R.id.cancel_button);
        Confirm = findViewById(R.id.confirm_button);

        BillEdit.setText(OrderID);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editOrderModelArrayList = new ArrayList<EditOrderModel>();
        editOrderAdapter = new EditOrderAdapter(EditOrderFromBill.this, editOrderModelArrayList, this);
        recyclerView.setAdapter(editOrderAdapter);

        EventChangeListener();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productAddIntent = new Intent(EditOrderFromBill.this, ProductAddInEdit.class);
                productAddIntent.putExtra("BillNo",OrderID);
                productAddIntent.putExtra("Shopkeeper_ID",ShopID);
                startActivity(productAddIntent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Orders").document(OrderID).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(EditOrderFromBill.this, "Order Cancelled Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditOrderFromBill.this, CustomerDashboardNew.class));
                                    finish();
                                }
                            }
                        });
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Orders").document(OrderID).collection("Added_Products").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (DocumentSnapshot documentSnapshot: task.getResult()){
                                        Amount = Amount + Integer.parseInt(documentSnapshot.get("Total_Cost").toString());
                                    }
                                    db.collection("Orders").document(OrderID).update("Amount",String.valueOf(Amount));
                                }
                            }
                        });
                Map<String,Object> updateConfirm = new HashMap<>();
                updateConfirm.put("Accepted",true);
                updateConfirm.put("Status","Unpacked");
                db.collection("Orders").document(OrderID).update(updateConfirm)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(EditOrderFromBill.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditOrderFromBill.this, CustomerDashboardNew.class));
                                    finish();
                                } else {
                                    Toast.makeText(EditOrderFromBill.this, "Error...!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditOrderFromBill.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void EventChangeListener() {
        db.collection("Orders").document(OrderID).collection("Added_Products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(EditOrderFromBill.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                EditOrderModel eom = documentChange.getDocument().toObject(EditOrderModel.class);
                                eom.setDocumentID(documentChange.getDocument().getId());
                                editOrderModelArrayList.add(eom);
                            }
                            editOrderAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onDeleteAlert(EditOrderModel editOrderModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditOrderFromBill.this)
                .setTitle("Delete Product")
                .setMessage("Are you sure want to delete?")
                .setIcon(R.drawable.baseline_delete_24)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Orders").document(OrderID).collection("Added_Products")
                                .document(editOrderModel.documentID)
                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            editOrderModelArrayList.remove(editOrderModel);
                                            editOrderAdapter.notifyDataSetChanged();
                                            Toast.makeText(EditOrderFromBill.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditOrderFromBill.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                        }
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

    @Override
    public void onUpdateAlert(EditOrderModel editOrderModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditOrderFromBill.this)
                .setTitle("Update Product")
                .setMessage("Are you sure want to update?")
                .setIcon(R.drawable.baseline_update_24)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editOrderModel.setCountStr(editOrderModel.count);
                        editOrderModel.setTotal_Cost();
                        editOrderModel.setProduct_Status("Unpacked");

                        Map<String,Object> updateNew = new HashMap<>();
                        updateNew.put("Count",editOrderModel.Count);
                        updateNew.put("Total_Cost", editOrderModel.Total_Cost);
                        updateNew.put("Product_Status",editOrderModel.Product_Status);

                        db.collection("Orders").document(OrderID).collection("Added_Products")
                                .document(editOrderModel.documentID).update(updateNew)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(EditOrderFromBill.this, "Product Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditOrderFromBill.this, "Error...!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditOrderFromBill.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
    }
}