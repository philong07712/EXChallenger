package com.example.exchallenger.Models.event;

import com.example.exchallenger.Models.User;

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
