package com.example.exchallenger.Helpers;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.exchallenger.Models.Group;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class GroupHelper {

    private FirebaseFirestore database;
    private CollectionReference ref;

    public GroupHelper() {
        database = FirebaseFirestore.getInstance();
        ref = database.collection("Groups");
    }

    public interface GetGroupDetailListener {
        void onRead(Group group);

        void onError(String error);
    }

    public interface GetGroupsListener {
        void onRead(List<String> groupIDs);

        void onError(String error);
    }

    public interface GroupJoinListener {
        void onSuccess();

        void onCancel(String message);
    }


    public void getGroup(String groupId, GetGroupDetailListener listener) {
        if (TextUtils.isEmpty(groupId)) {
            listener.onError("Invalid Group ID");
            return;
        }
        ref.document(groupId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Group group = new Group();
                group.setGroupKey(documentSnapshot.getString("groupKey"));
                group.setKey(documentSnapshot.getString("key"));
                group.setName(documentSnapshot.getString("name"));
                group.setPhoto(documentSnapshot.getString("photo"));
                listener.onRead(group);
            }
        })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        listener.onError("Cancel");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getMessage());
                    }
                });
    }

    public void getGroups(String userID, final GetGroupsListener listener) {
        if (TextUtils.isEmpty(userID)) {
            listener.onError("UserID is empty");
            return;
        }
        DocumentReference docRef = database.collection("Users_Groups").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = task.getResult().getData();
                    if (map != null) {
                        List<String> list = new ArrayList<>(map.keySet());
                        listener.onRead(list);
                    } else {
                        listener.onError("You haven't joined any group");
                    }
                } else {
                    listener.onError(task.getException().getMessage());
                }
            }
        });
    }

    public void joinGroup(String userID, String groupKey, GroupJoinListener listener) {
        ref.whereEqualTo("key", groupKey)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot.getDocuments().size() > 0
                                    && snapshot.getDocuments().get(0) != null) {
                                ref.document(snapshot.getDocuments().get(0).getString("groupKey"))
                                        .update("members", FieldValue.arrayUnion(userID)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            listener.onSuccess();
                                        } else {
                                            listener.onCancel(task.getException().getMessage());
                                        }
                                    }
                                });
                            } else {
                                listener.onCancel("Your code does not match any group");
                            }
                        } else {
                            listener.onCancel(task.getException().getMessage());
                        }
                    }
                });
    }

    public void createGroup(String userID, String groupKey, GroupJoinListener listener) {
        //Todo: get group with that key
        DocumentReference docRef = database.collection("Groups").document(groupKey);
        docRef.update("members", FieldValue.arrayUnion(userID)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onCancel(task.getException().getMessage());
                }
            }
        });
    }

    public void addUserToRanking(String userID, String groupKey, GroupJoinListener listener) {
        database.collection("Groups").document(groupKey).collection("Ranking");
    }

    public static String generateGroupCode() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }

    public Observable<Group> getGroupFromGroupId(String groupId) {
        return Observable.create(new ObservableOnSubscribe<Group>() {
            @Override
            public void subscribe(ObservableEmitter<Group> emitter) throws Exception {
                getGroup(groupId, new GetGroupDetailListener() {
                    @Override
                    public void onRead(Group group) {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(group);
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        if (!emitter.isDisposed()) {
                            emitter.onError(new Throwable(error));
                        }

                    }
                });
            }
        });
    }
}
