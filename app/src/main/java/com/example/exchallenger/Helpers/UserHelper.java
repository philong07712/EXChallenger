package com.example.exchallenger.Helpers;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.Listeners.EditListener;
import com.example.exchallenger.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHelper {

    FirebaseFirestore database;
    CollectionReference ref;
    // We will use custom interface here
    public interface GetUserInfo
    {
        void onRead(Map<String, Object> user);
    }

    public interface GetGroupListener
    {
        void onRead(List<Map<String, Object>> groups, List<String> keys);
        void onCancel(String message);
    }

    public UserHelper()
    {
        database = FirebaseFirestore.getInstance();
        ref = database.collection("Users");
    }

    // this will get one user by that user ID
    public void getUsersInfo(final String userID, final GetUserInfo listener)
    {
        DocumentReference docRef = ref.document(userID);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful())
//                {
//                    DocumentSnapshot document = task.getResult();
//                    listener.onRead(document.getData());
//                }
//                else
//                {
//                    listener.onRead(null);
//                }
//            }
//        });
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                {
                    Log.w(MainHelper.TAG, "Listener failed.", e);
                }
                if (documentSnapshot != null && documentSnapshot.exists())
                {
                    listener.onRead(documentSnapshot.getData());
                }
                else {
                    Log.d(MainHelper.TAG, "User is null");
                }
            }
        });
    }
// edit user information
    public void editUserInfo(final String userID, User user, final EditListener listener)
    {
        DocumentReference docRef = ref.document(userID);
        docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess();
            }
        });
    }
// Add new user by that user ID
    public void addNewUser(final String userID, final User user, final AddListener listener)
    {
        DocumentReference docRef = ref.document(userID);
        docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onAdd();
            }
        });
    }

    // add new user with unique id auto created
    public void addNewUser(final User user, final AddListener listener)
    {
        DocumentReference docRef = ref.document();
        user.setUserID(docRef.getId());
        docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onAdd();
            }
        });
    }

    public void addFinishWorkout(String userID, Map<String, Object> map, AddListener listener)
    {
        DocumentReference docRef = database.collection("Users").document(userID);
       docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> userData = documentSnapshot.getData();

                // plus point to totalPoint
                int totalPoint = Integer.parseInt(userData.get("totalPoints").toString());
                totalPoint += Integer.parseInt(map.get("totalPoints").toString());
                // plus Challenge
                int numChallenge = Integer.parseInt(userData.get("numChallenge").toString());
                numChallenge += 1;
                int totalTimes = Integer.parseInt(userData.get("totalTimes").toString());
                totalTimes += Integer.parseInt(map.get("totalTimes").toString());
                // now we will update the user data
                Map<String, Object> newInfo = new HashMap<>();
                newInfo.put("totalPoints", totalPoint);
                newInfo.put("numChallenge", numChallenge);
                newInfo.put("totalTimes", totalTimes);
                docRef.update(newInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onAdd();
                    }
                });
            }
        });
    }

    public void getGroupOfUser(String userID, GetGroupListener listener)
    {
        database.collection("Groups").whereArrayContains("members", userID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<Map<String, Object>> list = new ArrayList<>();
                            List<String> keys = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Map<String, Object> group = document.getData();
                                list.add(group);
                                keys.add(document.getId());
                            }
                            Log.d(MainHelper.TAG, list.toString());
                            listener.onRead(list, keys);
                        }
                        else
                        {
                            listener.onCancel(task.getException().getMessage());
                        }
                    }
                });
    }
}
