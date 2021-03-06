package com.example.exchallenger.ui.workout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.exchallenger.helpers.WorkoutFactory;
import com.example.exchallenger.helpers.WorkoutHelper;
import com.example.exchallenger.helpers.AddListener;
import com.example.exchallenger.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;

public class WorkoutFragment extends Fragment {

    RecyclerView rv_challenges, rv_daily;
    TextView tv_challenge;

    public WorkoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        rv_challenges = view.findViewById(R.id.workout_challenges_rv);
        rv_daily = view.findViewById(R.id.workout_daily_rv);
        tv_challenge = view.findViewById(R.id.tv_challenge);
        rv_daily.setLayoutManager(new LinearLayoutManager(getActivity()));


    }


    private void getData() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            new WorkoutHelper().getChallenges(uid, new WorkoutHelper.getWorkoutListener() {
                @Override
                public void onRead(List<Map<String, Object>> list) {
                    loadWorkoutChallenges(list);
                }

                @Override
                public void onCancel() {

                }
            });
        }


        new WorkoutHelper().getDailyWork(new WorkoutHelper.getWorkoutListener() {
            @Override
            public void onRead(List<Map<String, Object>> list) {
                loadWorkoutDaily(list);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    // This method will load the recyclerView of Challenges
    private void loadWorkoutChallenges(List<Map<String, Object>> list) {
        if (getContext() != null) {
            WorkoutAdapter dailyAdapter = new WorkoutAdapter(getContext(), list);
            rv_challenges.setAdapter(dailyAdapter);
            rv_challenges.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    // This method will load the recyclerView of Daily
    private void loadWorkoutDaily(List<Map<String, Object>> list) {
        if (getContext() != null) {
            WorkoutAdapter dailyAdapter = new WorkoutAdapter(getContext(), list);
            rv_daily.setAdapter(dailyAdapter);
        }
    }

    private void createWorkout() {
        WorkoutFactory factory = new WorkoutFactory();
        Map<String, Object> bigWork = factory.createDailyWork();
        new WorkoutHelper().addWorkout(bigWork, null, new AddListener() {
            @Override
            public void onAdd() {
            }
        });
    }
}
