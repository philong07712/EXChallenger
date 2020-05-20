package com.example.exchallenger.Models;

import com.google.firebase.firestore.FieldValue;

import java.util.List;

public class Group {
    private String groupKey, key, name, photo;
    private List<String> members;
    private Object timeStamp;

    public Group() {
    }

    public Group(String groupKey, String key, String name, String photo, List<String> members) {
        this.groupKey = groupKey;
        this.key = key;
        this.name = name;
        this.photo = photo;
        this.timeStamp = FieldValue.serverTimestamp();
        this.members = members;
    }

    public Group(Group group) {
        this.groupKey = group.groupKey;
        this.key = group.key;
        this.name = group.name;
        this.photo = group.photo;
        this.timeStamp = group.timeStamp;
        this.members = group.members;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
