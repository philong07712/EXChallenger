package com.example.exchallenger.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class LocalSaveHelper {
    Context activity;
    public LocalSaveHelper(Context activity)
    {
        this.activity = activity;
    }
    public void saveListMap(String key, Object obj)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.apply();
    }
    public void saveString(String key, String message)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, message);
        editor.apply();
    }

    public void saveMap(String key, Map<String, Object> map)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(map);
        editor.putString(key, json);
        editor.apply();
    }

    public String getString(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String message = prefs.getString(key, "");
        return message;
    }

    public List<Map<String, Object>> getListMap(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        Type type = new TypeToken<List<Map<String, Object>>>() {}.getType();
        List<Map<String, Object>> obj = gson.fromJson(json, type);
        return obj;
    }

    public Map<String, Object> getMap(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> obj = gson.fromJson(json, type);
        return obj;
    }

}
