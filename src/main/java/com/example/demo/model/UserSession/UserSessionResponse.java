/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.model.UserSession;


public class UserSessionResponse {
    public String sessionToken;
    public String userType;
    public String message;

    public UserSessionResponse(String sessionToken, String userType, String message) {
        this.sessionToken = sessionToken;
        this.userType = userType;
        this.message = message;
    }

    public UserSessionResponse(String message) {
        this.message = message;
    }


}
