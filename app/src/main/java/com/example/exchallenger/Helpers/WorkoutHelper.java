package com.example.exchallenger.Helpers;

import androidx.annotation.NonNull;

import com.example.exchallenger.Listeners.AddListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkoutHelper {
    FirebaseFirestore db;

    public interface getWorkoutListener
    {
        void onRead(List<Map<String, Object>> list);
        void onCancel();
    }

    public WorkoutHelper()
    {
        db = FirebaseFirestore.getInstance();
    }

    public void getChallengeWorkout(String userID, getWorkoutListener listener)
    {
        db.collection("Workout").document("WorkoutID1").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                    }
                });
    }

    public void addWorkout(Map<String, Object> bigWorkout, final List<Map<String, Object>> miniWorkout, final AddListener listener)
    {
        // get the key of the document
        final String key = db.collection("Workout").document().getId();
        bigWorkout.put("workoutID", key);
        // put the big workout in the collection
        db.collection("Workout").document(key).set(bigWorkout).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    // the task is successful
                    // we will
                    addMiniWorkout(key, miniWorkout, new AddListener() {
                        @Override
                        public void onAdd() {
                            listener.onAdd();
                        }
                    });
                }
                else
                {
                    //Todo: this will call when the add workout not successfully
                }
            }
        });
    }

    public void addMiniWorkout(String workoutID, List<Map<String, Object>> miniWorkout, final AddListener listener)
    {
        if (miniWorkout == null)
        {
            return;
        }
        db.collection("Workout").document(workoutID).collection("miniWorkout").add(miniWorkout)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                        {
                            listener.onAdd();
                        }
                    }
                });
    }


    public void getChallenges(String userID, final getWorkoutListener listener)
    {
        db.collection("Workout").whereArrayContains("members", userID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<Map<String, Object>> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                list.add(map);
                            }
                            listener.onRead(list);
                        }
                        else
                        {
                            listener.onCancel();
                        }
                    }
                });
    }

    public void getDailyWork( final getWorkoutListener listener)
    {
        db.collection("Workout").whereEqualTo("isChallenge", false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<Map<String, Object>> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                list.add(map);
                            }
                            listener.onRead(list);
                        }
                        else
                        {
                            listener.onCancel();
                        }
                    }
                });
    }
}
