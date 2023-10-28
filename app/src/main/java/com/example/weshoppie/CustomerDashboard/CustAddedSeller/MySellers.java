package com.example.weshoppie.CustomerDashboard.CustAddedSeller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust.AddedCustomers;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust.CustShow;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust.CustomerAdapter;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class MySellers extends AppCompatActivity implements SelectSeller{

    public static final String TAG = "My Sellers Activity";
    String userID;
    RecyclerView recyclerView;
    ArrayList<SellerShow> sellerShowArrayList;
    SellerAdapter sellerAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sellers);

        recyclerView = findViewById(R.id.recyclerSeller);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userID = CurrentUser.getUid();

        db = FirebaseFirestore.getInstance();
        sellerShowArrayList = new ArrayList<SellerShow>();
        sellerAdapter = new SellerAdapter(MySellers.this,sellerShowArrayList, this);
        recyclerView.setAdapter(sellerAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("Customer").document(userID).collection("My_Sellers").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){
                            Log.d(TAG, "onEvent: error: "+error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                SellerShow cs = dc.getDocument().toObject(SellerShow.class);
                                cs.setDocumentID(dc.getDocument().getId());
                                sellerShowArrayList.add(cs);
                            }
                            sellerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(SellerShow sellerShow) {
        Dialog dialog = new Dialog(MySellers.this);
        dialog.setContentView(R.layout.seller_profile_view);

        ProgressBar progressBar = dialog.findViewById(R.id.profileloadingSeller);
        ImageView callSeller = dialog.findViewById(R.id.callSeller);

        TextView ShopName, ShopType, Number, OwnerName, Open, Close, OpenText, CloseText, Address, City, Pincode, Description;
        ShopName = dialog.findViewById(R.id.dialog_shop_name);
        ShopType = dialog.findViewById(R.id.dialog_shop_type);
        Number = dialog.findViewById(R.id.dialog_shop_number);
        OwnerName = dialog.findViewById(R.id.dialog_owner_name);
        OpenText = dialog.findViewById(R.id.dialog_open_text);
        CloseText = dialog.findViewById(R.id.dialog_close_text);
        Open = dialog.findViewById(R.id.dialog_open);
        Close = dialog.findViewById(R.id.dialog_close);
        Address = dialog.findViewById(R.id.dialog_add);
        City = dialog.findViewById(R.id.dialog_shop_city);
        Pincode = dialog.findViewById(R.id.dialog_shop_pin);
        Description = dialog.findViewById(R.id.dialog_description);

        db.collection("Shopkeeper").document(sellerShow.getDocumentID()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            ShopName.setText(documentSnapshot.get("Shop_Name").toString());
                            ShopType.setText("( " + documentSnapshot.get("Shop_Type").toString() + " )");
                            Number.setText(documentSnapshot.get("Owner_Phone").toString());
                            OwnerName.setText("Owner: "+documentSnapshot.get("Owner_Name").toString());
                            Open.setText(documentSnapshot.get("Opening_Time").toString());
                            Close.setText(documentSnapshot.get("Closing_Time").toString());
                            Address.setText("Address: "+documentSnapshot.get("Shop_Address").toString());
                            City.setText(documentSnapshot.get("Shop_City").toString());
                            Pincode.setText(documentSnapshot.get("Shop_Pincode").toString());
                            Description.setText("Description: "+documentSnapshot.get("Shop_Description").toString());

                            progressBar.setVisibility(View.GONE);
                            callSeller.setVisibility(View.VISIBLE);
                            ShopName.setVisibility(View.VISIBLE);
                            ShopType.setVisibility(View.VISIBLE);
                            Number.setVisibility(View.VISIBLE);
                            OwnerName.setVisibility(View.VISIBLE);
                            OpenText.setVisibility(View.VISIBLE);
                            CloseText.setVisibility(View.VISIBLE);
                            Open.setVisibility(View.VISIBLE);
                            Close.setVisibility(View.VISIBLE);
                            Address.setVisibility(View.VISIBLE);
                            City.setVisibility(View.VISIBLE);
                            Pincode.setVisibility(View.VISIBLE);
                            Description.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(MySellers.this, "Task Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MySellers.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.show();
    }
}