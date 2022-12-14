package com.example.firebaseapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button button;
    private TextView signIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.button);
        signIn = findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();

        // if user already signed in
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this, Home.class));
        }

        //sign up button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(!emailText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    if(passwordText.length()>=8){

                            // sending data to firebase
                            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            // send email
                                            mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(MainActivity.this, "Signed up successfully and verification mail sent", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MainActivity.this, "Signed up successfully and verification mail not sent", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                            startActivity(new Intent(MainActivity.this, Home.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, "Sign up failed : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                    }
                    else{ Toast.makeText(MainActivity.this,"Password is invalid",Toast.LENGTH_SHORT).show(); password.setError("Password includes at least 8 characters"); }
                }
                else{ Toast.makeText(MainActivity.this,"invalid Email Address",Toast.LENGTH_SHORT).show(); email.setError("Email should be valid !!"); }
            }
        });

        // sign in button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
            }
        });
    }
}