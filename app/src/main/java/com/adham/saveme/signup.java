package com.adham.saveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.adham.saveme.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    EditText name,email, pw, confirmpw;
    Button signup;
    Database DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();

        name=(EditText)findViewById(R.id.signup_name);
        email=(EditText)findViewById(R.id.signup_email);
        pw=(EditText)findViewById(R.id.signup_password);
        confirmpw=(EditText)findViewById(R.id.signup_confirmpw);

//        getSupportActionBar().setTitle("SIGN UP");

        DB=new Database(this);

        signup=(Button)findViewById(R.id.signup);

        pw.setTransformationMethod(new PasswordTransformationMethod());
        confirmpw.setTransformationMethod(new PasswordTransformationMethod());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validateUserName()){
                    name.setError(null);
                }
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validateEmail()){
                    email.setError(null);
                }
            }
        });
        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validatePassword()){
                    pw.setError(null);
                }
            }
        });
        confirmpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validateCmPassword()){
                    confirmpw.setError(null);
                }
            }
        });


    }

    public void registerUser(){

        String username=name.getText().toString().trim();
        String emailId = email.getText().toString().trim();
        String password=pw.getText().toString().trim();
        String confirmpass=confirmpw.getText().toString().trim();

        if (!validateUserName() | !validateEmail() | !validatePassword() | !validateCmPassword()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailId,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.e("reg","account created");
                            User user = new User(username,emailId,password);
                            rootNode.getReference("Users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Log.e("reg","data created");
                                                startActivity(new Intent(signup.this, AnimationT.class));
                                            }
                                            else {
                                                Log.e("reg","data not created");
                                            }
                                        }
                                    });

                        }
                        else {
                        }
                    }
                });

    }

    private Boolean validateUserName(){
        String val = name.getText().toString().trim();
        if (val.isEmpty()){
            name.setError(" User Name is required");
            return false;
        }
        else {
            name.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = email.getText().toString().trim();
        if (val.isEmpty()){
            name.setError("Email is required");
            return false;
        }
        else {
            name.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = pw.getText().toString().trim();
        if (val.isEmpty()){
            pw.setError("Password is required");
            return false;
        }
        else if (val.length()<6){
            pw.setError("Min password length should be 6 character!");
            return false;
        }
        else {
            pw.setError(null);
            return true;
        }
    }

    private Boolean validateCmPassword(){
        String val = confirmpw.getText().toString().trim();
        String password = pw.getText().toString().trim();
        if (val.isEmpty()){
            confirmpw.setError("Confirm Password is required");
            return false;
        }
        else if (!val.equals(password)){
            confirmpw.setError("That doesn't math the password");
            return false;
        }
        else {
            confirmpw.setError(null);
            return true;
        }
    }


}