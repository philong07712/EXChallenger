package com.example.exchallenger.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.exchallenger.Adapters.WorkoutDetailAdapter;
import com.example.exchallenger.ExerciseActivity;
import com.example.exchallenger.Factories.WorkoutFactory;
import com.example.exchallenger.Helpers.LocalSaveHelper;
import com.example.exchallenger.Helpers.MainHelper;
import com.example.exchallenger.Helpers.WorkoutHelper;
import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutDetailActivity extends AppCompatActivity {
    RecyclerView rv_detail;
    String workoutID, groupID;
    WorkoutDetailAdapter adapter;
    List<Map<String, Object>> exerciseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        // Init view
        TextView tv_name = findViewById(R.id.workout_detail_name);
        ImageView img = findViewById(R.id.workout_detail_photo);
        rv_detail = findViewById(R.id.workout_detail_rv);
        final Button startButton = findViewById(R.id.workout_detail_btn);
        // get data from intent
        String workoutName = getIntent().getExtras().getString("name");
        tv_name.setText(workoutName);
        String groupID = getIntent().getExtras().getString("groupID");
        String workoutPhoto = getIntent().getExtras().getString("photo");
        Glide.with(this).load(workoutPhoto).into(img);

        workoutID = getIntent().getExtras().getString("workoutID");
        // init the recyclerView data
        getData();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExercise();
            }
        });
    }

    private void getData()
    {
        new WorkoutHelper().getDetailWorkout(workoutID, new WorkoutHelper.getWorkoutListener() {
            @Override
            public void onRead(List<Map<String, Object>> list) {
                exerciseList = list;
                updateWorkoutDetail(list);

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void updateWorkoutDetail(List<Map<String, Object>> list)
    {
        adapter = new WorkoutDetailAdapter(getApplicationContext(), list);
        rv_detail.setAdapter(adapter);
        rv_detail.setLayoutManager(new LinearLayoutManager(this));
    }
    private void updateExercise()
    {

        Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
        new LocalSaveHelper(getApplicationContext()).saveListMap("exercises", exerciseList);
        startActivity(intent);
    }

}
