package com.example.exchallenger.ui.group;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.exchallenger.ui.challenge.GroupChallengeListFragment;
import com.example.exchallenger.ui.group.ranking.MemberRankingFragment;

public class GroupDetailPagerAdapter extends FragmentPagerAdapter {
    String groupId;
    boolean isAdmin;
    public GroupDetailPagerAdapter(@NonNull FragmentManager fm, String groupId, boolean isAdmin) {
        super(fm);
        this.groupId = groupId;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return GroupChallengeListFragment.newInstance(groupId, isAdmin);
        } else {
            return MemberRankingFragment.newInstance(groupId);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
