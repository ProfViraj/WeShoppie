package com.example.weshoppie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class RegisterShopkeeper extends AppCompatActivity {

    String[] shop_types = {"Select the shop type","Art Gallery","Automotive Showroom","Bakery","Barbershop","Beauty Salon","Big-box Store","Book Store",
                            "Brand Flagship","Brand Shop","Cafe","Cake Shop","Candy Store","Chicken/Mutton Shop","Clothing Store",
                            "Co-operative","Collectables","Convenience Store","Craft Shop","Departmental Store","Donut Shop","Dress Shop",
                            "Dry Cleaner","Electronic Store","Fabric/Sewing Supplies","Farmers Market","Fashion Boutique","Fast Food Restaurant",
                            "Fish Shop","Franchise/Chain Store","Gas Station","General Store","Gift Shop","Hobby Shop","Home Decor","Home Improvement",
                            "Imported Goods","Jeweler","Junk Shop","Kitchen Store","Luxury Brand Shop","Mattress Store","Mechanic/Garage",
                            "Music Shop","Newspaper Stand","Other","Pharmacy/Drug Store","Thrift Shop","Service Center","Software/Video Games",
                            "Souvenir Shop","Sports Store","Stationery Shop","Street Vendors","Supermarket","Surplus Shop","Tailor",
                            "Takeout Restaurant","Tattoo Shop","Ticket Vendors","Trading Shop","Travel Agent","Truck Stop","Variety Store",
                            "Vegetable Market","Wholesaler"};
    TextView Shop_Open, Shop_Close;
    String shop,userid;
    EditText Shop_Name, Shop_Address, shop_city, pincode, state, shop_description, Owner_Name, Owner_Phone;
    Button Shopkeeper_Register;
    Spinner shop_type;
    FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //To check if the data is already saved*********************************************************
    @Override
    protected void onStart() {
        super.onStart();
        userid = CurrentUser.getUid();

        //Reading data from teh DATABASE************************************************************
        db.collection("Shopkeeper").document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            startActivity(new Intent(RegisterShopkeeper.this, CustomerDashboard.class));
                            finish();
                        }
                    }
                });
        //******************************************************************************************
    }
    //***********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shopkeeper);

        shop_type = findViewById(R.id.Shop_Type);

        //Setting adapter to the spinner view of shop type *****************************************
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterShopkeeper.this, android.R.layout.simple_spinner_item, shop_types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shop_type.setAdapter(adapter);
        shop_type.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shop = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegisterShopkeeper.this, "Enter the Shop Type", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        //******************************************************************************************

        //Initiations *******************************************************************************
        userid = CurrentUser.getUid();

        Shop_Name = findViewById(R.id.Shop_Name);
        Shop_Address = findViewById(R.id.Shop_Address);
        shop_city = findViewById(R.id.shop_city);
        pincode = findViewById(R.id.pincode);
        state = findViewById(R.id.state);
        shop_description = findViewById(R.id.shop_description);
        Owner_Name = findViewById(R.id.Owner_Name);
        Owner_Phone = findViewById(R.id.Owner_Phone);

        Shop_Open = findViewById(R.id.Shop_Open_Time);
        Shop_Close = findViewById(R.id.Shop_Close_Time);

        Shopkeeper_Register = findViewById(R.id.Shopkeeper_Register);
        //******************************************************************************************

        //Setting Opening Time **********************************************************************
        Shop_Open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(RegisterShopkeeper.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String am = "AM", pm = "PM";
                        if (hourOfDay<12){
                            Shop_Open.setText(String.valueOf(hourOfDay)+" : "+String.valueOf(minute)+" "+am);
                        }
                        else {
                            Shop_Open.setText(String.valueOf(hourOfDay)+" : "+String.valueOf(minute)+" "+pm);
                        }
                    }
                }, 0, 0, false);
                tpd.show();
            }
        });
        //******************************************************************************************

        //Setting Closing Time* *********************************************************************
        Shop_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(RegisterShopkeeper.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                        String am = "AM", pm = "PM";
                        if (hourOfDay<12){
                            Shop_Close.setText(String.valueOf(hourOfDay)+" : "+String.valueOf(minute)+" "+am);
                        }
                        else {
                            Shop_Close.setText(String.valueOf(hourOfDay)+" : "+String.valueOf(minute)+" "+pm);
                        }
                    }
                }, 0, 0, false);
                tpd.show();
            }
        });
        //******************************************************************************************

        //Clicking on register your shop button *****************************************************
        Shopkeeper_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Taking values from the views to local strings *************************************
                String shopname, shopaddress, shopcity, shoppincode, shopstate, shopopen, shopclose, shoptype, shopdescription, Ownername, Ownerphone;
                Ownername = Owner_Name.getText().toString();
                Ownerphone = Owner_Phone.getText().toString();
                shopname = Shop_Name.getText().toString();
                shopaddress = Shop_Address.getText().toString();
                shopcity = shop_city.getText().toString();
                shoppincode = pincode.getText().toString();
                shopstate = state.getText().toString();
                shopopen = Shop_Open.getText().toString();
                shopclose = Shop_Close.getText().toString();
                shoptype = shop_type.getSelectedItem().toString();
                shopdescription = shop_description.getText().toString();
                //**********************************************************************************

                //Checking if the fields are empty **************************************************
                if(TextUtils.isEmpty(Ownername)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Owner's Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Ownerphone)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shopname)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Shop Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shopaddress)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Shop Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shopcity)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Shop City", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shoppincode)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Shop Pincode", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shopstate)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Shop State", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shopopen)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Shop Opening Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shopclose)){
                    Toast.makeText(RegisterShopkeeper.this, "Enter the Shop Closing Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shoptype) || shoptype.equals("Select the shop type")){
                    Toast.makeText(RegisterShopkeeper.this, "Select the shop type", Toast.LENGTH_SHORT).show();
                    return;
                }
                //**********************************************************************************

                //Putting values into the collection of hashmap ************************************
                Map<String,Object> shopkeeper = new HashMap<>();
                shopkeeper.put("Owner's Name",Ownername);
                shopkeeper.put("Owner's Phone",Ownerphone);
                shopkeeper.put("Shop Name",shopname);
                shopkeeper.put("Shop Type",shoptype);
                shopkeeper.put("Shop Address",shopaddress);
                shopkeeper.put("Shop City",shopcity);
                shopkeeper.put("Shop Pincode",shoppincode);
                shopkeeper.put("Shop State",shopstate);
                shopkeeper.put("Opening Time",shopopen);
                shopkeeper.put("Closing Time",shopclose);

                if (TextUtils.isEmpty(shopdescription)){

                } else {
                    shopkeeper.put("Shop Description",shopdescription);
                }
                //**********************************************************************************

                //Adding data to the DATABASE*******************************************************
                db.collection("Shopkeeper").document(userid).set(shopkeeper)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(RegisterShopkeeper.this, "Data Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterShopkeeper.this, CustomerDashboard.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterShopkeeper.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                //**********************************************************************************
            }
        });
    }
}