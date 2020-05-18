package com.example.exchallenger.group.ranking;

import androidx.fragment.app.Fragment;

import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;

public class MemberRankingFragment extends BaseFragment {
    public static MemberRankingFragment newInstance() {
        return new MemberRankingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member_ranking;
    }

    @Override
    protected void setUp() {

    }
}
