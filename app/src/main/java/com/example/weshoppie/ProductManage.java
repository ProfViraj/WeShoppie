package com.example.weshoppie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** @noinspection ALL*/
public class ProductManage extends AppCompatActivity {
    ArrayList<ProductModel> arrProducts = new ArrayList<>();
    FloatingActionButton fab_add;
    FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);

        fab_add = findViewById(R.id.fab_add);

        RecyclerView recyclerView = findViewById(R.id.recyclerProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrProducts.add(new ProductModel("mooong","42 Rs.","Kilogram","Madhur"));
        arrProducts.add(new ProductModel("Tur","41","Kilogram","No Brand"));
        arrProducts.add(new ProductModel("Wheat","41","gram","Madhur"));
        arrProducts.add(new ProductModel("Sugarr","41","Kilogram","Madhur"));
        arrProducts.add(new ProductModel("Soap","61","Kilogram","Madhur"));

        RecyclerProductAdapter adapter = new RecyclerProductAdapter(this, arrProducts);
        recyclerView.setAdapter(adapter);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductManage.this, ProductAdd.class));
            }
        });
    }
}