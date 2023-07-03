package com.adham.saveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.adham.saveme.firebase.MessagingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


public class login extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText email, password;
    Button login,signup;
    Database DB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_password);

        password.setTransformationMethod(new PasswordTransformationMethod());

        login=(Button)findViewById(R.id.login);
        DB=new Database(this);

        signup=(Button)findViewById(R.id.signup);

        //---------------------------------
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(login.this, signup.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });



    }

    public void userLogin(){
        String mail=email.getText().toString();
        String pass=password.getText().toString();

        if (!validateEmail() | !validatePassword()){
            return;
        }

        mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(login.this, AnimationT.class));
                }
            }
        });

    }



    private Boolean validateEmail(){
        String val = email.getText().toString().trim();
        if (val.isEmpty()){
            email.setError("Email is required");
            return false;
        }
        else {
            email.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getText().toString();
        if (val.isEmpty()){
            password.setError("Password is required");
            return false;
        }
        else if (val.length()<6){
            password.setError("Min password length should be 6 character!");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

}