package com.example.exchallenger.profile.group;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.Helpers.GroupHelper;
import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Models.Group;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.base.OnItemClickListener;
import com.example.exchallenger.group.GroupDetailFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GroupListFragment extends BaseFragment {
    @BindView(R.id.rcv_group)
    RecyclerView rcvGroup;
    private GroupAdapter groupAdapter;
    private CompositeDisposable compositeDisposable;

    public static GroupListFragment newInstance() {
        return new GroupListFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_list;
    }

    @Override
    public void onStart() {
        super.onStart();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    protected void setUp() {
        groupAdapter = new GroupAdapter(getContext());
        rcvGroup.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcvGroup.setAdapter(groupAdapter);
        rcvGroup.setNestedScrollingEnabled(false);
        GroupHelper groupHelper = MyApplication.getInstance().getGroupHelper();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        groupHelper.getGroups(firebaseUser.getUid(), new GroupHelper.GetGroupsListener() {
            @Override
            public void onRead(List<Group> groups) {
                groupAdapter.set(groups);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        groupAdapter.setOnItemClickListener(new OnItemClickListener<Group>() {
            @Override
            public void onItemClick(int position, Group dataItem) {
                showGroupDetailFragment(dataItem);
            }
        });
    }

    private void showGroupDetailFragment(Group dataItem) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, GroupDetailFragment.newInstance(dataItem))
                .addToBackStack(null)
                .commit();
    }
}
