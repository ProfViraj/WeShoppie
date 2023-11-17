package com.example.weshoppie.CustomerDashboard.CustAddedSeller.SameNumDiffSellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustomerDashboardNew;
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

public class SameNumDiffSellers extends AppCompatActivity implements SelectRegisterShop{
    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<RegisteredShopModel> registeredShopModelArrayList;
    RegisteredShopsAdapter registeredShopsAdapter;
    String SellerNumber, UserId, Name, Mobile_Number;
    Intent fromAct;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_num_diff_sellers);

        fromAct = getIntent();
        SellerNumber = fromAct.getStringExtra("SellerNumber");

        searchView = findViewById(R.id.searchViewRegisteredShops);
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

        recyclerView = findViewById(R.id.recyclerRegisteredShops);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserId = CurrentUser.getUid();

        db = FirebaseFirestore.getInstance();
        registeredShopModelArrayList = new ArrayList<RegisteredShopModel>();
        registeredShopsAdapter = new RegisteredShopsAdapter(SameNumDiffSellers.this, registeredShopModelArrayList, this);
        recyclerView.setAdapter(registeredShopsAdapter);

        db.collection("Customer").document(UserId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    Name = task.getResult().get("Name").toString();
                                    Mobile_Number = task.getResult().get("Mobile_Number").toString();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SameNumDiffSellers.this, "Unable to retrieve the customer data", Toast.LENGTH_SHORT).show();
                    }
                });
        EventChangeListener();

    }

    private void filterList(String newText) {
        ArrayList<RegisteredShopModel> filteredList = new ArrayList<RegisteredShopModel>();
        for (RegisteredShopModel item : registeredShopModelArrayList){
            if (item.getOwner_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
            if (item.getShop_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such Shop exists", Toast.LENGTH_SHORT).show();
        } else {
            registeredShopsAdapter.setFilteredList(filteredList);
        }
    }

    private void EventChangeListener() {
        db.collection("Shopkeeper").whereEqualTo("Owner_Phone",SellerNumber)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            Toast.makeText(SameNumDiffSellers.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange documentChange:value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                RegisteredShopModel rsm = documentChange.getDocument().toObject(RegisteredShopModel.class);
                                rsm.setDocumentID(documentChange.getDocument().getId());
                                registeredShopModelArrayList.add(rsm);
                            }
                            registeredShopsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(RegisteredShopModel registeredShopModel) {


        Map<String, Object> mycustomer = new HashMap<>();
        mycustomer.put("Name",Name);
        mycustomer.put("Mobile_Number",Mobile_Number);

        Map<String, Object> myseller = new HashMap<>();
        myseller.put("Name",registeredShopModel.Owner_Name);
        myseller.put("Phone",registeredShopModel.Owner_Phone);
        myseller.put("Shop_Name",registeredShopModel.Shop_Name);
        myseller.put("Shop_Type",registeredShopModel.Shop_Type);

        db.collection("Customer").document(UserId)
                .collection("My_Sellers").document(registeredShopModel.getDocumentID()).set(myseller)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            db.collection("Shopkeeper").document(registeredShopModel.DocumentID)
                                    .collection("Added_Customers").document(UserId).set(mycustomer)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(SameNumDiffSellers.this, "Shop Added Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SameNumDiffSellers.this, CustomerDashboardNew.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SameNumDiffSellers.this, "Cannot add Customer data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SameNumDiffSellers.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SameNumDiffSellers.this, "Cannot add shopkeeper data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SameNumDiffSellers.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}