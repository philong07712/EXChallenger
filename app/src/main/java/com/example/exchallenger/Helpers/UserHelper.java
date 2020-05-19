package com.example.exchallenger.Helpers;

import androidx.annotation.NonNull;

import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.Listeners.EditListener;
import com.example.exchallenger.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserHelper {

    FirebaseFirestore database;
    CollectionReference ref;
    // We will use custom interface here
    public interface GetUserInfo
    {
        void onRead(User user);
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
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    User user = document.toObject(User.class);
                    listener.onRead(user);
                }
                else
                {
                    listener.onRead(null);
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
}
