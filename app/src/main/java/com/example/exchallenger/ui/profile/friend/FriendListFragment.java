package com.example.exchallenger.profile.friend;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.R;

import java.util.Arrays;

import butterknife.BindView;

import com.example.exchallenger.base.BaseFragment;

public class FriendListFragment extends BaseFragment {
    @BindView(R.id.rcv_friend)
    RecyclerView rcvFriend;
    private FriendAdapter friendAdapter;

    public static FriendListFragment newInstance() {
        return new FriendListFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_list;
    }

    @Override
    protected void setUp() {
        friendAdapter = new FriendAdapter(getContext());
        rcvFriend.setAdapter(friendAdapter);
        rcvFriend.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcvFriend.setNestedScrollingEnabled(false);
        friendAdapter.set(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
    }
}
