package com.example.exchallenger;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.exchallenger.Helpers.MainHelper;
import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.customviews.CircleProgressView;
import com.example.exchallenger.tensorflow.PosenetActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {
    private static final String TAG = ExerciseActivity.class.getSimpleName();
    List<Map<String, Object>> listExercise;

    //    ImageView image;
    TextView tvName, tvReps, txtCount;
    Button giveUpBtn, startBtn;
    int position;
    int currentPoint;
    int totalPoint;
    long startTime, endTime;
    ProgressBar pb;
    LottieAnimationView lottieAnimationView;
    FrameLayout cameraView;
    View viewPlaceholderAnimation;
    int currentWorkoutPosition = 0;
    CircleProgressView circleProgressView;
    TextView txtTimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        listExercise = MyApplication.getInstance().getPrefsHelper().getListMap("exercises");
        position = 0;
        totalPoint = 0;
        // Init view
        circleProgressView = findViewById(R.id.circle_progress);
        txtTimeLeft = findViewById(R.id.txt_time_left);
        cameraView = findViewById(R.id.camera_view);
        lottieAnimationView = findViewById(R.id.exercise_img);
        viewPlaceholderAnimation = findViewById(R.id.view_place_holder_animation);
        txtCount = findViewById(R.id.txt_count);
        tvName = findViewById(R.id.exercise_name);
        tvReps = findViewById(R.id.exercise_rep);
        giveUpBtn = findViewById(R.id.btn_exercise_giveup);
        startBtn = findViewById(R.id.btn_exercise_done);
        pb = findViewById(R.id.pb_exercise_activity);
        pb.setVisibility(View.INVISIBLE);
        startTime = System.currentTimeMillis();
        // loadExercise for first time
        currentWorkoutPosition = 0;
        hideTimeLeft();
        loadExercise();
        startBtn.setOnClickListener(v -> {
            setCameraView();
//            if (position < listExercise.size() - 1) {
//                totalPoint += currentPoint;
//                position++;
//                loadExercise(position);
//            } else {
//                loading();
//                // Todo: plus point to user ranked
//                addPoint();
//            }
        });

        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        setCameraView();
    }

     private void addPoint() {
        endTime = System.currentTimeMillis();
        long secondsDiff = (endTime - startTime) / 1000;
        Map<String, Object> map = new HashMap<>();
        map.put("totalPoints", totalPoint);
        map.put("totalTimes", secondsDiff);
        Map<String, Object> currentWorkout = new LocalSaveHelper(this).getMap("currentWorkout");
        boolean isChallenge = (boolean) currentWorkout.get("isChallenge");
        new UserHelper().addFinishWorkout(MyApplication.user.getUserID(), map, new AddListener() {

            @Override
            public void onAdd() {
                // if this exercise is challenge then we will add point to that ranking system
                if (isChallenge)
                {
                    addPointToChallenge(currentWorkout);
                }
                else
                {
                    finish();
                }
            }
        });
    }

     private void addPointToChallenge(Map<String, Object> currentWorkout)
    {
        String userID = MyApplication.user.getUserID();
        Map<String, Object> map = new HashMap<>();
        map.put("point", totalPoint);
//        new WorkoutHelper().addPointChallenge(userID, "Groups_key_1", map, new AddListener() {
//            @Override
//            public void onAdd() {
//                finish();
//            }
//        });
    }

    private void loadExercise() {
        Map<String, Object> map = listExercise.get(currentWorkoutPosition);
        String introductionPhoto = map.get("introduction").toString();
        String name = map.get("name").toString();
        double rep = (double) map.get("rep");
        int reps = (int) rep;

        currentPoint = (int) ((double) map.get("point"));

        Log.d(MainHelper.TAG, String.valueOf(totalPoint));
        updateExercise(introductionPhoto, name, reps);
    }

    private void updateExercise(String photo, String name, int rep) {
        lottieAnimationView.setAnimation("squat.json");
        lottieAnimationView.playAnimation();
        tvName.setText(name);
        String reps = "x " + rep + " Times";
        tvReps.setText(reps);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading() {
        pb.setVisibility(View.VISIBLE);
        giveUpBtn.setVisibility(View.INVISIBLE);
        startBtn.setVisibility(View.INVISIBLE);
    }

    ConstraintLayout.LayoutParams previousLayoutParams1, previousLayoutParams2, previousLayoutParams3;

    private void setCameraView() {
        cameraView.setVisibility(View.VISIBLE);
        if (previousLayoutParams1 == null) {
            previousLayoutParams1 = (ConstraintLayout.LayoutParams) lottieAnimationView.getLayoutParams();
            previousLayoutParams2 = (ConstraintLayout.LayoutParams) tvName.getLayoutParams();
            previousLayoutParams3 = (ConstraintLayout.LayoutParams) tvReps.getLayoutParams();
        }
        ConstraintLayout.LayoutParams viewHolderAnimationLayoutParams = (ConstraintLayout.LayoutParams) viewPlaceholderAnimation.getLayoutParams();
        lottieAnimationView.setLayoutParams(viewHolderAnimationLayoutParams);

        ConstraintLayout.LayoutParams viewHolderExcerciseNameLayoutParams = (ConstraintLayout.LayoutParams) findViewById(R.id.view_place_holder_exercise_name).getLayoutParams();
        tvName.setLayoutParams(viewHolderExcerciseNameLayoutParams);

        ConstraintLayout.LayoutParams viewHolderExcerciseRepLayoutParams = (ConstraintLayout.LayoutParams) findViewById(R.id.view_place_holder_exercise_rep).getLayoutParams();
        tvReps.setLayoutParams(viewHolderExcerciseRepLayoutParams);

        txtCount.setVisibility(View.VISIBLE);
        txtCount.setText("0");
        startBtn.setVisibility(View.GONE);
        hideTimeLeft();
        addCameraView();
    }

    private void addCameraView() {
        PosenetActivity posenetActivity = new PosenetActivity();
        posenetActivity.setOnDetectListener(new PosenetActivity.OnDetectListener() {
            @Override
            public void onNewCount(int newCount) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtCount.setText(newCount + "");
//                        if (newCount >= 1) {
                        if (newCount >= (double) listExercise.get(currentWorkoutPosition).get("rep")) {
                            posenetActivity.reset();
                            doneWorkout();
                        }
                    }
                });
            }
        });
        pb.setVisibility(View.VISIBLE);
        posenetActivity.setOnCameraLoaded(new PosenetActivity.OnCameraLoaded() {
            @Override
            public void onCameraLoaded() {
                pb.setVisibility(View.INVISIBLE);
            }
        });
        posenetActivity.setWorkoutName(tvName.getText().toString().trim());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.camera_view, posenetActivity, PosenetActivity.class.getSimpleName()).commitAllowingStateLoss();
    }

    private void removeCameraView() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PosenetActivity.class.getSimpleName());
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void doneWorkout() {
        if (currentWorkoutPosition <= listExercise.size() - 1) {
            totalPoint += currentPoint;
            currentWorkoutPosition++;
            removeCameraView();
            showWaitingToNextWorkout();
        } else {
            addPoint();
        }
    }

    private void showWaitingToNextWorkout() {
        lottieAnimationView.setLayoutParams(previousLayoutParams1);
        txtCount.setVisibility(View.INVISIBLE);
        loadExercise();
        showTimeLeft();
        startBtn.setVisibility(View.VISIBLE);
    }

    int progress = 15000;

    private void showTimeLeft() {
        progress = 15000;
        circleProgressView.setVisibility(View.VISIBLE);
        txtTimeLeft.setVisibility(View.VISIBLE);
        circleProgressView.setMax(progress);
        circleProgressView.setProgress(progress);
        circleProgressView.setOnProgressChangeListener(new CircleProgressView.OnProgressChangeListener() {
            @Override
            public void onChanged(float angle, float value) {
                txtTimeLeft.setText(String.format(Locale.US, "%d", (int) value/1000));
            }
        });

        ValueAnimator va = ValueAnimator.ofFloat(progress, 0f);
        int mDuration = progress; //in millis
        va.setDuration(mDuration);
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (circleProgressView.getVisibility() == View.VISIBLE) {
                    circleProgressView.setProgress((int) value);
                    if (value == 0f) {
                        startBtn.performClick();
                    }
                } else {
                    va.cancel();
                }
            }
        });
        va.start();
    }

    private void hideTimeLeft() {
        circleProgressView.setVisibility(View.GONE);
        txtTimeLeft.setVisibility(View.GONE);
    }
}
