package com.example.weshoppie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    private EditText Cust_Email, Password;
    private Button login;
    private FirebaseAuth mAuth;
    TextView createNew, forgotPassword;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            if (currentUser.isEmailVerified()){
                startActivity(new Intent(LoginPage.this, SelectUserType.class));
                finish();
            }
            else {
                Toast.makeText(this, "Email is not verified yet!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Cust_Email = findViewById(R.id.Cust_Email);
        Password = findViewById(R.id.password);

        login = findViewById(R.id.login);

        mAuth =FirebaseAuth.getInstance();

        createNew = findViewById(R.id.create);
        forgotPassword = findViewById(R.id.forgot_password);

        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, Registration.class));
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, ForgotPassword.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password;
                email = Cust_Email.getText().toString();
                password = Password.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginPage.this, "Enter The Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginPage.this, "Enter The Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (mAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginPage.this, SelectUserType.class));
                                finish();
                            } else {
                                Toast.makeText(LoginPage.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(LoginPage.this, "Login Unsuccessful "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}