package com.example.exchallenger.challenge;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        MyApplication.getInstance().workoutHelper.getChallenges("UserID1", new WorkoutHelper.getWorkoutListener() {
            @Override
            public void onRead(List<Map<String, Object>> list) {
                List<ChallengeItem> challengeItems = new ArrayList<>();
                for(Map map: list){
                    challengeItems.add(AppUtils.convertMapToChallengeItem(map));
                }
                challengeAdapter.set(challengeItems);
            }

            @Override
            public void onCancel() {

            }
        });

    }
}
