package com.egesaylan.capstoneproject.Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;


import com.egesaylan.capstoneproject.Models.User;
import com.egesaylan.capstoneproject.R;
import com.egesaylan.capstoneproject.helpers.MainFragmentsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    ViewPager main_viewPager;
    TabLayout main_tabLayout;
    Toolbar menuToolBar;

    float v = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_viewPager = findViewById(R.id.mainViewPager);
        main_tabLayout = findViewById(R.id.mainTabLayout);
        menuToolBar = findViewById(R.id.menuToolbar);

        main_tabLayout.addTab(main_tabLayout.newTab().setText("Home"));
        main_tabLayout.addTab(main_tabLayout.newTab().setText("Profile"));
        main_tabLayout.addTab(main_tabLayout.newTab().setText("Query Code"));
        main_tabLayout.addTab(main_tabLayout.newTab().setText("Map"));
        main_tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        main_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MainFragmentsAdapter main_adapter = new MainFragmentsAdapter(getSupportFragmentManager(), this, main_tabLayout.getTabCount());
        main_viewPager.setAdapter(main_adapter);

        main_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(main_tabLayout));
        main_tabLayout.setTranslationY(300);//A????a????daki 2 sat??r ve bu sat??r tablayoutun ekrana gelirkenki animasyon de??erlerini temsil ediyor
        main_tabLayout.setAlpha(v);
        main_tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        main_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {//Tablayoutta de??erlere t??kland??????ndada sayfay?? de??i??tirmeyi sa??layan kod dizisi (sadece kayd??rarak ??al????mamas??n?? sa??l??yor)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                main_viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //Admin paneli ile ilgili olan k??sm??n kod blo??u alt taraftan itibaren ba??l??yor
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null && userProfile.isAdmin) {
                    menuToolBar.inflateMenu(R.menu.admin_panel);
                    menuToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.manageUsers) {
                                startActivity(new Intent(MainActivity.this, ManageUsers.class));

                            }
                            return false;
                        }
                    });
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //E??er kullan??c?? kasta ise databasede hasta kullan??c??lar listesine ekleme yap
        changeRegionStatus();
        //E??er kullan??c?? sa??l??????na d??nm????se sickusers tablosundan kald??r
        updateRegionAndSickUserTable();

    }

    //Kullan??c?? hasta ise ve hasta kullan??c??lar listesinde yoksa hem oraya eklememize yarayan hemde  hastan??n ya??ad?????? il??edeki hasta say??s??na ekleme yapan fonksyon
    public void changeRegionStatus() {
        String ID = user.getUid();//Kullan??c??m??z??n id sini yani key value de??erini al??yoruz
        ArrayList<String> idArray = new ArrayList<>();

        //Users tablosu alt??nda arama yapaca????z
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User userInfos = ds.getValue(User.class);
                    //kullan??c??m??z??n id'sini databasede ar??yoruz ve bulursak alttaki ko??ula gidiyoruz
                    if (ds.getKey().equals(ID)) {
                        //E??er kullan??c??m??z hasta ise alttaki ko??ul sa??lan??yorsa alttaki kod ??al??????yor
                        if (userInfos.status == 1) {

                            //Hasta kullan??c??lar tablosunda bu kez kullan??c??m??z??n id si var m?? yok mu ona bak??yoruz
                            reference = FirebaseDatabase.getInstance().getReference("Sickusers");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds2 : snapshot.getChildren()) {
                                        String a = (String) ds2.getKey();
                                        idArray.add(a);
                                    }
                                    //E??er kullan??c??m??z yok ise kullan??c??m??z?? bu tabloya ekliyoruz
                                    if (!idArray.contains(ID) /*!ds2.getKey().equals(ID)*/) {
                                        FirebaseDatabase.getInstance().getReference("Sickusers")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userInfos.livein);

                                        //??ehirlerin de??erini artt??ran algoritma
                                        reference = FirebaseDatabase.getInstance().getReference("Regions");
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot ds3 : snapshot.getChildren()) {
                                                    if (ds3.getKey().equals(userInfos.livein)) {
                                                        String a = String.valueOf(ds3.child("sickness").getValue());
                                                        int b = Integer.parseInt(a);
                                                        b = b + 1;
                                                        String c = Integer.toString(b);
                                                        reference = FirebaseDatabase.getInstance().getReference("Regions").child(userInfos.livein).child("sickness");
                                                        reference.setValue(c);
                                                        break;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //E??er kullan??c?? sa??l??????na kavu??mu??sa, hastay?? hasta kullan??c??lar listesinden silme  ve hastan??n oldu??u il??ede hasta say??s??nda eksilme yap??cak olan fonksyon

    public void updateRegionAndSickUserTable() {

        String ID = user.getUid();
        ArrayList<String> idArray2 = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User userInfos2 = ds.getValue(User.class);

                    if (ds.getKey().equals(ID)) {

                        if (userInfos2.status == 0) {

                            reference = FirebaseDatabase.getInstance().getReference("Sickusers");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds2 : snapshot.getChildren()) {
                                        String a = (String) ds2.getKey();
                                        idArray2.add(a);
                                    }
                                    if (idArray2.contains(ID)) {
                                        reference = FirebaseDatabase.getInstance().getReference("Sickusers");
                                        reference.child(ID).removeValue();

                                        reference = FirebaseDatabase.getInstance().getReference("Regions");
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot ds3 : snapshot.getChildren()){
                                                    if(ds3.getKey().equals(userInfos2.livein)){
                                                        String a = String.valueOf(ds3.child("sickness").getValue());
                                                        int b = Integer.parseInt(a);
                                                        b = b - 1;
                                                        String c = Integer.toString(b);
                                                        reference = FirebaseDatabase.getInstance().getReference("Regions").child(userInfos2.livein).child("sickness");
                                                        reference.setValue(c);
                                                        break;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
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