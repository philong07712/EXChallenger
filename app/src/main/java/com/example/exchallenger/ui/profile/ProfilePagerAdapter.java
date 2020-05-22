package com.example.exchallenger.ui.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.exchallenger.ui.profile.friend.FriendListFragment;
import com.example.exchallenger.ui.profile.group.GroupListFragment;

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    public ProfilePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return GroupListFragment.newInstance();
        } else {
            return FriendListFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
