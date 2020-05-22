package com.example.exchallenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

public class WorkFromHomeManager {
    private static final String TAG = WorkFromHomeManager.class.getSimpleName();
    public static Context context;
    public static final String MY_PREFS_NAME = "MY_PREFS_NAME";
    public static final String WORK_TIME = "WORK_TIME";
    public static final String RELAX_TIME = "RELAX_TIME";

    public static final String MORNING_FROM = "MORNING_FROM";
    public static final String MORNING_TO = "MORNING_TO";
    public static final String AFTERNOON_FROM = "AFTERNOON_FROM";
    public static final String AFTERNOON_TO = "AFTERNOON_TO";

    public static final String ALARM_TIME = "ALARM_TIME";
    public static final String ALARM_ON = "ALARM_ON";

    public static final int[] defaultTime = new int[]{8 * 60 * 60 * 1000, 12 * 60 * 60 * 1000, 14 * 60 * 60 * 1000, 18 * 60 * 60 * 1000};


    public static boolean isOnWorkingTime() {
        boolean hasSetData = getData(MORNING_FROM, 0) != 0;
        if (hasSetData) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int currentTimeMils = (hour * 60 + minutes) * 60 * 1000;

            if (currentTimeMils < getMorningFrom())
                return false;
            if (currentTimeMils > getMorningTo() && currentTimeMils < getAfternoonFrom())
                return false;
            if (currentTimeMils > getAfternoonTo())
                return false;

            return true;
        } else
            return true;
    }

    public static boolean isOnWorkingTime(int time) {
        boolean hasSetData = getData(MORNING_FROM, 0) != 0;
        if (hasSetData) {
            int currentTimeMils = time;

            if (currentTimeMils < getMorningFrom())
                return false;
            if (currentTimeMils > getMorningTo() && currentTimeMils < getAfternoonFrom())
                return false;
            if (currentTimeMils > getAfternoonTo())
                return false;

            return true;
        } else
            return true;
    }

    public static int getMorningFrom() {
        return getData(MORNING_FROM, defaultTime[0]);
    }

    public static int getMorningTo() {
        return getData(MORNING_TO, defaultTime[1]);
    }

    public static int getAfternoonFrom() {
        return getData(AFTERNOON_FROM, defaultTime[2]);
    }

    public static int getAfternoonTo() {
        return getData(AFTERNOON_TO, defaultTime[3]);
    }

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

    public static void setAlarmTime(int offset) {
        ArrayList<Integer> data = new ArrayList<>();
        int currentTime = getCurrentTimeToday();
        int workTime = getWorkTime();
        int relaxTime = getRelaxTime();
        int timeUp = currentTime + (workTime + relaxTime) + offset;
        int timeUpNextDay = 24 * 60 * 60 * 1000;
        int timeDown = currentTime + offset;
        int timeDownToday = 0;

        while (timeDown > timeDownToday) {
            if (isOnWorkingTime(timeDown)) {
                data.add(timeDown);
            }
            timeDown -= (workTime + relaxTime);
        }
        Collections.reverse(data);

        while (timeUp < timeUpNextDay) {
            if (isOnWorkingTime(timeUp)) {
                data.add(timeUp);
            }
            timeUp += (workTime + relaxTime);
        }
        setAlarmTime(convertIntegers(data));
    }

    private static void setAlarmTime(int[] list) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            str.append(list[i]).append(",");
        }
        editor.putString(ALARM_TIME, str.toString());
        editor.apply();
    }

    public static int[] getAlarmTime() {
        SharedPreferences editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String savedString = editor.getString(ALARM_TIME, "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        int[] savedList = new int[st.countTokens()];
        for (int i = 0; i < savedList.length; i++) {
            savedList[i] = Integer.parseInt(st.nextToken());
        }
        return savedList;
    }

    public static void setAlarm(boolean isON) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(ALARM_ON, isON);
        editor.apply();
    }

    public static boolean isAlarmOn() {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(ALARM_ON, false);
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

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public static int getCurrentTimeToday() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        return ((hour * 60 + minutes) * 60 + seconds) * 1000;
    }
}
