package com.egesaylan.capstoneproject.helpers;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.egesaylan.capstoneproject.Fragments.LoginFragment;
import com.egesaylan.capstoneproject.Fragments.SignupFragment;

public class LoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    @Override
    public int getCount() {
        return totalTabs;
    }

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    public Fragment getItem (int position){

        switch (position){
            case 0:
                LoginFragment loginFragment = new LoginFragment();
                return loginFragment;

            case 1:
                SignupFragment signupFragment = new SignupFragment();
                return signupFragment;

            default:
                return null;
        }
    }
}
