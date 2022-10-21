package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button button;
    private TextView signUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.button);
        signUp = findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,MainActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(!emailText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    if(passwordText.length()>=8){
                        // sending data to firebase
                        mAuth.signInWithEmailAndPassword(emailText,passwordText)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(SignInActivity.this,"Signed in successfully",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignInActivity.this,Home.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignInActivity.this,"Signed in failed",Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else{ Toast.makeText(SignInActivity.this,"Password is invalid",Toast.LENGTH_SHORT).show(); password.setError("Password includes at least 8 characters"); }
                }
                else{ Toast.makeText(SignInActivity.this,"invalid Email Address",Toast.LENGTH_SHORT).show(); email.setError("Email should be valid !!"); }
            }
        });
    }
}