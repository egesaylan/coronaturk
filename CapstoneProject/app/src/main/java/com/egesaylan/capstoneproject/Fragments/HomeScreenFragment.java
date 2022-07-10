package com.egesaylan.capstoneproject.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.egesaylan.capstoneproject.Classes.DailyCases;
import com.egesaylan.capstoneproject.Classes.IntroScreen;
import com.egesaylan.capstoneproject.Classes.MyRelatives;
import com.egesaylan.capstoneproject.Classes.RiskyPeopleAround;
import com.egesaylan.capstoneproject.Models.User;
import com.egesaylan.capstoneproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class HomeScreenFragment extends Fragment {

    private CardView dailyCases,riskAround,myRelatives;
    private TextView greetings;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private Animation animation;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.homescreen_fragment, container, false);

        greetings = root.findViewById(R.id.greetingsTV);
        dailyCases = root.findViewById(R.id.dailyCasesCV);
        riskAround = root.findViewById(R.id.riskAroundCV);
        myRelatives = root.findViewById(R.id.myRelativesCV);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        //Türkiye vaka istatistiklerini gösteren sayfaya yçnlendirme yapıyoruz alttaki kod bloğu ile
        dailyCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DailyCases.class));
            }
        });

        riskAround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RiskyPeopleAround.class);
                startActivity(intent);
            }
        });

        myRelatives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyRelatives.class));
            }
        });

        //Kullanıcı uygulamaya giriş yapıp homescreen kısmına geldiğinde selamlayan fonksyonu yazdık
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userFullName = snapshot.getValue(User.class);
                Calendar calendar = Calendar.getInstance();
                String fullname = userFullName.fullName;
                if (userFullName != null) {
                    if(calendar.get(Calendar.HOUR_OF_DAY) < 12 && calendar.get(Calendar.HOUR_OF_DAY) >4){

                        greetings.setText("Good Morning\n"+ fullname);
                        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.taban);
                        greetings.setAnimation(animation);
                        greetings.setVisibility(View.VISIBLE);
                    }
                    else if(calendar.get(Calendar.HOUR_OF_DAY) > 12 && calendar.get(Calendar.HOUR_OF_DAY)< 20){
                        greetings.setText("Good Afternoon\n"+ fullname);
                        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.taban);
                        greetings.setAnimation(animation);
                        greetings.setVisibility(View.VISIBLE);
                    }
                    else{
                        greetings.setText("Good Night\n"+ fullname);
                        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.taban);
                        greetings.setAnimation(animation);
                        greetings.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something Went Wrong while Greeting User", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
