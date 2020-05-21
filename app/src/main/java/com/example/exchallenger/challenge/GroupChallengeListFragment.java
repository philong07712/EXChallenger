package com.example.exchallenger.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchallenger.ExerciseActivity;
import com.example.exchallenger.Helpers.GroupHelper;
import com.example.exchallenger.Helpers.LocalSaveHelper;
import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Helpers.WorkoutHelper;
import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.Models.ChallengeItem;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.base.OnItemClickListener;
import com.example.exchallenger.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.facebook.FacebookSdk.getApplicationContext;

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

        challengeAdapter.setOnItemClickListener(new OnItemClickListener<ChallengeItem>() {
            @Override
            public void onItemClick(int position, ChallengeItem dataItem) {
                if(dataItem.isDone()){
                    return;
                }
//                Map<String, Object> map = new HashMap<>();
//                map.put("totalPoints", dataItem.getPoint());
//                map.put("totalTimes", 4 * 60000);
//                MyApplication.getInstance().getGroupHelper().addFinishGroupChallenge(groupId, MyApplication.firebaseUser.getUid(), dataItem.getId(),
//                        dataItem.getPoint(), new GroupHelper.CustomCompleteListener() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onFailure(String message) {
//
//                            }
//                        });
//                new UserHelper().addFinishWorkout(MyApplication.firebaseUser.getUid(), map, new AddListener() {
//
//                    @Override
//                    public void onAdd() {
//                    }
//                });
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
}
