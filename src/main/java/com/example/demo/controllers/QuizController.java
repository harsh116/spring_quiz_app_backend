/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.controllers;

import com.example.demo.Helpers;
import com.example.demo.States;
import com.example.demo.dao.QuizRepo;
import com.example.demo.model.Quiz.QuizRequestBody;
import com.example.demo.model.Quiz.QuizResponse;
import com.example.demo.model.UserSession.UserSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@RestController
public class QuizController {

    @Autowired
    QuizRepo quizRepo;

    @PostMapping(path = "/addQuiz", consumes = "application/json")
    public ResponseEntity<QuizResponse> addQuiz(@RequestHeader String sessionToken, @RequestBody QuizRequestBody qr) {

        // Validation Step for checking if user is Admin type of not
        Optional<UserSession> userOptional = Helpers.findUserSession(States.userSessions, sessionToken);

        System.out.println("userSessions: " + States.userSessions.toString());
//        System.out.println("useroptional: " + userOptional.get());
        if (userOptional.isEmpty() || !userOptional.get().getUser().getUser_type().equals("Admin")) {
            return ResponseEntity.status(401).body(null);
        }

        try {

            QuizResponse quizResponse = quizRepo.addQuiz(qr);
            return ResponseEntity.ok().body(quizResponse);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    //    for userType 'user'
    // response in {technology : quizid : {quizid,name,technology, List<QuestionResponse>}, ... }
    @GetMapping("/getQuizzes")
    public ResponseEntity<Map<String, Map<Integer, QuizResponse>>> getQuizzesForUser(@RequestHeader String sessionToken) {

        System.out.println("in getquizzes");

        // Validation Step for checking if user is Admin type of not
        Optional<UserSession> userOptional = Helpers.findUserSession(States.userSessions, sessionToken);

        System.out.println("userSessions: " + States.userSessions.toString());
//        System.out.println("useroptional: " + userOptional.get());
        if (userOptional.isEmpty() || !userOptional.get().getUser().getUser_type().equals("User")) {
            return ResponseEntity.status(401).body(null);
        }

        try {

            Map<String, Map<Integer, QuizResponse>> userquizzes = quizRepo.getQuizzesForUser();
            return ResponseEntity.ok().body(userquizzes);
        } catch (Exception e) {
            System.out.println("getquiizzesforuser err message: " + e.getMessage());
            return ResponseEntity.status(500).body(null);

        }
    }

}
