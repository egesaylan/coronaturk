package com.egesaylan.capstoneproject.Fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.egesaylan.capstoneproject.R;
import com.egesaylan.capstoneproject.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class SignupFragment extends Fragment {

    public FirebaseAuth mAuth;//Firebase kullanıcıyı databaseye yazdırmamız için gereken sınıf

    ProgressBar progressBar;
    EditText signupEmail, signupPassword, signupFullName, signupAge; //Register kısmındaki edittextleri tanımladık
    AppCompatButton signupButton;//Register kısmındaki buttonu tanımladık
    Spinner spinnerGender, spinnerIlceler;
    ArrayAdapter<CharSequence> adapterGender,adapterIlceler;

    TabLayout tabLayout;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);

        mAuth = FirebaseAuth.getInstance();

        signupButton = root.findViewById(R.id.signupButton);//SignUp kısmındaki initialize edilen değer

        progressBar = root.findViewById(R.id.signupProgress);

        signupEmail = root.findViewById(R.id.signup_email);//SignUp kısmındaki initialize edilen değer
        signupPassword = root.findViewById(R.id.signup_password);//SignUp kısmındaki initialize edilen değer
        signupFullName = root.findViewById(R.id.signup_FullName);//SignUp kısmındaki initialize edilen değer
        signupAge = root.findViewById(R.id.signup_Age);
        spinnerGender = root.findViewById(R.id.spinnerGender);
        spinnerIlceler = root.findViewById(R.id.spinnerIlceler);

        adapterGender = ArrayAdapter.createFromResource(getActivity(),R.array.GenderList,R.layout.support_simple_spinner_dropdown_item);
        adapterGender.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        adapterIlceler = ArrayAdapter.createFromResource(getActivity(),R.array.Ilceler,R.layout.support_simple_spinner_dropdown_item);
        adapterIlceler.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerIlceler.setAdapter(adapterIlceler);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = signupFullName.getText().toString().trim();// bu kısımda signup fragment içinde değerlerini doldurmuş olduğumuz editText içöindeki değerleri string değerlerinin içine atadık
                String age = signupAge.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                int status = 0; //Statüsü 0 olan kullanıcılar riskisz olacaktır. Sisteme kaydolan bütün kullanıcılar fix olarak risksiz olarak sisteme işlenecektir
                String gender = spinnerGender.getSelectedItem().toString();
                boolean isAdmin = false; // Oluşturulan her kullanıcı en başta normal kullanıcı olarak oluşturulacak
                String statusCode = createStatusCode();
                String livein = spinnerIlceler.getSelectedItem().toString();
                //boolean isPollDone = false;

                if(fullName.isEmpty()){//İSİM-SOY İSİM değerinin boş kalmamsı için gereken koşul
                    signupFullName.setError("Full name is required");
                    signupFullName.requestFocus();
                    return;
                }
                if(age.isEmpty()){//Yaş değerinin boş kalmaması için gereken koşul
                    signupAge.setError("Age is required");
                    signupAge.requestFocus();
                    return;
                }
                if(email.isEmpty()){//email değerinin boş kalmaması için gereken koşul
                    signupEmail.setError("Email is required");
                    signupEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){//Doğru bir email adresi girilmesi için gereken koşul
                    signupEmail.setError("Please provide valid email");
                    signupEmail.requestFocus();
                    return;
                }
                if(password.isEmpty()){//Şifrenin değerinin boş kalmaması için gereken koşul
                    signupPassword.setError("Password is required");
                    signupPassword.requestFocus();
                    return;
                }
                if(password.length() < 5){//Şifrenin 5 karakterden az olmaması için gereken koşul
                    signupPassword.setError("Password can't be less than 5 characters");
                    signupPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {// Girmiş olduğumuz mail ve password değerleri üzerinden databasede kullanıcıyı oluşturmayı sağlıyoruz
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){// Eğer işlem geçerli olursa gerçekleşmesi gereken durumlar
                            User user = new User(
                                    fullName,
                                    age,
                                    email,
                                    status,
                                    gender,
                                    isAdmin,
                                    statusCode,
                                    password,
                                    livein
                                    /*isPollDone*/);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {// Eğer işlemler tamamlandı yada tamamlanmadı bilgisini kullanıcıya döndürecek olan completeListenerı ekledik
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Registeration has been completed", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);

                                        signupFullName.setText("");
                                        signupAge.setText("");
                                        signupEmail.setText("");
                                        signupPassword.setText("");
                                    }

                                    else{
                                        Toast.makeText(getActivity(), "Failed to Register", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(getActivity(), "Failed to Register", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

            }
        });


        return root;


    }

    public String createStatusCode(){
        UUID uıd = UUID.randomUUID();
        String statusCode = uıd.toString();

        return statusCode;
    }

}
