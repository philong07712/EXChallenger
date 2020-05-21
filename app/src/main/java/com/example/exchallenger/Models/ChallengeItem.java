package com.example.exchallenger.Models;

public class ChallengeItem {
    public static final String SQUAT = "squat";
    public static final String PUSH_UP = "push up";
    public static final String PLANK = "plank";
    private String type;
    private int number;
    private int hour;
    private int minute;
    private String unit;
    private Integer[] repeat;

    public ChallengeItem() {
    }

    public ChallengeItem(String type, int number, int hour, int minute, Integer[] repeat, String unit) {
        this.type = type;
        this.number = number;
        this.hour = hour;
        this.minute = minute;
        this.repeat = repeat;
        this.unit = unit;
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

    public Integer[] getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer[] repeat) {
        this.repeat = repeat;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
