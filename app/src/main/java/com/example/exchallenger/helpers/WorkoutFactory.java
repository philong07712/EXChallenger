package com.example.exchallenger.helpers;

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

    public Map<String, Object> createMiniWorkout()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("anim", "animation");
        map.put("introduction", "https://firebasestorage.googleapis.com/v0/b/exchallenger-9b166.appspot.com/o/Men%20Push%20up%20position%20flat%20vector%403x.png?alt=media&token=850fe2c1-7904-46b7-a7a0-33fc655c103e");
        map.put("name", "Push up");
        map.put("photo", "https://firebasestorage.googleapis.com/v0/b/exchallenger-9b166.appspot.com/o/Flat%403x.png?alt=media&token=20bde06e-dd04-4859-abe5-d8eae9a08c77");
        map.put("point", 15);
        map.put("rep", 15);
        map.put("time", 15);
        map.put("type", "repeat");

        return map;
    }

}
