package com.egesaylan.capstoneproject.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.egesaylan.capstoneproject.R;

public class IntroAdapter extends PagerAdapter {
    //Bir context ve layoutİnflater oluşturduk
    Context context;
    LayoutInflater layoutInflater;

    public IntroAdapter(Context context) {
        this.context = context;
    }
    //Intro screende gözükücek (ViewPager içerisinde gözükücek) resimleri ekledik
    int images[] = {
            R.drawable.medical,
            R.drawable.location,
            R.drawable.bluetooth,
            R.drawable.statistics
    };
    //Intro screende gözükücek (relative layout içerisinde gözükücek) başlıkları ekledik
    int headings[] = {
            R.string.intro_title,
            R.string.intro_title2,
            R.string.intro_title3,
            R.string.intro_title4

    };
    //Intro screende gözükücek (relative layout içerisinde gözükücek) yazıları ekledik
    int descriptions[] = {
            R.string.intro_description,
            R.string.intro_description2,
            R.string.intro_description3,
            R.string.intro_description4
    };

    @Override
    public int getCount() {//Sayısını yazı sayısı (başlık yada resim sayısıda olurdu farketmez) kadar belirledik
        return descriptions.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //Burada layout inflateri oluşturduğumuz slide_layout xml dosyası ile optimize ettik
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        //Bütün oluşturduğumuz bu resimleri, başlıkları, ve açıklamaları bu view pager içerisinde kullanıma hazır bir hale geitdik bunu slide_layout içerisine koymuş olduğumuz toolların id leri ile eşitleyerek yaptık
        ImageView imageView = view.findViewById(R.id.intro_image);
        TextView heading = view.findViewById(R.id.intro_heading);
        TextView description = view.findViewById(R.id.intro_descripton);
        //şimdik sırasıyla bu toolların içine değerleri atıyoruz yukarıda bulundukalrı listelerin pozisyon yani index konumlarına göre
        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        description.setText(descriptions[position]);
        //daha sonradan bu görünümün eklenmesi için gereken komutu veriyoruz
        container.addView(view);
        //ve bu görüntüyü kullanıcıya göstermesini sağlıyoruz
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
