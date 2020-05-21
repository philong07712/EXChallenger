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
        groupHelper.getGroups(MyApplication.user.getUserID(), new GroupHelper.GetGroupsListener() {
            @Override
            public void onRead(List<Map<String, Object>> groups) {
                for (Map<String, Object> group : groups) {
                    Log.d("GroupsList", group.toString());
                }
            }

            @Override
            public void onReadGroups(List<String> groupIDs) {
                compositeDisposable.add(Observable.just(groupIDs)
                        .flatMapIterable(ids -> ids)
                        .flatMap(groupId -> groupHelper.getGroupFromGroupId(groupId))
                        .subscribe(new Consumer<Group>() {
                            @Override
                            public void accept(Group group) throws Exception {
                                groupAdapter.addItem(group);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }));

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

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
