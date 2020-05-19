package com.example.exchallenger;

public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WorkFromHomeManager.context = this;
    }
}
