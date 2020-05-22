package com.example.exchallenger.helpers;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.exchallenger.models.ChallengeItem;
import com.example.exchallenger.models.Group;
import com.example.exchallenger.models.GroupMember;
import com.example.exchallenger.MyApplication;
import com.example.exchallenger.ui.group.GroupDetailFragment;
import com.example.exchallenger.utils.AppUtils;
import com.example.exchallenger.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
                    listener.onRead(new ArrayList<>());
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

    private void createUserInGroupRanking(String userID, String groupID, AddListener listener) {
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
                ref.document(groupID).collection(Constants.PATH_RANKING)
                        .document(userID)
                        .set(userRanker)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                listener.onAdd();
                            }
                        });
//                // set that data to group ranking
//                database.collection("Groups").document(groupID).collection(Constants.PATH_RANKING).add(userRanker)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                            }
//                        });
            }
        });

    }

    public void getGroupRanking(String groupID, GetGroupRankingListener listener) {
        if (TextUtils.isEmpty(groupID)) {
            listener.onError("Invalid Group ID");
            return;
        }

        database.collection("Groups").document(groupID).collection(Constants.PATH_RANKING)
                .orderBy("point", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            listener.onError(e.getMessage());
                        }
                        if (queryDocumentSnapshots != null) {
                            List<GroupMember> rankers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Map<String, Object> ranker = document.getData();
                                rankers.add(AppUtils.convertMapToGroupMember(ranker));
                            }
                            listener.onRead(rankers);
                        }
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
        docRef.set(group)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CollectionReference collectionReference = docRef.collection(Constants.PATH_MINI_WORKOUT);
                        for (ChallengeItem challengeItem : challengeList) {
                            collectionReference.add(AppUtils.createMapFromChallengeItem(challengeItem));
                        }
                        listener.onCreateSuccess(docRef.getId(), group.getKey());
                        createUserInGroupRanking(admin, docRef.getId(), new AddListener() {
                            @Override
                            public void onAdd() {

                            }
                        });
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
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            listener.onError(e.getMessage());

                        }
                        if (queryDocumentSnapshots != null) {
                            List<ChallengeItem> challengeItems = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Map<String, Object> challengeMap = document.getData();
                                challengeItems.add(AppUtils.convertMapToChallengeItem(document.getId(), challengeMap));
                            }
                            listener.onRead(challengeItems);
                        }
                    }
                });
    }

    public interface CustomCompleteListener {
        void onSuccess();

        void onFailure(String message);
    }

    public void addFinishGroupChallenge(String groupId, String userId, String workOutId, int plusPoint, CustomCompleteListener completeListener) {
        DocumentReference groupRef = ref.document(groupId);
        DocumentReference docRef = groupRef.collection(Constants.PATH_RANKING).document(userId);
        DocumentReference workOutLogRef = groupRef.collection(Constants.PATH_MINI_WORKOUT)
                .document(workOutId);
        workOutLogRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> logData = documentSnapshot.getData();
                        Object oldData = logData.get("done");
                        if (oldData instanceof ArrayList) {
                            ArrayList<String> userDone = (ArrayList<String>) oldData;
                            userDone.add(userId);
                            logData.put("done", userDone);
                        } else {
                            ArrayList<String> userDone = new ArrayList<>();
                            userDone.add(userId);
                            logData.put("done", userDone);
                        }
                        workOutLogRef.set(logData);
                    }
                });

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> rankingData = documentSnapshot.getData();
                int point = AppUtils.getIntFromFirebaseMap(rankingData, "point");
                point += plusPoint;
                rankingData.put("point", point);
                docRef.update(rankingData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                completeListener.onSuccess();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                completeListener.onFailure(e.getMessage());
                            }
                        });
            }
        });

    }

    public void updateChallenge(String groupId, ChallengeItem ci, CustomCompleteListener listener) {
        DocumentReference docRef = ref.document(groupId).collection(Constants.PATH_MINI_WORKOUT)
                .document(ci.getId());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("ALOALO", documentSnapshot.getData().toString());

                docRef.update(AppUtils.createMapFromChallengeItem(ci))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                listener.onSuccess();
                                Log.d("ALOALO", groupId);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ALOALO", e.getMessage());
                                listener.onFailure(e.getMessage());
                            }
                        });
            }
        });
    }

    public void addGroupChallenge(String groupId, ChallengeItem challengeItem, CustomCompleteListener listener) {
        CollectionReference collectionReference = ref.document(groupId).collection(Constants.PATH_MINI_WORKOUT);
        collectionReference.add(AppUtils.createMapFromChallengeItem(challengeItem))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e.getMessage());

                    }
                });

    }

    public void deleteChallenge(String groupId, ChallengeItem dataItem) {
        ref.document(groupId).collection(Constants.PATH_MINI_WORKOUT)
                .document(dataItem.getId())
                .delete();
    }

    public void deleteGroup(Group group, CustomCompleteListener customCompleteListener) {
        ref.document(group.getGroupKey()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customCompleteListener.onFailure(e.getMessage());
                    }
                });
    }

    public void leaveGroup(Group group, String uid, CustomCompleteListener customCompleteListener) {


        ref.document(group.getGroupKey())
                .update("members", FieldValue.arrayRemove(uid))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ref.document(group.getGroupKey())
                                .collection(Constants.PATH_RANKING)
                                .document(uid)
                                .delete();
                        customCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customCompleteListener.onFailure(e.getMessage());
                    }
                });
    }

}
