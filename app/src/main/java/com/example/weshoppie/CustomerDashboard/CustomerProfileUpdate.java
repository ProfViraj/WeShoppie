package com.example.weshoppie.CustomerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.example.weshoppie.login.RegisterCustomer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerProfileUpdate extends AppCompatActivity {

    EditText Address_1, Address_2 , cust_city, pincode, state, Cust_Name, Cust_Phone;
    Button Update_Profile;
    String userid, usermail;
    FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_update);

        Address_1 = findViewById(R.id.Address_1_update);
        Address_2 = findViewById(R.id.Address_2_update);
        cust_city = findViewById(R.id.cust_city_update);
        pincode = findViewById(R.id.pincode_update);
        state = findViewById(R.id.state_update);
        Cust_Name = findViewById(R.id.Cust_Name_update);
        Cust_Phone = findViewById(R.id.Cust_Phone_update);

        userid = CurrentUser.getUid();
        usermail = CurrentUser.getEmail();
        //Get the previously stored data ********************************************************************
        getData();

        Update_Profile = findViewById(R.id.Cust_Profile_Update);
        //Update Profile Button *****************************************************************************
        Update_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cust_name,Cust_phone,Cust_add_1,Cust_add_2,Cust_city,Cust_pincode,Cust_state;
                Cust_name = Cust_Name.getText().toString();
                Cust_phone = Cust_Phone.getText().toString();
                Cust_add_1 = Address_1.getText().toString();
                Cust_add_2 = Address_2.getText().toString();
                Cust_city = cust_city.getText().toString();
                Cust_pincode = pincode.getText().toString();
                Cust_state = state.getText().toString();
                //Checking if the fields are empty *************************************************************************************
                if(TextUtils.isEmpty(Cust_name)){
                    Toast.makeText(CustomerProfileUpdate.this, "Enter the Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Cust_phone)){
                    Toast.makeText(CustomerProfileUpdate.this, "Enter the Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Cust_add_1)){
                    Toast.makeText(CustomerProfileUpdate.this, "Enter the Address line 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Cust_add_2)){
                    Toast.makeText(CustomerProfileUpdate.this, "Enter the Address line 2", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Cust_city)){
                    Toast.makeText(CustomerProfileUpdate.this, "Enter the City", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Cust_pincode)){
                    Toast.makeText(CustomerProfileUpdate.this, "Enter the Pincode", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Cust_state)){
                    Toast.makeText(CustomerProfileUpdate.this, "Enter the State", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Adding data to HashMap *************************************************************************************
                Map<String,Object> userdata = new HashMap<>();
                userdata.put("Name",Cust_name);
                userdata.put("Mobile_Number",Cust_phone);
                userdata.put("Address_1",Cust_add_1);
                userdata.put("Address_2",Cust_add_2);
                userdata.put("City",Cust_city);
                userdata.put("Pincode",Cust_pincode);
                userdata.put("State",Cust_state);
                userdata.put("Email",usermail);
                //Updating the data ********************************************************************************************************
                db.collection("Customer").document(userid).set(userdata)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(CustomerProfileUpdate.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CustomerProfileUpdate.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void getData() {
        db.collection("Customer").document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot ds = task.getResult();
                            Cust_Name.setText(ds.get("Name").toString());
                            Cust_Phone.setText(ds.get("Mobile_Number").toString());
                            Address_1.setText(ds.get("Address_1").toString());
                            Address_2.setText(ds.get("Address_2").toString());
                            cust_city.setText(ds.get("City").toString());
                            pincode.setText(ds.get("Pincode").toString());
                            state.setText(ds.get("State").toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerProfileUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}