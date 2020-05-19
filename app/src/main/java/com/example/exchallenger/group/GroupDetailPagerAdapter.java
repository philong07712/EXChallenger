package com.example.exchallenger.group;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.exchallenger.challenge.GroupChallengeListFragment;
import com.example.exchallenger.group.ranking.MemberRankingFragment;
import com.example.exchallenger.profile.friend.FriendListFragment;

public class GroupDetailPagerAdapter extends FragmentPagerAdapter {
    public GroupDetailPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return GroupChallengeListFragment.newInstance();
        } else {
            return MemberRankingFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
