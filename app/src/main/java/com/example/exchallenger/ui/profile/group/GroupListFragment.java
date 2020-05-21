package com.example.exchallenger.ui.profile.group;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.helpers.GroupHelper;
import com.example.exchallenger.models.Group;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.base.OnItemClickListener;
import com.example.exchallenger.ui.group.GroupDetailFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utilityview.customview.CustomTextviewFonts;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public class GroupListFragment extends BaseFragment {
    @BindView(R.id.rcv_group)
    RecyclerView rcvGroup;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    CustomTextviewFonts tvEmpty;
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
        if (firebaseUser != null) {
            groupHelper.getGroups(firebaseUser.getUid(), new GroupHelper.GetGroupsListener() {
                @Override
                public void onRead(List<Group> groups) {
                    groupAdapter.set(groups);
                    if (groups.size() == 0) {
                        ivEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        ivEmpty.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
        }

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
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(android.R.id.content, GroupDetailFragment.newInstance(dataItem))
                .addToBackStack(null)
                .commit();
    }
}
