package com.example.exchallenger.challenge;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.Helpers.GroupHelper;
import com.example.exchallenger.Helpers.WorkoutHelper;
import com.example.exchallenger.Models.ChallengeItem;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class GroupChallengeListFragment extends BaseFragment {

    private static final String K_GROUP = "group";
    @BindView(R.id.rcv_challenge)
    RecyclerView rcvChallenge;
    private ChallengeAdapter challengeAdapter;

    private String groupId;

    public static GroupChallengeListFragment newInstance(String groupId) {
        GroupChallengeListFragment fragment = new GroupChallengeListFragment();
        Bundle args = new Bundle();
        args.putString(K_GROUP, groupId);
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

        groupId = getArguments().getString(K_GROUP);

        MyApplication.getInstance().getGroupHelper().getGroupChallenges(groupId, new GroupHelper.GetGroupChallengeListener() {
            @Override
            public void onRead(List<ChallengeItem> challengeItems) {
                challengeAdapter.set(challengeItems);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
