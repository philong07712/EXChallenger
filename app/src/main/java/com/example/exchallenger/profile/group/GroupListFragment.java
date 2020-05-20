package com.example.exchallenger.profile.group;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.R;

import java.util.Arrays;

import butterknife.BindView;

import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.base.OnItemClickListener;
import com.example.exchallenger.group.GroupDetailFragment;

public class GroupListFragment extends BaseFragment {
    @BindView(R.id.rcv_group)
    RecyclerView rcvGroup;
    private GroupAdapter groupAdapter;

    public static GroupListFragment newInstance() {
        return new GroupListFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_list;
    }

    @Override
    protected void setUp() {
        groupAdapter = new GroupAdapter(getContext());
        rcvGroup.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcvGroup.setAdapter(groupAdapter);
        rcvGroup.setNestedScrollingEnabled(false);
        groupAdapter.set(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        groupAdapter.setOnItemClickListener(new OnItemClickListener<Object>() {
            @Override
            public void onItemClick(int position, Object dataItem) {
                showGroupDetailFragment();
            }
        });
    }

    private void showGroupDetailFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, GroupDetailFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}