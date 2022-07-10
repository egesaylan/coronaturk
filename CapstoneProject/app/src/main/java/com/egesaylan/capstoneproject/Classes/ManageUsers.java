package com.egesaylan.capstoneproject.Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.egesaylan.capstoneproject.Models.User;
import com.egesaylan.capstoneproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageUsers extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    AppCompatButton changeButton;
    TextView backToMain;
    EditText userStatusManager, userStatusUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        changeButton = findViewById(R.id.changeStatusButton);
        backToMain = findViewById(R.id.backToMainActivity);

        userStatusManager = findViewById(R.id.userStatusCodeManager);
        userStatusUpdater = findViewById(R.id.userStatusUpdaterManager);

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageUsers.this,MainActivity.class);
                startActivity(intent);
            }
        });




        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = userStatusManager.getText().toString().trim();
                String newStatus = userStatusUpdater.getText().toString().trim();

                int newStatusInteger = Integer.parseInt(newStatus);

                reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            User userInfos = ds.getValue(User.class);
                            String UID = (String)ds.getKey();
                            if(userInfos.statusCode.equals(code)){
                               FirebaseDatabase.getInstance().getReference("Users")
                                       .child(UID)
                                       .child("status").setValue(newStatusInteger).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(ManageUsers.this, "Status Updated Successfully", Toast.LENGTH_SHORT).show();

                                           userStatusManager.setText("");
                                           userStatusUpdater.setText("");
                                       }else{
                                           Toast.makeText(ManageUsers.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}