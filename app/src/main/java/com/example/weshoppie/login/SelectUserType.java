package com.example.weshoppie.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.CustomerDashboard.CustomerDashboardNew;
import com.example.weshoppie.R;
import com.example.weshoppie.ShopkeeperDashboard.ShopkeeperDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectUserType extends AppCompatActivity {

    public static final String TAG = "Select user Type Activity";
    TextView Welcome;
    String userid, usermail;
    Button customer_login;
    Button shopkeeper_login;
    Button logout;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        customer_login = findViewById(R.id.customer_login);
        shopkeeper_login = findViewById(R.id.shopkeeper_login);
        logout = findViewById(R.id.Logout);
        Welcome = findViewById(R.id.WelcomeUser);
        userid = user.getUid();
        usermail = user.getEmail();
        Welcome.setText("Login by: "+usermail);
        //Customer Login Button**********************************************************************************************************
        customer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking if the Data exists********************************************************************************************
                db.collection("Customer").document(userid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    if(task.getResult().exists()){
                                        //if Exists -> Go to Customer Dashboard******************************************************************
                                        startActivity(new Intent(SelectUserType.this, CustomerDashboardNew.class));
                                    }
                                    else {
                                        //Does not exist -> Go to Customer Register*****************************************************
                                        Intent intent = new Intent(SelectUserType.this, RegisterCustomer.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(SelectUserType.this, "Task Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        //Shopkeeper Login Button************************************************************************************************
        shopkeeper_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking if the Data exists********************************************************************************************
                db.collection("Shopkeeper").document(userid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists()){
                                    //if Exists -> Go to Shopkeeper Dashboard******************************************************************
                                    startActivity(new Intent(SelectUserType.this, ShopkeeperDashboard.class));
                                }
                                else {
                                    //Does not exist -> Go to Shopkeeper Register*****************************************************
                                    Intent intent = new Intent(SelectUserType.this, RegisterShopkeeper.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
        //Logout button ****************************************************************************************
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectUserType.this, "Signing out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SelectUserType.this, LoginPage.class));
                finish();
            }
        });
    }
}