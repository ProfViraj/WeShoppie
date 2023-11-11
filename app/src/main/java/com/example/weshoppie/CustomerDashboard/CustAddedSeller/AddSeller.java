package com.example.weshoppie.CustomerDashboard.CustAddedSeller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustAddedSeller.SameNumDiffSellers.SameNumDiffSellers;
import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AddSeller extends AppCompatActivity {
    String UserId;
    EditText Seller_Num;
    Button Confirm_Seller_Num;
    FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seller);

        Seller_Num = findViewById(R.id.seller_num);
        Confirm_Seller_Num = findViewById(R.id.confirm_seller_num);
        UserId = CurrentUser.getUid();

        Confirm_Seller_Num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SellerNumber;
                SellerNumber = Seller_Num.getText().toString();
                if(TextUtils.isEmpty(SellerNumber)){
                    Toast.makeText(AddSeller.this, "Enter Seller's Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.collection("Shopkeeper").whereEqualTo("Owner_Phone",SellerNumber)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult().isEmpty()){
                                        Toast.makeText(AddSeller.this, "No such Seller Exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(AddSeller.this, SameNumDiffSellers.class);
                                        intent.putExtra("SellerNumber", SellerNumber);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddSeller.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}