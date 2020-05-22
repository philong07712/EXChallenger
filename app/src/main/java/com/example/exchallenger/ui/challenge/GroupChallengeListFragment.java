package com.example.exchallenger.ui.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.ui.exercise.ExerciseActivity;
import com.example.exchallenger.helpers.GroupHelper;
import com.example.exchallenger.helpers.LocalSaveHelper;
import com.example.exchallenger.models.ChallengeItem;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.base.OnItemClickListener;
import com.example.exchallenger.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GroupChallengeListFragment extends BaseFragment {

    private static final String K_GROUP = "group";
    private static final String K_ADMIN = "admin";
    @BindView(R.id.rcv_challenge)
    RecyclerView rcvChallenge;
    @BindView(R.id.text3)
    TextView tvAdmin;
    private ChallengeAdapter challengeAdapter;

    private String groupId;
    private boolean isAdmin;

    public static GroupChallengeListFragment newInstance(String groupId, boolean isAdmin) {
        GroupChallengeListFragment fragment = new GroupChallengeListFragment();
        Bundle args = new Bundle();
        args.putString(K_GROUP, groupId);
        args.putBoolean(K_ADMIN, isAdmin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_challenge_list;
    }

    @Override
    protected void setUp() {
        groupId = getArguments().getString(K_GROUP);
        isAdmin = getArguments().getBoolean(K_ADMIN);
        tvAdmin.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        challengeAdapter = new ChallengeAdapter(getContext(), isAdmin);
        rcvChallenge.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcvChallenge.setAdapter(challengeAdapter);


        challengeAdapter.setOnItemClickListener(new OnItemClickListener<ChallengeItem>() {
            @Override
            public void onItemClick(int position, ChallengeItem dataItem) {
                if (dataItem.isDone()) {
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
                intent.putExtra(ExerciseActivity.K_GROUP, groupId);
                intent.putExtra(ExerciseActivity.K_WORKOUT, dataItem.getId());
                List<Map<String, Object>> exerciseList = new ArrayList<>();
                exerciseList.add(AppUtils.getMapFromChallenge(dataItem));
                new LocalSaveHelper(getApplicationContext()).saveListMap("exercises", exerciseList);
                startActivity(intent);
            }
        });

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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 0) {//edit
            showCreateChallengeDialog(challengeAdapter.getDataItem(item.getGroupId()), item.getGroupId());
            return true;
        }

        if (item.getItemId() == 1) {//delete
            MyApplication.getInstance().getGroupHelper().deleteChallenge(groupId, challengeAdapter.getDataItem(item.getGroupId()));
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void showCreateChallengeDialog(ChallengeItem ci, int pos) {
        CreateChallengeDialog createChallengeDialog = CreateChallengeDialog.newInstance(ci, pos,
                new CreateChallengeDialog.OnSubmitListener() {
                    @Override
                    public void onSubmit(int pos, ChallengeItem challengeItem) {
                        challengeItem.setId(ci.getId());
                        MyApplication.getInstance().getGroupHelper().updateChallenge(groupId, challengeItem, new GroupHelper.CustomCompleteListener() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        });
                    }
                });
        createChallengeDialog.show(getChildFragmentManager(), null);
    }
}
