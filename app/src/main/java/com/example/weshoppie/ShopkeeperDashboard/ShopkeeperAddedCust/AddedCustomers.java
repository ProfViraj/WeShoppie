package com.example.weshoppie.ShopkeeperDashboard.ShopkeeperAddedCust;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddedCustomers extends AppCompatActivity implements SelectCustomer{

    public static final String TAG = "Added Customers";
    SearchView searchView;
    String userID;
    RecyclerView recyclerView;
    ArrayList<CustShow> custShowArrayList;
    CustomerAdapter customerAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser CurrentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_customers);
        //Implementing search view ***************************************************************************
        searchView = findViewById(R.id.searchViewCust);
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
        //Implementing the recycler view ************************************************************************
        recyclerView = findViewById(R.id.recyclerCustomer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userID = CurrentUser.getUid();

        db = FirebaseFirestore.getInstance();
        custShowArrayList = new ArrayList<CustShow>();
        customerAdapter = new CustomerAdapter(AddedCustomers.this,custShowArrayList, this);
        recyclerView.setAdapter(customerAdapter);

        EventChangeListener();
    }

    private void filterList(String newText) {
        ArrayList<CustShow> filteredList = new ArrayList<CustShow>();
        for (CustShow custShow : custShowArrayList){
            if (custShow.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(custShow);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No such Customer added", Toast.LENGTH_SHORT).show();
        } else {
            customerAdapter.setFilteredList(filteredList);
        }
    }

    private void EventChangeListener() {
        //Realtime updates for added customers *********************************************************************
        db.collection("Shopkeeper").document(userID).collection("Added_Customers").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Log.d(TAG, "onEvent: error: "+error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED){
                        CustShow cs = dc.getDocument().toObject(CustShow.class);
                        cs.setDocumentID(dc.getDocument().getId());
                        custShowArrayList.add(cs);
                    }
                    customerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClicked(CustShow custShow) {
        //On selecting a customer *******************************************************************************
        Dialog dialog = new Dialog(AddedCustomers.this);
        dialog.setContentView(R.layout.customer_profile_view);

        TextView Name = dialog.findViewById(R.id.dialog_name);
        TextView Number = dialog.findViewById(R.id.dialog_number);
        TextView Add1 = dialog.findViewById(R.id.dialog_add1);
        TextView Add2 = dialog.findViewById(R.id.dialog_add2);
        TextView City = dialog.findViewById(R.id.dialog_city);
        TextView Pincode = dialog.findViewById(R.id.dialog_pin);
        ProgressBar Progress = dialog.findViewById((R.id.profileloading));
        //Getting the information of customers ****************************************************************************
        db.collection("Customer").document(custShow.getDocumentID()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    Name.setText(documentSnapshot.get("Name").toString());
                                    Number.setText(documentSnapshot.get("Mobile_Number").toString());
                                    Add1.setText(documentSnapshot.get("Address_1").toString());
                                    Add2.setText(documentSnapshot.get("Address_2").toString());
                                    City.setText(documentSnapshot.get("City").toString());
                                    Pincode.setText(documentSnapshot.get("Pincode").toString());

                                    Progress.setVisibility(View.GONE);
                                    Name.setVisibility(View.VISIBLE);
                                    Number.setVisibility(View.VISIBLE);
                                    Add1.setVisibility(View.VISIBLE);
                                    Add2.setVisibility(View.VISIBLE);
                                    City.setVisibility(View.VISIBLE);
                                    Pincode.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(AddedCustomers.this, "Task Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddedCustomers.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        dialog.show();
    }
}