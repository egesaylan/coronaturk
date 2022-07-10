package com.egesaylan.capstoneproject.Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.egesaylan.capstoneproject.Models.User;
import com.egesaylan.capstoneproject.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRelatives extends AppCompatActivity {

    private DatabaseReference reference;
    FirebaseUser user;
    private String userID;

    ListView myRelativesList;
    ArrayList<String> relativesArray;
    ArrayAdapter<String> arrayAdapter;

    //User eklerken relatives altında kullanıcı var mı yok mu için sorgulayacağımız arraylist
    ArrayList<String> isExistArray;


    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_relatives);

        myRelativesList = findViewById(R.id.myRelativesList);
        relativesArray = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, relativesArray);
        myRelativesList.setAdapter(arrayAdapter);
        fab = findViewById(R.id.floatinButton);

        isExistArray = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = new Dialog(MyRelatives.this);
                d.setContentView(R.layout.dia_add);
                EditText etCode = d.findViewById(R.id.etCode);
                EditText etName = d.findViewById(R.id.etName);
                AppCompatButton btnAdd = d.findViewById(R.id.btnAdd);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code = etCode.getText().toString().trim();
                        String name = etName.getText().toString().trim();
                        etCode.setText("");
                        etName.setText("");
                        reference = FirebaseDatabase.getInstance().getReference("Relatives").child(userID);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ss: snapshot.getChildren()){
                                    String relativeCodes = ss.getKey();
                                    isExistArray.add(relativeCodes);
                                    if(!isExistArray.contains(code)){
                                        reference = FirebaseDatabase.getInstance().getReference("Relatives").child(userID);
                                        reference.child(code).setValue(name);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                d.show();
            }
        });

        myRelativesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Dialog d2 = new Dialog(MyRelatives.this);
                d2.setContentView(R.layout.relative_status);
                d2.setTitle(relativesArray.get(i)+"'s status");
                TextView relativeName = d2.findViewById(R.id.textRelativeName);
                TextView relativeStatus = d2.findViewById(R.id.textRelativeStatus);

                reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ss : snapshot.getChildren()){
                            User user = ss.getValue(User.class);

                            if(user.statusCode.equals(relativesArray.get(i))){
                                String dialogUserName = String.valueOf(user.fullName);
                                relativeName.setText(dialogUserName);
                                if(user.status == 1){
                                    relativeStatus.setText("Risky");
                                    relativeStatus.setTextColor(ContextCompat.getColor(MyRelatives.this, R.color.main_theme2_yedek2));
                                }else{
                                    relativeStatus.setText("Non-Risky");
                                    relativeStatus.setTextColor(ContextCompat.getColor(MyRelatives.this, R.color.riskColor));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                d2.show();
            }
        });


        createMeOnRelatives();
        getRelative();

    }


    public void getRelative(){

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Relatives").child(userID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ss : snapshot.getChildren()){

                    if(ss.getKey().equals("relative")){
                        break;
                    }else {
                        String name = (String) ss.getKey(); //getValue() yaparsak listede kullanıcının ismi gözükür fakat üstüne tıklandığında gelen pencerede statüs kod ve risk durumu yazmalı onun için yukarıdaki listitemcliclistener algoritması baştan yazılmalı...
                        relativesArray.add(name);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void createMeOnRelatives() {
        ArrayList<String> idArray = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Relatives");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    String a = (String) ss.getKey();
                    idArray.add(a);

                    if (!idArray.contains(userID)) {
                        FirebaseDatabase.getInstance().getReference("Relatives")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("relative")
                                .setValue("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}