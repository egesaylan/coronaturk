package com.egesaylan.capstoneproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.egesaylan.capstoneproject.Classes.ForgetPassword;
import com.egesaylan.capstoneproject.Classes.MainActivity;
import com.egesaylan.capstoneproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    public EditText loginEmail, loginPassword;//Login kısmındaki edittextleri tanımladık
    public AppCompatButton loginButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    public TextView forgetPassword;
    public CheckBox checkBoxLogin;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_fragment, container, false);

        loginButton = root.findViewById(R.id.loginButton);//login kısmındaki initialize edilen değer

        forgetPassword = root.findViewById(R.id.forgetPassword);

        loginEmail = root.findViewById(R.id.login_email);//login kısmındaki initialize edilen değer
        loginPassword = root.findViewById(R.id.login_password);//login kısmındaki initialize edilen değer

        checkBoxLogin = root.findViewById(R.id.checkboxLogin);

        progressBar = root.findViewById(R.id.loginProgress);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");
        if (checkbox.equals("true")) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgetPassword.class));
            }
        });

        checkBoxLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();
                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(getActivity(), "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }

        });


        return root;
    }

    private void Login() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            loginEmail.setError("Email is required");
            loginEmail.requestFocus();

            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {//Doğru bir email adresi girilmesi için gereken koşul
            loginEmail.setError("Please provide valid email");
            loginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loginPassword.setError("Please enter your password");
            loginPassword.requestFocus();

            return;
        }
        if (password.length() < 6) {
            loginPassword.setError("Please enter a password more than 5 characters");
            loginPassword.requestFocus();

            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Handler handler = new Handler();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                progressBar.setVisibility(View.GONE);
                            }
                        }, 1500);
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(getActivity(), "Please verify you email address", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }


                } else {
                    Toast.makeText(getActivity(), "Failed to login! Please check your email or password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
