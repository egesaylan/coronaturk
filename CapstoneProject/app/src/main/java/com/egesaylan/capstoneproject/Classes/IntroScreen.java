package com.egesaylan.capstoneproject.Classes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egesaylan.capstoneproject.R;
import com.egesaylan.capstoneproject.helpers.IntroAdapter;

public class IntroScreen extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout noktalarLayout; //LinearLayout içinde kullandığımız değer

    IntroAdapter introAdapter;
    TextView[] noktalar;

    AppCompatButton startButton;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro_screen);


        viewPager = findViewById(R.id.slider);
        noktalarLayout = findViewById(R.id.dots);
        startButton = findViewById(R.id.get_started_button);

        introAdapter = new IntroAdapter(IntroScreen.this);
        viewPager.setAdapter(introAdapter);

        NoktalarEkle(0);
        viewPager.addOnPageChangeListener(changeListener);

    }

    private void NoktalarEkle(int position) { //LinearLayout un altına ekleyecek olduğumuz noktalar

        noktalar = new TextView[4];
        noktalarLayout.removeAllViews();

        for (int i = 0; i < noktalar.length; i++) {
            noktalar[i] = new TextView(this);
            noktalar[i].setText(Html.fromHtml("&#8226"));
            noktalar[i].setTextSize(45);

            noktalarLayout.addView(noktalar[i]);
        }

        if(noktalar.length > 0 ){
            noktalar[position].setTextColor(getResources().getColor(R.color.renk2));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            NoktalarEkle(position);

            if(position == 0){
                startButton.setVisibility(View.INVISIBLE);
            }
            else if(position == 1){
                startButton.setVisibility(View.INVISIBLE);
            }
            else if(position == 2){
                startButton.setVisibility(View.INVISIBLE);
            }
            else {
                animation = AnimationUtils.loadAnimation(IntroScreen.this,R.anim.taban);
                startButton.setAnimation(animation);
                startButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void start(View view){
        Intent intent = new Intent(this,LoginScreen.class);
        startActivity(intent);
        finish();
    }
}