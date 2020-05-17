package com.example.exchallenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;


import com.example.exchallenger.Helpers.MainHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testingHelper();
    }


    private void testingHelper()
    {
//        db.collection("UserExercises").whereArrayContains("members", "UserID1").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot document : queryDocumentSnapshots)
//                        {
//                            Log.d(MainHelper.TAG, document.getId() + "=>" + document.getData());
//                        }
//                    }
//                });
        db.collection("Workout").document("WorkoutID1").collection("miniWorkout")
                .whereEqualTo("name", "push up").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                {
                    Log.d(MainHelper.TAG, document.getId() + "=>" + document.getData());
                }
            }
        });

    }
}
