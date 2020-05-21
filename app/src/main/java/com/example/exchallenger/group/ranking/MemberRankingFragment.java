package com.example.exchallenger.group.ranking;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.Helpers.GroupHelper;
import com.example.exchallenger.Models.GroupMember;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class MemberRankingFragment extends BaseFragment {
    private static final String K_GROUP = "group";

    @BindView(R.id.rcv_member)
    RecyclerView rcvMember;

    private MemberRankingAdapter memberRankingAdapter;

    public static MemberRankingFragment newInstance(String groupId) {
        MemberRankingFragment fragment = new MemberRankingFragment();
        Bundle args = new Bundle();
        args.putString(K_GROUP, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member_ranking;
    }

    @Override
    protected void setUp() {
        memberRankingAdapter = new MemberRankingAdapter(getContext());
        rcvMember.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcvMember.setAdapter(memberRankingAdapter);

        String groupId = getArguments().getString(K_GROUP);
        MyApplication.getInstance().getGroupHelper().getGroupRanking(groupId, new GroupHelper.GetGroupRankingListener() {
            @Override
            public void onRead(List<GroupMember> groups) {
                memberRankingAdapter.set(groups);
            }

            @Override
            public void onError(String error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
