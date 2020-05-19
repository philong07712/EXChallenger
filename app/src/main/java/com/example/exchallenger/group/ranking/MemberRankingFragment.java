package com.example.exchallenger.group.ranking;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;

import java.util.Arrays;

import butterknife.BindView;

public class MemberRankingFragment extends BaseFragment {
    @BindView(R.id.rcv_member)
    RecyclerView rcvMember;

    private MemberRankingAdapter memberRankingAdapter;

    public static MemberRankingFragment newInstance() {
        return new MemberRankingFragment();
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
        memberRankingAdapter.set(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
    }
}
