package com.example.exchallenger.models.event;

import com.example.exchallenger.models.User;

public class LoginSuccessEvent {
    private User user;

    public LoginSuccessEvent(User newUser) {
        this.user = newUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
