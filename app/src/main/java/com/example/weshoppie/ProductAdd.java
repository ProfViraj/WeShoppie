package com.example.weshoppie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductAdd extends AppCompatActivity {

    ArrayList<ProductModel> arrProducts = new ArrayList<>();
    String[] PricePer = {"Select the Type","Kilogram","Item","Gram","Litre","Dozen"};
    String product_price_type, userid;
    FloatingActionButton fab_add;
    EditText product_name, product_price, product_company;
    Spinner product_price_per;
    Button action_button;
    FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

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
                Dialog dialog = new Dialog(ProductAdd.this);
                dialog.setContentView(R.layout.product_add_layout);

                product_name = dialog.findViewById(R.id.product_name);
                product_price = dialog.findViewById(R.id.product_price);
                product_price_per = dialog.findViewById(R.id.product_price_per);
                product_company = dialog.findViewById(R.id.product_company);
                action_button = dialog.findViewById(R.id.action_button);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProductAdd.this, android.R.layout.simple_spinner_item, PricePer);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                product_price_per.setAdapter(adapter);

                product_price_per.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        product_price_type = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(ProductAdd.this, "Enter the Shop Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                action_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ProductName, ProductPrice, ProductPricePer, ProductCompany;
                        ProductName = product_name.getText().toString();
                        ProductPrice = product_price.getText().toString();
                        ProductPricePer = product_price_per.getSelectedItem().toString();
                        ProductCompany = product_company.getText().toString();

                        if(TextUtils.isEmpty(ProductName)){
                            Toast.makeText(ProductAdd.this, "Enter the Product's Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(ProductPrice)){
                            Toast.makeText(ProductAdd.this, "Enter the Product's Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(ProductPricePer) || ProductPricePer.equals("Select the Type")){
                            Toast.makeText(ProductAdd.this, "Select the Price per type", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        userid = CurrentUser.getUid();

                        Map<String,Object> products = new HashMap<>();
                        products.put("Product Price",ProductPrice+" Rs.");
                        products.put("Product Price per",ProductPricePer);

                        if (TextUtils.isEmpty(ProductCompany)){
                            db.collection("Products").document(ProductName).collection("No Brand").document(userid)
                                    .set(products)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ProductAdd.this, "Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProductAdd.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            db.collection("Products").document(ProductName).collection(ProductCompany).document(userid)
                                    .set(products)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ProductAdd.this, "Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProductAdd.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}