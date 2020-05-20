package com.example.exchallenger.Helpers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.exchallenger.Models.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.DocumentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupHelper {

    private FirebaseFirestore database;
    private CollectionReference ref;

    public GroupHelper() {
        database = FirebaseFirestore.getInstance();
        ref = database.collection("Groups");
    }

    public interface getGroupsListener {
        void onRead(List<Map<String, Object>> groups);
    }


    public interface GroupJoinListener
    {
        void onSuccess();
        void onCancel(String message);
    }

    public void getGroups(String userID, final getGroupsListener listener) {
        database.collection("Groups").whereArrayContains("members", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                {
                    Log.w(MainHelper.TAG, "Listener failed.", e);
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty())
                {
                    List<Map<String, Object>> groups = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                    {
                        Map<String, Object> map = document.getData();
                        groups.add(map);
                    }
                    listener.onRead(groups);
                }

                else {
                    Log.d(MainHelper.TAG, "Group list is null");
                }
            }
        });
    }

    public void joinGroup(String userID, String groupKey, GroupJoinListener listener)
    {
        //Todo: get group with that key
        DocumentReference docRef = database.collection("Groups").document(groupKey);
        docRef.update("members", FieldValue.arrayUnion(userID)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    listener.onSuccess();
                }
                else
                {
                    listener.onCancel(task.getException().getMessage());
                }
            }
        });
    }

    public void addUserToRanking(String userID, String groupKey, getGroupsListener listener)
    {
        database.collection("Groups").document(groupKey).collection("Ranking");
    }

    public void getGroupRanking(String groupID, getGroupsListener listener)
    {
        database.collection("Groups").document(groupID).collection("Ranking").orderBy("point")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    List<Map<String, Object>> rankers = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        Map<String, Object> ranker = document.getData();
                        rankers.add(ranker);
                    }
                    listener.onRead(rankers);
                }
            }
        });
    }
}
