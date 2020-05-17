package com.example.exchallenger.Models;

import android.database.DatabaseUtils;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    private String email, name, phoneNumber, photo, userID;
    private int totalPoints, numChallenger;
    private Object timeStamp;

    public User() {
    }

    public User(String email, String name, String phoneNumber, String photo, String userID) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.userID = userID;
        this.totalPoints = 0;
        this.numChallenger = 0;
        this.timeStamp = FieldValue.serverTimestamp();
    }

    public User(User user)
    {
        this.email = user.email;
        this.name = user.name;
        this.phoneNumber = user.phoneNumber;
        this.photo = user.photo;
        this.userID = user.userID;
        this.totalPoints = user.totalPoints;
        this.numChallenger = user.numChallenger;
        if (user.timeStamp == null) {
            this.timeStamp = FieldValue.serverTimestamp();
        }
        else
            this.timeStamp = user.timeStamp;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getNumChallenger() {
        return numChallenger;
    }

    public void setNumChallenger(int numChallenger) {
        this.numChallenger = numChallenger;
    }
}
