package com.example.exchallenger;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.example.exchallenger.Helpers.GroupHelper;
import com.example.exchallenger.Helpers.LocalSaveHelper;
import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Helpers.WorkoutHelper;
import com.example.exchallenger.Models.User;

public class MyApplication extends Application {

    public static MyApplication instance;
    private LocalSaveHelper prefsHelper;
    private UserHelper userHelper;

    public GroupHelper getGroupHelper() {
        return groupHelper;
    }

    private GroupHelper groupHelper;
    public WorkoutHelper workoutHelper;
    public static User user;

    public static MyApplication getInstance(){
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        WorkFromHomeManager.context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        userHelper = new UserHelper();
        workoutHelper = new WorkoutHelper();
        prefsHelper = new LocalSaveHelper(this);
        groupHelper = new GroupHelper();
    }

    public LocalSaveHelper getPrefsHelper() {
        return prefsHelper;
    }

    public void setPrefsHelper(LocalSaveHelper prefsHelper) {
        this.prefsHelper = prefsHelper;
    }

    public UserHelper getUserHelper() {
        return userHelper;
    }

    public void setUserHelper(UserHelper userHelper) {
        this.userHelper = userHelper;
    }
}
