package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText mSignUpEmail, mSignUpPassword;
    private RelativeLayout mSignUp;
    private TextView mGoToLogin;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        mSignUpEmail = findViewById(R.id.sign_up_email);
        mSignUpPassword = findViewById(R.id.sign_up_password);
        mSignUp = findViewById(R.id.signup);
        mGoToLogin = findViewById(R.id.go_to_login);

        firebaseAuth = FirebaseAuth.getInstance();


        mGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);

            }
        });
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mSignUpEmail.getText().toString().trim();
                String password = mSignUpPassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"All Fields Required",Toast.LENGTH_SHORT).show();
                }else if (password.length()<7) {
                    Toast.makeText(getApplicationContext(), "Too Short", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Registration Successful!",Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Failed to register",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }

    //Verification email
    private void  sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                     Toast.makeText(getApplicationContext(),"Verification Email Sent, Verify and login again",Toast.LENGTH_SHORT).show();
                     firebaseAuth.signOut();
                     finish();
                     startActivity(new Intent(SignUp.this, MainActivity.class));
             }
            });
        }
        else
        {
                       Toast.makeText(getApplicationContext(),"Failed to send Email!    ",Toast.LENGTH_SHORT).show();


        }
    }
}