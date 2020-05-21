package com.example.exchallenger.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.exchallenger.Models.ChallengeItem;
import com.example.exchallenger.Models.Group;
import com.example.exchallenger.Models.GroupMember;
import com.example.exchallenger.Models.User;
import com.google.common.primitives.ImmutableDoubleArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AppUtils {
    public static String getScheduleText(List<Long> repeat) {
        if (repeat == null || repeat.size() < 7) {
            return "";
        }
        int sum = 0;

        for (long value : repeat) {
            sum += value;

        }
        if (sum == 0) {
            return "Once";
        }
        if (sum == 7) {
            return "Everyday";
        }
        if (sum == repeat.get(0) + repeat.get(6)) {
            return "Weekend";
        }
        if (sum == 5 && (repeat.get(0) + repeat.get(6) == 0)) {
            return "Weekdays";
        }
        StringBuilder concat = new StringBuilder();
        if (repeat.get(0) == 1) {
            concat.append("SU, ");
        }
        if (repeat.get(1) == 1) {
            concat.append("MO, ");
        }
        if (repeat.get(2) == 1) {
            concat.append("TU, ");
        }
        if (repeat.get(3) == 1) {
            concat.append("WE, ");
        }
        if (repeat.get(4) == 1) {
            concat.append("TH, ");
        }
        if (repeat.get(5) == 1) {
            concat.append("FR, ");
        }
        if (repeat.get(6) == 1) {
            concat.append("SA, ");
        }
        int lastComma = concat.lastIndexOf(", ");
        if (lastComma > 0) {
            concat.delete(lastComma, lastComma + 2);
        }
        return concat.toString();
    }

    public static String getTimeFromHourAndUnit(int hour, int minute) {
        String hourStr = hour > 9 ? (hour + "") : ("0" + hour);
        String minStr = minute > 9 ? (minute + "") : ("0" + minute);

        return hourStr + ":" + minStr;
    }

    public static User convertMapToUser(Map<String, Object> user) {
        User currentUser = new User();
        currentUser.setNumChallenger((long) user.get("numChallenger"));
        currentUser.setTotalPoints((long) user.get("totalPoints"));
        currentUser.setUserID((String) user.get("userID"));
        currentUser.setEmail((String) user.get("email"));
        currentUser.setName((String) user.get("name"));
        currentUser.setPhoneNumber((String) user.get("phoneNumber"));
        currentUser.setPhoto((String) user.get("photo"));
        return currentUser;
    }

    public static GroupMember convertMapToGroupMember(Map<String, Object> ranker) {
        GroupMember member = new GroupMember();
        member.setGroupID((String) ranker.get("groupID"));
        member.setName((String) ranker.get("name"));
        member.setPhoto((String) ranker.get("photo"));
        member.setUserId((String) ranker.get("userID"));
        member.setPoint(getIntFromFirebaseMap(ranker, "point"));
        return member;
    }

    public static Group convertMapToGroup(Map<String, Object> map) {
        Group group = new Group();
        group.setName((String) map.get("name"));
        group.setGroupKey((String) map.get("groupKey"));
        group.setKey((String) map.get("key"));
        group.setPhoto((String) map.get("photo"));
        Object members = map.get("members");
        group.setMembers(members instanceof ArrayList ? (ArrayList<String>) members : new ArrayList<>());
        return group;
    }

    public static ChallengeItem convertMapToChallengeItem(Map<String, Object> map) {
        ChallengeItem challengeItem = new ChallengeItem();
        challengeItem.setUnit((String) map.get("unit"));
        challengeItem.setNumber(AppUtils.getIntFromFirebaseMap(map, "time"));
        if (map.get("rep") instanceof ArrayList) {
            challengeItem.setRepeat((ArrayList<Long>) map.get("rep"));
        }
        challengeItem.setType((String) map.get("name"));
        challengeItem.setPoint(AppUtils.getIntFromFirebaseMap(map, "point"));
        challengeItem.setPhoto((String) map.get("photo"));
        challengeItem.setHour(AppUtils.getIntFromFirebaseMap(map, "hour"));
        challengeItem.setMinute(AppUtils.getIntFromFirebaseMap(map, "minute"));
        return challengeItem;
    }

    private static int getIntFromFirebaseMap(Map<String, Object> map, String key) {
        Long hour = (Long) map.get(key);
        int value = hour != null ? hour.intValue() : 0;
        return value;
    }

    public static Map<String, Object> createMapFromChallengeItem(ChallengeItem challengeItem) {
        Map<String, Object> map = new HashMap<>();
        map.put("anim", "animation");
        map.put("name", challengeItem.getType());
        map.put("photo", challengeItem.getPhoto());
        map.put("point", challengeItem.getPoint());
        map.put("rep", challengeItem.getRepeat());
        map.put("time", challengeItem.getNumber());
        map.put("unit", AppUtils.getUnitOfExercise(challengeItem.getType()));
        map.put("hour", challengeItem.getHour());
        map.put("minute", challengeItem.getMinute());
        return map;
    }

    public static String getPhotoOfExercise(String type) {
        if (TextUtils.isEmpty(type)) {
            return "https://firebasestorage.googleapis.com/v0/b/exchallenger-9b166.appspot.com/o/sports.png?alt=media&token=fe57df92-2c81-4e37-a14e-05e160248d3c";
        }

        switch (type.toLowerCase()) {
            case ChallengeItem.PLANK:
                return "https://firebasestorage.googleapis.com/v0/b/exchallenger-9b166.appspot.com/o/Flat%403x.png?alt=media&token=20bde06e-dd04-4859-abe5-d8eae9a08c77";

            case ChallengeItem.PUSH_UP:
                return "https://firebasestorage.googleapis.com/v0/b/exchallenger-9b166.appspot.com/o/Men%20Push%20up%20position%20flat%20vector%403x.png?alt=media&token=850fe2c1-7904-46b7-a7a0-33fc655c103e";

            case ChallengeItem.SQUAT:
                return "https://firebasestorage.googleapis.com/v0/b/exchallenger-9b166.appspot.com/o/squat%403x.png?alt=media&token=b1c6283d-7af3-4383-b4b2-c22f524a600c";
            default:
                return "https://firebasestorage.googleapis.com/v0/b/exchallenger-9b166.appspot.com/o/sports.png?alt=media&token=fe57df92-2c81-4e37-a14e-05e160248d3c";
        }
    }

    public static int getPointOfExercise(String type) {
        if (TextUtils.isEmpty(type)) {
            return new Random().nextInt(5);
        }

        switch (type.toLowerCase()) {
            case ChallengeItem.PLANK:
                return 5;
            case ChallengeItem.PUSH_UP:
                return 2;
            case ChallengeItem.SQUAT:
                return 1;
            default:
                return new Random().nextInt(5);
        }
    }

    public static String getUnitOfExercise(String type) {
        if (TextUtils.isEmpty(type)) {
            return Constants.UNIT_NUMBER;
        }

        switch (type.toLowerCase()) {
            case ChallengeItem.PLANK:
                return Constants.UNIT_MINUTE;
            case ChallengeItem.PUSH_UP:
            case ChallengeItem.SQUAT:
            default:
                return Constants.UNIT_NUMBER;
        }
    }

    public static String getUnitStrOfExercise(String type) {
        if (TextUtils.isEmpty(type)) {
            return " times";
        }

        switch (type.toLowerCase()) {
            case ChallengeItem.PLANK:
                return " mins";
            case ChallengeItem.PUSH_UP:
            case ChallengeItem.SQUAT:
            default:
                return " times";
        }
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static Map<String, Object> getMapFromChallenge(ChallengeItem dataItem) {
        Map<String, Object> map = new HashMap<>();
        map.put("introduction", dataItem.getPhoto());
        map.put("point", dataItem.getPoint());
        map.put("name", dataItem.getType());
        map.put("rep", dataItem.getNumber());
        return map;
    }
}
