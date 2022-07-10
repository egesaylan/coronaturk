package com.egesaylan.capstoneproject.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.egesaylan.capstoneproject.Classes.LoginScreen;
import com.egesaylan.capstoneproject.Classes.MainActivity;
import com.egesaylan.capstoneproject.R;
import com.egesaylan.capstoneproject.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    private String userID;

    TextView fullnameTV, ageTV, statusTV, genderTV, emailTV, statusCodeTV;
    //Switch notifications;
    AppCompatButton deleteprofileBTN, logOutBTN;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container, false);


        //XML dosyasında oluşturmuş olduğumuz viewları burada bir yukarıda oluşturduğumuz değerlere atayarak initialize ettik
        fullnameTV = root.findViewById(R.id.profileFullNameTV);
        ageTV = root.findViewById(R.id.profileAgeTV);
        statusTV = root.findViewById(R.id.profileRiskTV);
        genderTV = root.findViewById(R.id.profile_genderTV);
        emailTV = root.findViewById(R.id.profileEmailTV);
        statusCodeTV = root.findViewById(R.id.profileStatusCodeTV);

        //notifications = root.findViewById(R.id.notificationsSwitch);

        deleteprofileBTN = root.findViewById(R.id.deleteAccount);
        logOutBTN = root.findViewById(R.id.logOutBTN);

        //Database işlemlerini gerçekleştirebilmek için firebase değişkenlerini initialize ettik
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        mAuth = FirebaseAuth.getInstance();

        //Kullanıcının profil bilgilerini databaseden çektiğimiz işlemler
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullname = userProfile.fullName;
                    String age = userProfile.age;
                    int status = userProfile.status;
                    String gender = userProfile.gender;
                    String email = userProfile.email;
                    String statusCode = userProfile.statusCode;

                    fullnameTV.setText("Full Name: " + fullname);
                    ageTV.setText("Age: " + age);
                    emailTV.setText("Email: " + email);
                    statusCodeTV.setText("Status Code: " + statusCode);


                    if (status == 0) {
                        statusTV.setText("Status: Non-Risky");
                    } else {
                        statusTV.setText("Status: Risky");
                        statusTV.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
                    }

                    if (gender == null) {
                        genderTV.setText("Gender: Not identified");
                    } else {
                        genderTV.setText("Gender: " + gender);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        //Kullanıcının sistemden çıkması için yaptığımız işlemler
        logOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();

                getActivity().finish();
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginScreen.class));
                //MainActivity.mediaPlayer.stop();

            }
        });

        //Kullanıcının profilini silmesi için yaptığımız işlemler
        deleteprofileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you won't be able to access the app");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ID = FirebaseAuth.getInstance().getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(ID).removeValue();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                    SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("remember", "false");
                                    editor.apply();
                                    startActivity(new Intent(getActivity(), LoginScreen.class));

                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        return root;
    }

}
