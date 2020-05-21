package com.example.exchallenger.group;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.exchallenger.challenge.GroupChallengeListFragment;
import com.example.exchallenger.group.ranking.MemberRankingFragment;
import com.example.exchallenger.profile.friend.FriendListFragment;

public class GroupDetailPagerAdapter extends FragmentPagerAdapter {
    String groupId;
    public GroupDetailPagerAdapter(@NonNull FragmentManager fm, String groupId) {
        super(fm);
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return GroupChallengeListFragment.newInstance(groupId);
        } else {
            return MemberRankingFragment.newInstance(groupId);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
