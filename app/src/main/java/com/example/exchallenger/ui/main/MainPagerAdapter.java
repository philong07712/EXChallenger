package com.example.exchallenger.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.exchallenger.ui.profile.EmptyFragment;
import com.example.exchallenger.ui.workout.WorkoutFragment;
import com.example.exchallenger.ui.profile.GuestFragment;
import com.example.exchallenger.ui.profile.ProfileFragment;
import com.example.exchallenger.ui.work_from_home.WorkFromHomeFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private boolean login;

    public MainPagerAdapter(@NonNull FragmentManager fm, boolean login) {
        super(fm, BEHAVIOR_SET_USER_VISIBLE_HINT);
        this.login = login;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WorkoutFragment();
            case 1:
                return new WorkFromHomeFragment();
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
        notifyDataSetChanged();
    }

    public boolean isLogin() {
        return login;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
