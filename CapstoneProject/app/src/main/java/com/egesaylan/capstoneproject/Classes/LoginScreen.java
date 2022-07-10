package com.egesaylan.capstoneproject.Classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.egesaylan.capstoneproject.R;
import com.egesaylan.capstoneproject.helpers.LoginAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {

    public FirebaseAuth mAuth;

    TabLayout tabLayout;
    ViewPager viewPager;
    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();

        LinearLayout linearLayout = findViewById(R.id.animatedLayout);// XML de oluşturduğumuz linearlayoutun id sini eşleştirdik

        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();// Bu kısımda oluşturduğumuz animasyon ile linearlayoutu eşleştirdik ve fade giriş çıkış sürelerini belirleyip start verdirdik.
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        tabLayout = findViewById(R.id.tabLayout);//Tablayoutu id'si ile eşleştirdik
        viewPager = findViewById(R.id.viewPager);//viewpageri id'si ile eşleştirdik
        tabLayout.addTab(tabLayout.newTab().setText("Logın"));//tablayoutların tablerine başlık verdik
        tabLayout.addTab(tabLayout.newTab().setText("SıgnUp"));//tablayoutların tablerine başlık verdik
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//tablayout gravity fill değerini verdik

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTranslationY(300);//Aşşağıdaki 2 satır ve bu satır tablayoutun ekrana gelirkenki animasyon değerlerini temsil ediyor
        tabLayout.setAlpha(v);
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {//Tablayoutta değerlere tıklandığındada sayfayı değiştirmeyi sağlayan kod dizisi (sadece kaydırarak çalışmamasını sağlıyor)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


}
