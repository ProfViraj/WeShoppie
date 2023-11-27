package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedProducts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/** @noinspection ALL*/
public class ProductManage extends AppCompatActivity {
    public static final String TAG = "Product Manage Activity";
    SearchView searchView;
    String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    String usermail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
    ArrayList<ProductModel> arrProducts;
    FloatingActionButton fab_add;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerProductAdapter adapter;
    FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);

        fab_add = findViewById(R.id.fab_add);
        progressBar = findViewById(R.id.progress_circular);
        searchView = findViewById(R.id.searchViewMyProducts);
        searchView.clearFocus();
        //Implementing search view *******************************************************************
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
        //Implementing Recycler view ******************************************************************
        recyclerView = findViewById(R.id.recyclerProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrProducts = new ArrayList<ProductModel>();
        adapter = new RecyclerProductAdapter(ProductManage.this, arrProducts);

        recyclerView.setAdapter(adapter);

        EventChangeListener();
        //Add Product button press *****************************************************************
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductManage.this, ProductAdd.class));
            }
        });
    }

    private void filterList(String newText) {
        ArrayList<ProductModel> filteredList = new ArrayList<ProductModel>();
        for (ProductModel item : arrProducts){
            if (item.getProduct_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such Product Added", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }
    //Realtime update for products ***********************************************************************************
    private void EventChangeListener() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Products")
                .whereEqualTo("Shopkeeper_id",userid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.d(TAG, "onEvent: "+ error.getMessage());
                            Toast.makeText(ProductManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                ProductModel pm = dc.getDocument().toObject(ProductModel.class);
                                pm.setDocumentID(dc.getDocument().getId());
                                arrProducts.add(pm);
                            }
                            if (dc.getType() == DocumentChange.Type.MODIFIED){
                                Intent intent = getIntent();
                                overridePendingTransition(0, 0);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(intent);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}