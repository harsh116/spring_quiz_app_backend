/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.controllers;

import com.example.demo.Helpers;
import com.example.demo.States;
import com.example.demo.dao.UserRepo;
import com.example.demo.model.AuthenticateStatus;
import com.example.demo.model.UserSession.UserSession;
import com.example.demo.model.UserSession.UserSessionResponse;
import com.example.demo.model.Users.Answer;
import com.example.demo.model.Users.User;
import com.example.demo.model.Users.UserForm;
import com.example.demo.model.Users.User_Quiz;
import com.example.demo.model.Users.User_Quizzes;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

    @PostMapping(path = "/authenticate", consumes = "multipart/form-data")
    public ResponseEntity<UserSessionResponse> generateSessionKey(UserForm uf) {
        HashMap<Integer, String> statusMap = new HashMap<>();
        statusMap.put(0, "username not found");
        statusMap.put(1, "username found but wrong password");
        statusMap.put(2, "username and password both correct, userType is user");
        statusMap.put(3, "username and password both correct, userType is admin");

        System.out.println();

        try {
            AuthenticateStatus as = userRepo.authenticate(uf);
            User record = as.user;
            Integer status = as.status;

            if (status == 0) {
                return ResponseEntity.status(404).body(new UserSessionResponse(statusMap.get(status)));
            } else if (status == 1) {
                return ResponseEntity.status(401).body(new UserSessionResponse(statusMap.get(status)));
            } else if (status == 2) {
                String sessionToken = Helpers.generateRandomString(10);
                UserSessionResponse usr = new UserSessionResponse(sessionToken, "User", statusMap.get(status));
                UserSession userSession = new UserSession(record, sessionToken);
                States.userSessions.add(userSession);
                return ResponseEntity.ok().body(usr);

            } else if (status == 3) {
                String sessionToken = Helpers.generateRandomString(10);
                UserSessionResponse usr = new UserSessionResponse(sessionToken, "Admin", statusMap.get(status));

//                UserSession userSession = new UserSession(record, sessionToken);
//                States.userSessions.add(userSession);
                Helpers.addUserSession(States.userSessions, record, sessionToken);
                return ResponseEntity.ok().body(usr);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());

        }

        return ResponseEntity.status(500).body(null);

    }

    // for userType 'User'
    @PostMapping(path = "/addAnswers", consumes = "application/json")
    public ResponseEntity<User_Quiz> addAnswers(@RequestHeader String sessionToken, @RequestBody Answer answer) {
        System.out.println("sessiontoken: " + sessionToken);

        // Validation Step for checking if user is Admin type of not
        Optional<UserSession> userOptional = Helpers.findUserSession(States.userSessions, sessionToken);
        if (!userOptional.isPresent() || !userOptional.get().getUser().getUser_type().equals("User")) {
            return ResponseEntity.status(401).body(null);
        }

        try {

            User user = userOptional.get().getUser();

            User_Quiz user_Quiz = userRepo.addAnswer(user, answer);
            return ResponseEntity.ok().body(user_Quiz);
        } catch (Exception e) {
            System.out.println("addAnswers error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // for userType 'Admin'
    @GetMapping("/getAllUsersQuizzes")
    public ResponseEntity<Map<Integer, User_Quizzes>> getUsersWithQuizzes(@RequestHeader String sessionToken) {

        System.out.println("sessiontoken: " + sessionToken);

        // Validation Step for checking if user is Admin type of not
        Optional<UserSession> userOptional = Helpers.findUserSession(States.userSessions, sessionToken);
        if (!userOptional.isPresent() || !userOptional.get().getUser().getUser_type().equals("Admin")) {
            return ResponseEntity.status(401).body(null);
        }

        try {

            Map<Integer, User_Quizzes> user_quizzesMap = userRepo.getAllUserResponse();
            return ResponseEntity.ok().body(user_quizzesMap);

        } catch (Exception e) {
            System.out.println("getUsersWithQuizzes error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
