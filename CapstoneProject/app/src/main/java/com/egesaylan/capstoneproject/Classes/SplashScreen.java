package com.egesaylan.capstoneproject.Classes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.egesaylan.capstoneproject.R;

public class SplashScreen extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    TextView textView;
    LinearLayout linearLayout, linearLayout2;

    SharedPreferences introScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Bu kısımda yukarıda tanımlamış olduğumuz değerleri id leriyle eşleştirdik
        lottieAnimationView = findViewById(R.id.lottie);
        linearLayout2 = findViewById(R.id.linear_splash_reversed);
        textView = findViewById(R.id.textView_splash);
        linearLayout = findViewById(R.id.linear_splash);

        lottieAnimationView.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);//Bu kısım ekrandaki yazıların ve layoutların animasyon süreleri ve doğrultularını temsil ediyor
        textView.animate().translationY(1600).setDuration(1000).setStartDelay(4000);
        linearLayout.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        linearLayout2.animate().translationY(1600).setDuration(1000).setStartDelay(4000);


        /*Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashScreen.this, IntroScreen.class);
                startActivity(intent);
                finish();
            }

        }, 5300);// Geliştirme aşamasında intro screeni görmek için bu kod duruyor sonradan silinecek ve alttaki kod bloğu kullanıma geçirilicek*/



        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {//Bu kısımda eğer kullanıcı bir kez tanıtım ekranını görmüşse bir daha görmesin, vakit kaybetmesin diye yazdığımız kod
                introScreen = getSharedPreferences("IntroScreen", MODE_PRIVATE);
                boolean isFirst = introScreen.getBoolean("firstTime", true);

                if (isFirst) {

                    SharedPreferences.Editor editor = introScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();

                    Intent intent = new Intent(SplashScreen.this, IntroScreen.class);
                    startActivity(intent);
                    finish();


                } else {
                    Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 5300);
    }

}