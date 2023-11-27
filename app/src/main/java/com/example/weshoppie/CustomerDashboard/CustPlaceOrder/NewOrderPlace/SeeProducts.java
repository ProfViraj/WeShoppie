package com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrderPlace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustAddedSeller.MySellers;
import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrUndeliveredOrders;
import com.example.weshoppie.CustomerDashboard.CustPlaceOrder.NewOrderPlace.SeeOrderPlaced.OrderPlaced;
import com.example.weshoppie.CustomerDashboard.CustomerDashboardNew;
import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SeeProducts extends AppCompatActivity implements SelectCount{

    public static final String TAG = "See Products Activity";
    SearchView searchView;
    boolean isProductListEmpty=true;
    int AmountInt;
    Intent fromAct;
    String ShopId, UserId, Number, Name, time, AmountStr;
    String OrderID;
    TextView Shopname;
    Button Placeorder, CancelOrder;
    ArrayList<SelectProductModel> arrSelectProducts;
    RecyclerView recyclerView;
    SelectProductAdapter selectProductAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_products);

        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        time = formatter.format(currentTime);

        fromAct = getIntent();
        ShopId = fromAct.getStringExtra("OwnerId");
        OrderID = fromAct.getStringExtra("BillNo");
        UserId = CurrentUser.getUid();

        Shopname = findViewById(R.id.Shopname);
        Placeorder = findViewById(R.id.place_order);
        CancelOrder = findViewById(R.id.cancel_order);
        searchView = findViewById(R.id.searchViewCustSeeProducts);
        //Implementing search view ***************************************************************
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
        //Getting shopkeeper's name ************************************************************************************************8
        db.collection("Shopkeeper").document(ShopId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String shop_name_retrieved = Objects.requireNonNull(task.getResult().get("Shop_Name")).toString();
                            Shopname.setText(shop_name_retrieved);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, "Shop Name not retrieved", Toast.LENGTH_SHORT).show();
                    }
                });
        //On placing the order ********************************************************************************************************
        Placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProductListEmpty){
                    Toast.makeText(SeeProducts.this, "Select at least one product", Toast.LENGTH_SHORT).show();
                    return;
                }

                Dialog dialog = new Dialog(SeeProducts.this);
                dialog.setContentView(R.layout.expected_time_dialog);
                ImageView Clock;
                TextView TimeShow;
                Button SubmitTime;

                Clock = dialog.findViewById(R.id.time_set_up);
                TimeShow = dialog.findViewById(R.id.time_show);
                SubmitTime = dialog.findViewById(R.id.submit);
                //Setting the estimated time ***********************************************************************************************
                Clock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(SeeProducts.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String am = "AM", pm = "PM";
                                if (hourOfDay<12){
                                    TimeShow.setText(String.valueOf(hourOfDay)+" : "+String.valueOf(minute)+" "+am);
                                }
                                else {
                                    TimeShow.setText(String.valueOf(hourOfDay)+" : "+String.valueOf(minute)+" "+pm);
                                }
                            }
                        }, 0, 0, false);
                        timePickerDialog.show();
                    }
                });
                //Submitting the estimated time ******************************************************************************
                SubmitTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Time = TimeShow.getText().toString();
                        if (TextUtils.isEmpty(Time)){
                            Toast.makeText(SeeProducts.this, "Set the Expected Time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        db.collection("Orders").document(OrderID).update("Expected_Time",Time)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SeeProducts.this, "Time set successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SeeProducts.this, OrderPlaced.class);
                                            intent.putExtra("BillNo", OrderID);
                                            intent.putExtra("ShopId", ShopId);
                                            intent.putExtra("Date", time);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                dialog.show();
            }
        });
        //On cancel the order *******************************************************************************************
        CancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrder();
            }
        });

        recyclerView = findViewById(R.id.recyclerSellerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrSelectProducts = new ArrayList<SelectProductModel>();
        selectProductAdapter = new SelectProductAdapter(SeeProducts.this, arrSelectProducts,this);

        recyclerView.setAdapter(selectProductAdapter);
        //Getting customer's name and number for further use ***********************************************************
        db.collection("Customer").document(UserId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    Number = Objects.requireNonNull(task.getResult().get("Mobile_Number")).toString();
                                    Name = Objects.requireNonNull(task.getResult().get("Name")).toString();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //Realtime updates for products ***************************************************************************
        EventChangeListener();
    }

    private void filterList(String newText) {
        ArrayList<SelectProductModel> filteredList = new ArrayList<SelectProductModel>();
        for (SelectProductModel item : arrSelectProducts){
            if (item.getProduct_Name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such Product exists", Toast.LENGTH_SHORT).show();
        } else {
            selectProductAdapter.setFilteredList(filteredList);
        }
    }

    private void DeleteOrder() {
        //On cancelling the order ******************************************************************************************************
        db.collection("Orders").document(OrderID).update("Cancellation", true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SeeProducts.this, "Order Cancelled Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SeeProducts.this, NewOrUndeliveredOrders.class));
                            finish();
                        } else {
                            Toast.makeText(SeeProducts.this, "Error...!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void EventChangeListener() {
        //Getting the shopkeepers products ********************************************************************************8
        db.collection("Products").whereEqualTo("Shopkeeper_id",ShopId).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            Log.d(TAG, "onEvent: "+ error.getMessage());
                            Toast.makeText(SeeProducts.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        assert value != null;
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                SelectProductModel spm = dc.getDocument().toObject(SelectProductModel.class);
                                spm.setDocumentID(dc.getDocument().getId());
                                arrSelectProducts.add(spm);
                            }
                            selectProductAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onQuantitySelected(SelectProductModel selectProductModel) {
        isProductListEmpty = false;
        //On selecting the products with quantity ************************************************************************
        Map<String,Object> ProductOrderData = new HashMap<>();
        ProductOrderData.put("Product_Name", selectProductModel.Product_Name);
        ProductOrderData.put("Product_Price",selectProductModel.Product_Price);
        ProductOrderData.put("Product_Price_Per",selectProductModel.Product_Price_per);
        ProductOrderData.put("Brand",selectProductModel.Brand);
        ProductOrderData.put("Count",selectProductModel.getCountstr());
        ProductOrderData.put("Total_Cost",selectProductModel.getCoststr());
        ProductOrderData.put("Product_Status","Unpacked");

        db.collection("Orders").document(OrderID).collection("Added_Products")
                .document(selectProductModel.documentID).set(ProductOrderData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SeeProducts.this, "ProductAdded", Toast.LENGTH_SHORT).show();
                        //Updating total count ********************************************************************************
                        db.collection("Orders").document(OrderID).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        AmountInt = Integer.parseInt(task.getResult().get("Amount").toString());
                                        AmountInt = AmountInt + Integer.parseInt(selectProductModel.getCoststr());
                                        AmountStr = String.valueOf(AmountInt);

                                        db.collection("Orders").document(OrderID)
                                                .update("Amount",AmountStr);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onQuantityNotSelected(SelectProductModel selectProductModel) {
        //Deleting the product from the order ***************************************************************************************
        db.collection("Orders").document(OrderID).collection("Added_Products")
                .document(selectProductModel.getDocumentID()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SeeProducts.this, "Unselected", Toast.LENGTH_SHORT).show();
                        db.collection("Orders").document(OrderID).collection("Added_Products").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            if (task.getResult().isEmpty()){
                                                isProductListEmpty = true;
                                                db.collection("Orders").document(OrderID).delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(SeeProducts.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeeProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCountToast() {
        Toast.makeText(SeeProducts.this, "Please select an appropriate count", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
    }
}