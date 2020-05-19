package com.example.exchallenger;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class WorkFromHomeManager {

    public static Context context;

    public static final String MY_PREFS_NAME = "MY_PREFS_NAME";
    public static final String WORK_TIME = "WORK_TIME";
    public static final String RELAX_TIME = "RELAX_TIME";

    public static final String MORNING_FROM = "MORNING_FROM";
    public static final String MORNING_TO = "MORNING_TO";
    public static final String AFTERNOON_FROM = "AFTERNOON_FROM";
    public static final String AFTERNOON_TO = "AFTERNOON_TO";

    public static int[] defaultTime = new int[]{8 * 60 * 60 * 1000, 12 * 60 * 60 * 1000, 14 * 60 * 60 * 1000, 18 * 60 * 60 * 1000};

    public static void setWorkTime(int time) {
        saveData(WORK_TIME, time);
    }

    public static void setRelaxTime(int time) {
        saveData(RELAX_TIME, time);
    }

    public static int getWorkTime() {
        return getData(WORK_TIME, 25 * 60 * 1000);
    }

    public static int getRelaxTime() {
        return getData(RELAX_TIME, 5 * 60 * 1000);
    }

    public static void saveData(String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getData(String key, int defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt(key, defaultValue);
    }
}
