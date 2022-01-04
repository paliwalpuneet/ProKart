package com.example.mobiles.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobiles.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText name,email,mobile,password,address;
    Button login,register;

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        name = findViewById(R.id.name_register);
        email = findViewById(R.id.email_register);
        mobile = findViewById(R.id.mobile_register);
        password = findViewById(R.id.pass_register);
        address = findViewById(R.id.address_register);


        login = findViewById(R.id.login_register);
        register = findViewById(R.id.register);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.login_register:
                startActivity(new Intent(this, Login.class));
                finish();
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }

    public void registerUser()
    {
        String fullName = name.getText().toString();
        String emailId = email.getText().toString();
        String mobileNo = mobile.getText().toString();
        String pass = password.getText().toString();
        String add = address.getText().toString();


        if(TextUtils.isEmpty(fullName))
            name.setError("Name is Required");
        if(TextUtils.isEmpty(emailId))
            email.setError("Email is Required");
        if(TextUtils.isEmpty(mobileNo))
            mobile.setError("Mobile is Required");
        if(TextUtils.isEmpty(pass))
            password.setError("Password is Required");
        if(TextUtils.isEmpty(add))
            address.setError("Address is Required");

        if(pass.length()<6)
            password.setError("Password must be >= 6 characters");


        firebaseAuth.createUserWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    userId = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firestore.collection("Users").document(userId);
                    Map<String,Object> user = new HashMap<>();
                    user.put("name",fullName);
                    user.put("email",emailId);
                    user.put("mobile",mobileNo);
                    user.put("address",add);
                    user.put("password",pass);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(Register.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Some error Occurred.Please try again after some time", Toast.LENGTH_SHORT).show();
                            Log.d("User Creation Error", "onFailure: "+e);
                        }
                    });

                    if(firebaseAuth.getCurrentUser()!=null)
                        firebaseAuth.signOut();
                    startActivity(new Intent(Register.this,Login.class));
                    finish();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("User Registration Error", "onFailure: "+e);
            }
        });

    }
}