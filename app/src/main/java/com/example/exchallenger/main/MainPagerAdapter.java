package com.example.exchallenger.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.exchallenger.EmptyFragment;
import com.example.exchallenger.GuestFragment;
import com.example.exchallenger.profile.ProfileFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private boolean login;

    public MainPagerAdapter(@NonNull FragmentManager fm, boolean login) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.login = login;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 2:
                if (login) {
                    return ProfileFragment.newInstance();
                } else {
                    return GuestFragment.newInstance();
                }
            default:
                return EmptyFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isLogin() {
        return login;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
