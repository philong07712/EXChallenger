package com.example.exchallenger.Helpers;

import com.example.exchallenger.Models.User;

import java.util.Map;

public class MainHelper {
    public static final String TAG = "MainHelper";
    String userID;
    String currentWorkoutID = null;
    Map<String, Object> workoutDetail = null;
    Map<String, Object> user = null;
    public static MainHelper instance;
    private MainHelper()
    {
        userID = "UserID1";

    }

    public static MainHelper getInstance()
    {
        if (instance == null)
        {
            instance = new MainHelper();
        }
        return instance;
    }

    public Map<String, Object> getWorkoutDetail() {
        return workoutDetail;
    }

    public void setWorkoutDetail(Map<String, Object> workoutDetail) {
        this.workoutDetail = workoutDetail;
    }

    public String getUserID()
    {
        return this.userID;
    }

    public void setUserID(String id)
    {
        this.userID = id;
    }

    public void setUser(Map<String, Object> user)
    {
        this.user = user;
    }

    public Map<String, Object> getUser()
    {
        return this.user;
    }
}
