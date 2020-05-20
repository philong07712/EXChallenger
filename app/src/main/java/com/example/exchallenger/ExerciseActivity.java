package com.example.exchallenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.exchallenger.Helpers.LocalSaveHelper;
import com.example.exchallenger.Helpers.MainHelper;
import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.Models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {
    List<Map<String, Object>> listExercise;

    ImageView image;
    TextView tvName, tvReps;
    Button giveUpBtn, doneBtn;
    int position;
    int currentPoint;
    int totalPoint;
    long startTime, endTime;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        listExercise = MyApplication.getInstance().getPrefsHelper().getListMap("exercises");
        position = 0;
        totalPoint = 0;
        // Init view
        image = findViewById(R.id.exercise_img);
        tvName = findViewById(R.id.exercise_name);
        tvReps = findViewById(R.id.exercise_rep);
        giveUpBtn = findViewById(R.id.btn_exercise_giveup);
        doneBtn = findViewById(R.id.btn_exercise_done);
        pb = findViewById(R.id.pb_exercise_activity);
        pb.setVisibility(View.INVISIBLE);
        startTime = System.currentTimeMillis();
        // loadExercise for first time
        loadExercise(0);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < listExercise.size() - 1)
                {
                    totalPoint += currentPoint;
                    position++;
                    loadExercise(position);
                }
                else
                {
                    loading();
                    // Todo: plus point to user ranked
                    addPoint();
                }
            }
        });

        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addPoint()
    {
        endTime = System.currentTimeMillis();
        long secondsDiff = (endTime - startTime) / 1000;
        Map<String, Object> map = new HashMap<>();
        map.put("totalPoints", totalPoint);
        map.put("totalTimes", secondsDiff);
        MyApplication.getInstance().getUserHelper().addFinishWorkout(MainHelper.getInstance().getUserID(), map, new AddListener() {
            @Override
            public void onAdd() {
                finish();
            }
        });
    }

    private void loadExercise(int position)
    {
        Map<String, Object> map = listExercise.get(position);
        String introductionPhoto = map.get("introduction").toString();
        String name = map.get("name").toString();
        double rep = (double) map.get("rep");
        int reps = (int) rep;

        currentPoint = (int) ((double) map.get("point"));

        Log.d(MainHelper.TAG, String.valueOf(totalPoint));
        updateExercise(introductionPhoto, name, reps);
    }

    private void updateExercise(String photo, String name, int rep)
    {
        Glide.with(this).load(photo).into(image);
        tvName.setText(name);
        String reps = "x " + rep + " Times";
        tvReps.setText(reps);
    }

    private void showMessage(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading()
    {
        pb.setVisibility(View.VISIBLE);
        giveUpBtn.setVisibility(View.INVISIBLE);
        doneBtn.setVisibility(View.INVISIBLE);
    }
}
