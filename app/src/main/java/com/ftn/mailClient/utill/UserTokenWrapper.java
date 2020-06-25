package com.ftn.mailClient.utill;

import com.ftn.mailClient.model.User;

public class UserTokenWrapper {
    private User user;
    private String token;

    public UserTokenWrapper() {
        user = null;
        token = "";
    }

    public UserTokenWrapper(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
