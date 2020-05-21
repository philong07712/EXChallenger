package com.example.exchallenger.Helpers;

import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutHelper {
    FirebaseFirestore db;

    public interface getWorkoutListener {
        void onRead(List<Map<String, Object>> list);

        void onCancel();
    }

    public WorkoutHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void getChallengeWorkout(String userID, getWorkoutListener listener) {
        db.collection("Workout").document("WorkoutID1").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                    }
                });
    }

    public void addWorkout(Map<String, Object> bigWorkout, final Map<String, Object> miniWorkout, final AddListener listener) {
        // get the key of the document
        final String key = db.collection("Workout").document().getId();
        bigWorkout.put("workoutID", key);
        // put the big workout in the collection
        db.collection("Workout").document(key).set(bigWorkout).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // the task is successful
                    // we will
                    addMiniWorkout(key, miniWorkout, new AddListener() {
                        @Override
                        public void onAdd() {
                            listener.onAdd();
                        }
                    });
                } else {
                    //Todo: this will call when the add workout not successfully
                }
            }
        });
    }

    public void addMiniWorkout(String workoutID, Map<String, Object> miniWorkout, final AddListener listener) {
        if (miniWorkout == null) {
            return;
        }
        String key = db.collection("Workout").document(workoutID).collection(Constants.PATH_MINI_WORKOUT).document().getId();
        miniWorkout.put("miniWorkoutID", key);
        db.collection("Workout").document(workoutID).collection(Constants.PATH_MINI_WORKOUT).document(key).set(miniWorkout)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onAdd();
                    }
                });

    }


    public void getChallenges(String userID, final getWorkoutListener listener) {
        db.collection("Workout").whereArrayContains("members", userID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Map<String, Object>> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                list.add(map);
                            }
                            listener.onRead(list);
                        } else {
                            listener.onCancel();
                        }
                    }
                });
    }

    public void getDailyWork(final getWorkoutListener listener) {
        db.collection("Workout").whereEqualTo("isChallenge", false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Map<String, Object>> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                list.add(map);
                            }
                            listener.onRead(list);
                        } else {
                            listener.onCancel();
                        }
                    }
                });
    }

    public void getDetailWorkout(String workoutID, final getWorkoutListener listener) {
        db.collection("Workout").document(workoutID).collection(Constants.PATH_MINI_WORKOUT).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Map<String, Object>> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                list.add(map);
                            }
                            listener.onRead(list);
                        } else {
                            listener.onCancel();
                        }
                    }
                });
    }

    public void addPointChallenge(String userID, String groupID, Map<String, Object> map, AddListener listener)
    {
        CollectionReference docCollection = db.collection("Groups").document(groupID).collection("Ranking");

        docCollection.whereEqualTo("userID", userID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        Map<String, Object> userData = document.getData();
                        Long totalPoint = Long.parseLong(document.get("point").toString());
                        totalPoint += Long.parseLong(map.get("point").toString());
                        userData.put("point", totalPoint);
                        Log.d(MainHelper.TAG, totalPoint.toString());
                        String documentID = document.getId();
                        docCollection.document(documentID).update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                listener.onAdd();
                            }
                        });
                    }
                }
            }
        });
    }

}
