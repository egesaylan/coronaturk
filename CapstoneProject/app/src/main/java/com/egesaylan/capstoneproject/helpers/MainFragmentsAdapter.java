package com.egesaylan.capstoneproject.helpers;

import android.content.Context;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.egesaylan.capstoneproject.Fragments.FindPersonFragment;
import com.egesaylan.capstoneproject.Fragments.HomeScreenFragment;
import com.egesaylan.capstoneproject.Fragments.MapFragment;
import com.egesaylan.capstoneproject.Fragments.ProfileFragment;


public class MainFragmentsAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    @Override
    public int getCount() {
        return totalTabs;
    }

    public MainFragmentsAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
                return homeScreenFragment;

            case 1:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

            case 2:
                FindPersonFragment findPersonFragment = new FindPersonFragment();
                return findPersonFragment;

            case 3:
                MapFragment mapFragment = new MapFragment();
                return mapFragment;

            default:
                return null;
        }
    }
}
