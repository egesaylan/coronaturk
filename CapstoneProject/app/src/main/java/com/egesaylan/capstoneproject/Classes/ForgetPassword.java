package com.egesaylan.capstoneproject.Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.egesaylan.capstoneproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {

    private EditText emailEditText;
    private AppCompatButton resetPasswordButton;
    private ProgressBar progressbar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEditText = findViewById(R.id.forgetPasswordEmailTV);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        progressbar  = findViewById(R.id.forgetPasswordProgress);

        mAuth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });


    }

    public void resetPassword(){
        String email = emailEditText.getText().toString().trim();


        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide a vaild Email");
            emailEditText.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPassword.this, "Reset link has been sent please check your Email", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgetPassword.this, "Something went wrong please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}