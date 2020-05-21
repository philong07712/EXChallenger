package com.example.exchallenger.Helpers;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.Models.ChallengeItem;
import com.example.exchallenger.Models.Group;
import com.example.exchallenger.Models.GroupMember;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.utils.AppUtils;
import com.example.exchallenger.utils.Constants;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class GroupHelper {

    private FirebaseFirestore database;
    private CollectionReference ref;

    public GroupHelper() {
        database = FirebaseFirestore.getInstance();
        ref = database.collection("Groups");
    }

    public interface GetGroupsListener {

        void onRead(List<Group> groups);

        void onError(String error);
    }

    public interface GetGroupRankingListener {

        void onRead(List<GroupMember> groups);

        void onError(String error);
    }

    public interface GetGroupDetailListener {
        void onRead(Group group);

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
        database.collection("Groups").whereArrayContains("members", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(MainHelper.TAG, "Listener failed.", e);
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    List<Group> groups = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> map = document.getData();
                        groups.add(AppUtils.convertMapToGroup(map));
                    }
                    listener.onRead(groups);
                } else {
                    Log.d(MainHelper.TAG, "Group list is null");
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
                                DocumentSnapshot doc = snapshot.getDocuments().get(0);
                                Log.d("ALOALO", doc.toString());
                                // get group ID
                                String groupID = doc.getId();
                                if (doc != null && !TextUtils.isEmpty(doc.getString("groupKey"))) {
                                    ref.document(doc.getString("groupKey"))
                                            .update("members", FieldValue.arrayUnion(userID)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // create a new user in that group ranking system
                                                createUserInGroupRanking(userID, groupID, new AddListener() {
                                                    @Override
                                                    public void onAdd() {
                                                        listener.onSuccess();
                                                    }
                                                });
                                            } else {
                                                listener.onCancel(task.getException().getMessage());
                                            }
                                        }
                                    });
                                } else {
                                    listener.onCancel("Your code does not match any group");
                                }
                            } else {
                                listener.onCancel("Your code does not match any group");
                            }
                        } else {
                            listener.onCancel(task.getException().getMessage());
                        }
                    }
                });
    }

    private void createUserInGroupRanking(String userID, String groupID, AddListener listener)
    {
        database.collection("Groups").document(groupID).collection("Ranking").whereEqualTo("userID", userID).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        if (task.getResult().isEmpty())
                        {
                            MyApplication.getInstance().getUserHelper().getUsersInfo(userID, new UserHelper.GetUserInfo() {
                                @Override
                                public void onRead(Map<String, Object> user) {
                                    Log.d(MainHelper.TAG, user.toString());
                                    Map<String, Object> userRanker = new HashMap<>();
                                    userRanker.put("groupID", groupID);
                                    userRanker.put("name", user.get("name").toString());
                                    userRanker.put("photo", user.get("photo").toString());
                                    userRanker.put("userID", userID);
                                    userRanker.put("point", 0);
                                    Log.d(MainHelper.TAG, userRanker.toString());
                                    // set that data to group ranking
                                    database.collection("Groups").document(groupID).collection("Ranking").add(userRanker)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    listener.onAdd();
                                                }
                                            });
                                }
                            });
                        }
                    }
                }
            });

    }

    public void addUserToRanking(String userID, String groupKey, GetGroupsListener listener) {
        // This not done
        database.collection("Groups").document(groupKey).collection("Ranking");
    }

    public void getGroupRanking(String groupID, GetGroupRankingListener listener) {
        if (TextUtils.isEmpty(groupID)) {
            listener.onError("Invalid Group ID");
            return;
        }

        database.collection("Groups").document(groupID).collection("Ranking").orderBy("point")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<GroupMember> rankers = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Map<String, Object> ranker = document.getData();
                            rankers.add(AppUtils.convertMapToGroupMember(ranker));
                        }
                        listener.onRead(rankers);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getMessage());

                    }
                });
    }

    public interface CreateGroupListener {
        void onCreateSuccess(String groupId, String key);

        void onFail(String message);
    }

    public void createGroup(String groupName, List<ChallengeItem> challengeList, CreateGroupListener listener) {
        DocumentReference docRef = ref.document();
        Group group = new Group();
        group.setGroupKey(docRef.getId());
        group.setKey(randomAlphaNumeric(6));
        group.setName(groupName);
        group.setPhoto(generateGroupPhoto());
        String admin = FirebaseAuth.getInstance().getUid();
        group.setMembers(Arrays.asList(admin));
        group.setAdmin(admin);
        docRef.set(group).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CollectionReference collectionReference = docRef.collection(Constants.PATH_MINI_WORKOUT);
                for (ChallengeItem challengeItem : challengeList) {
                    collectionReference.add(AppUtils.createMapFromChallengeItem(challengeItem));
                }
                listener.onCreateSuccess(docRef.getId(), group.getKey());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFail(e.getMessage());
                    }
                });
    }


    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String generateGroupPhoto() {
        int random = new Random().nextInt(Constants.GROUP_IMAGES.length);
        return Constants.GROUP_IMAGES[random];
    }

    public interface GetGroupChallengeListener {
        void onRead(List<ChallengeItem> challengeItems);

        void onError(String message);
    }

    public void getGroupChallenges(String groupId, GetGroupChallengeListener listener) {
        if (TextUtils.isEmpty(groupId)) {
            listener.onError("Invalid Group ID");
            return;
        }

        database.collection("Groups").document(groupId).collection(Constants.PATH_MINI_WORKOUT)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ChallengeItem> challengeItems = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> challengeMap = document.getData();
                        challengeItems.add(AppUtils.convertMapToChallengeItem(challengeMap));
                    }
                    listener.onRead(challengeItems);
                }
            }
        });
    }
}
