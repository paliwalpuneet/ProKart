package com.example.mobiles.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobiles.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {


    EditText email,password;
    Button login,register;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(Login.this,MainActivity.class));
            finish();
        }

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.pass_login);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);


        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login:
                loginUser();
                break;
            case R.id.register:

                startActivity(new Intent(this,Register.class));
                finish();

                break;
        }
    }

    public void loginUser()
    {
        String emailid = email.getText().toString();
        String pass = password.getText().toString();


        if (TextUtils.isEmpty(emailid)) {
            email.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            password.setError("Password is required");
            return;
        }

        if (pass.length() < 6) {
            password.setError("Password must be >= 6 characters");
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(emailid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(Login.this, "Some Error Occurred.Please try again after some time. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("User Login", "onFailure: "+e);
            }
        });


    }


}