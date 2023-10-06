package com.example.weshoppie.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weshoppie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class Registration extends AppCompatActivity {

    private TextView click_to_login;
    private EditText Cust_Email, Cust_Set_Password, Name, Phone;
    private Button Cust_Register;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        Cust_Email = findViewById(R.id.Cust_Email);
        Cust_Set_Password = findViewById(R.id.Cust_Set_Password);

        progressbar = findViewById(R.id.progressbar);

        Cust_Register = findViewById(R.id.Cust_Register);

        click_to_login = findViewById(R.id.click_to_login);

        click_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, LoginPage.class));
                finish();
            }
        });

        Cust_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);

                String email, password, name, phone;
                email = Cust_Email.getText().toString();
                password = Cust_Set_Password.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Registration.this, "Enter The Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Registration.this, "Enter The Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Registration.this, "Please verify your Email", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Registration.this, LoginPage.class));
                                    finish();
                                }
                            });
                        }
                        else {
                            Toast.makeText(Registration.this, "Registration Unsuccessful " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }
}