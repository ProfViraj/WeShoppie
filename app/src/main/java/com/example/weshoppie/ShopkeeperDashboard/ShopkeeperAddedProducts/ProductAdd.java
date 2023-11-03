package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedProducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/** @noinspection ALL*/
public class ProductAdd extends AppCompatActivity {

    public static final String TAG = "Product Add Activity";
    String[] PricePer = {"Select the Type","Kilogram","Item","Gram","Litre","Dozen"};
    String product_price_type, userid;
    EditText product_name, product_price, product_company;
    Spinner product_price_per;
    Button action_button;
    FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_price_per = findViewById(R.id.product_price_per);
        product_company = findViewById(R.id.product_company);
        action_button = findViewById(R.id.action_button);

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
                if (TextUtils.isEmpty(ProductCompany)){
                    ProductCompany = "Local";
                }
                String finalProductCompany = ProductCompany;
                userid = CurrentUser.getUid();

                //Map<String,Object> ProductNameMap = new HashMap<>();
                //ProductNameMap.put("Product Name",ProductName);

                Map<String,Object> products = new HashMap<>();
                products.put("Shopkeeper_id",userid);
                products.put("Product_Name",ProductName);
                products.put("Product_Price",ProductPrice+" Rs.");
                products.put("Product_Price_per",ProductPricePer);
                products.put("Brand",ProductCompany);

                db.collection("Products").whereEqualTo("Brand",ProductCompany)
                        .whereEqualTo("Product_Name",ProductName)
                        .whereEqualTo("Shopkeeper_id",userid)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult().isEmpty()){
                                        db.collection("Products").add(products).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(ProductAdd.this, "Product Added", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ProductAdd.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ProductAdd.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        String ProductId = "";
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                            ProductId = documentSnapshot.getId();
                                        }
                                        db.collection("Products").document(ProductId).set(products)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(ProductAdd.this, "Product updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ProductAdd.this, "Product cannon be Updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(ProductAdd.this, "Cannot Check the database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductAdd.this, "Unable to connect", Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
            }
        });
    }
}