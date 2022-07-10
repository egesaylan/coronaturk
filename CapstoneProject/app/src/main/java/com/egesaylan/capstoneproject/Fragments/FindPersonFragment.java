package com.egesaylan.capstoneproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.egesaylan.capstoneproject.Models.User;
import com.egesaylan.capstoneproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindPersonFragment extends Fragment {

    private EditText investiagationET;
    private TextView investigationName, investigationStatus;
    private AppCompatButton investigationBT, investigationClearBT;
    private DatabaseReference investigationReference;
    private FirebaseUser investigationUser;
    private String code;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.findperson_fragment, container, false);

        investiagationET = root.findViewById(R.id.investigationET);
        investigationBT = root.findViewById(R.id.investigationBT);
        investigationName = root.findViewById(R.id.investigationFullNameTXT);
        investigationStatus = root.findViewById(R.id.investigationStatusTXT);
        investigationClearBT = root.findViewById(R.id.investigationClearBT);






        investigationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getUser();

            }
        });

        investigationClearBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                investigationStatus.setText("");
                investigationName.setText("");
                investiagationET.setText("");
            }
        });

        return root;
    }

    private void getUser(){
        //investigationUser = FirebaseAuth.getInstance().getCurrentUser();

        investigationReference = FirebaseDatabase.getInstance().getReference("Users");

        investigationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    code = investiagationET.getText().toString().trim();

                    if(user.statusCode.equals(code)){
                        String name = user.fullName;
                        int status = user.status;
                        investigationName.setText("Full Name: "+name);

                        if (status == 0) {
                            investigationStatus.setText("Status: Non-Risky");
                            investigationStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.riskColor));
                        } else {
                            investigationStatus.setText("Status: Risky");
                            investigationStatus.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
