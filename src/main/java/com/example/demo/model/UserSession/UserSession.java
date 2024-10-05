/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.model.UserSession;

import com.example.demo.model.Users.User;

public class UserSession {

    private User user;
    private String sessionToken;

    public UserSession(User user, String sessionToken) {
        this.user = new User(user);
        this.sessionToken = sessionToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String toString() {
        return "UserSession{" + "user=" + user + ", sessionToken=" + sessionToken + '}';
    }


}
