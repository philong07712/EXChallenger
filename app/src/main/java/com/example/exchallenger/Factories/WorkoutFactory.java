package com.example.exchallenger.Factories;

import com.google.firebase.firestore.FieldValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WorkoutFactory {

    public Map<String, Object> createChallenge()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("isChallenge", true);
        map.put("members", Arrays.asList("UserID1", "UserID3", "UserID4"));
        map.put("name", "Running hackathon");
        map.put("photo", "https://cdn.pixabay.com/photo/2019/12/20/23/07/landscape-4709500_960_720.jpg");
        map.put("timeStamp", FieldValue.serverTimestamp());
        return map;
    }

    public Map<String, Object> createDailyWork()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("isChallenge", false);
        map.put("name", "Push up");
        map.put("photo", "https://c4.wallpaperflare.com/wallpaper/938/467/793/men-fitness-pushups-wallpaper-preview.jpg");
        map.put("timeStamp", FieldValue.serverTimestamp());
        return map;
    }

}
