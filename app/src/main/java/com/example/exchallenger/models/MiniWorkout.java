package com.example.exchallenger.models;

public class MiniWorkout {
    private String anim, introduction, miniWorkoutID, name, photo, type;
    private int point, rep, time;

    public MiniWorkout() {
    }

    public MiniWorkout(String anim, String introduction, String miniWorkoutID, String name, String photo, String type, int point, int rep, int time) {
        this.anim = anim;
        this.introduction = introduction;
        this.miniWorkoutID = miniWorkoutID;
        this.name = name;
        this.photo = photo;
        this.type = type;
        this.point = point;
        this.rep = rep;
        this.time = time;
    }

    public MiniWorkout(String anim, String introduction, String name, String photo, String type, int point, int rep, int time) {
        this.anim = anim;
        this.introduction = introduction;
        this.name = name;
        this.photo = photo;
        this.type = type;
        this.point = point;
        this.rep = rep;
        this.time = time;
    }

    public String getAnim() {
        return anim;
    }

    public void setAnim(String anim) {
        this.anim = anim;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMiniWorkoutID() {
        return miniWorkoutID;
    }

    public void setMiniWorkoutID(String miniWorkoutID) {
        this.miniWorkoutID = miniWorkoutID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
