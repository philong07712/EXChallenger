package com.example.exchallenger.challenge;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;

import java.util.Arrays;

import butterknife.BindView;

public class GroupChallengeListFragment extends BaseFragment {

    @BindView(R.id.rcv_challenge)
    RecyclerView rcvChallenge;
    private ChallengeAdapter challengeAdapter;

    public static GroupChallengeListFragment newInstance() {
        GroupChallengeListFragment fragment = new GroupChallengeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_challenge_list;
    }

    @Override
    protected void setUp() {
        challengeAdapter = new ChallengeAdapter(getContext());
        rcvChallenge.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcvChallenge.setAdapter(challengeAdapter);

        challengeAdapter.set(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
    }
}
