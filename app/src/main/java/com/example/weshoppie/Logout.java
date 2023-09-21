package com.example.weshoppie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logout extends AppCompatActivity {

    private Button logout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        logout = findViewById(R.id.Logout);

        if (user == null){
            startActivity(new Intent(Logout.this, LoginPage.class));
            finish();
        }
        else {
            Toast.makeText(this, "Current User is "+user.getEmail(), Toast.LENGTH_SHORT).show();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Logout.this, LoginPage.class));
                finish();
            }
        });
    }
}