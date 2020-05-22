package com.example.exchallenger.models;

import java.util.ArrayList;

public class ChallengeItem {
    public static final String SQUAT = "squat";
    public static final String PUSH_UP = "push up";
    public static final String PLANK = "plank";
    private String type;
    private int number;
    private int hour;
    private int minute;
    private String unit;
    private ArrayList<Long> repeat;
    private int point;
    private String photo;
    private String id;
    private boolean isDone;

    public ChallengeItem() {
    }

    public ChallengeItem(String type, int number, int hour, int minute, ArrayList<Long> repeat, String unit,
                         int point, String photo) {
        this.type = type;
        this.number = number;
        this.hour = hour;
        this.minute = minute;
        this.repeat = repeat;
        this.unit = unit;
        this.point = point;
        this.photo = photo;
    }

    public ChallengeItem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public ArrayList<Long> getRepeat() {
        return repeat;
    }

    public void setRepeat(ArrayList<Long> repeat) {
        this.repeat = repeat;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
