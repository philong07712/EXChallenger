package com.example.exchallenger.Helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.exchallenger.Models.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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
        void onRead(List<String> groupsID);
    }

    public interface GroupJoinListener
    {
        void onSuccess();
        void onCancel(String message);
    }

    public void getGroups(String userID, final getGroupsListener listener) {
        DocumentReference docRef = database.collection("Users_Groups").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    Map<String, Object> map = task.getResult().getData();
                    List<String> list = new ArrayList<>(map.keySet());
                    listener.onRead(list);
                }
                else
                {
                    listener.onRead(null);
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

    public void addUserToRanking(String userID, String groupKey, GroupJoinListener listener)
    {
        database.collection("Groups").document(groupKey).collection("Ranking");
    }
}
