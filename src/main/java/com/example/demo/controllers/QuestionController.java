package com.example.demo.controllers;

import com.example.demo.Helpers;
import com.example.demo.States;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.QuestionRepo;
import com.example.demo.dao.UserRepo;
import com.example.demo.model.ResponseStatus;
import com.example.demo.model.Questions.QuestionsForm;
import com.example.demo.model.Questions.QuestionsResponse;
import com.example.demo.model.UserSession.UserSession;
import com.example.demo.model.Users.UserForm;
import com.example.demo.model.UserSession.UserSessionResponse;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class QuestionController {

    @Autowired
    QuestionRepo questionRepo;


    // expects questionForm as body and atleast one header as 'sessionToken'
    // will only authorize this if userType is 'Admin'
    @PostMapping(path = "/addQuestion", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<QuestionsResponse> addQuestions(@RequestHeader String sessionToken, QuestionsForm qf) {

        System.out.println("sessiontoken: " + sessionToken);

        // Validation Step for checking if user is Admin type of not
        Optional<UserSession> userOptional = Helpers.findUserSession(States.userSessions, sessionToken);
        if (!userOptional.isPresent() || !userOptional.get().getUser().getUser_type().equals("Admin")) {
            return ResponseEntity.status(401).body(null);
        }

        try {

            // boolean addQuestionStatus = questionRepo.insertQuestion(qf);
            QuestionsResponse qr = questionRepo.insertQuestion(qf);


            if (qr.getId() > 0) {

                ResponseStatus responseBody = new ResponseStatus(200, "Record added");

                // return ResponseEntity.ok().body(responseBody);
                return ResponseEntity.status(200).body(qr);
            } else {
                ResponseStatus responseBody = new ResponseStatus(400, "Record not added");
                // return ResponseEntity.status(500).body(responseBody);
                return ResponseEntity.status(500).body(qr);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
            ResponseStatus responseBody = new ResponseStatus(400, String.format("error message : %s", e.getMessage()));
            return ResponseEntity.status(500).body(null);
        }

    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<Optional<List<QuestionsResponse>>> getAllQuestions(@RequestHeader String sessionToken) {

        // Validation Step for checking if user is Admin type of not
        Optional<UserSession> userOptional = Helpers.findUserSession(States.userSessions, sessionToken);

        System.out.println("userSessions: " + States.userSessions.toString());
//        System.out.println("useroptional: " + userOptional.get());
        if (!userOptional.isPresent() || !userOptional.get().getUser().getUser_type().equals("Admin")) {
            return ResponseEntity.status(401).body(null);
        }

        try {

            Optional<List<QuestionsResponse>> qrList = questionRepo.getAll();

            if (qrList.isPresent()) {

                // System.out.println("qrlist: "+qrList.get().toString());
                // ResponseStatus responseBody= new ResponseStatus(0, "",qrList);
                return ResponseEntity.ok().body(qrList);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PatchMapping(path = "/editQuestion/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Optional<QuestionsResponse>> editQuestion(@RequestHeader String sessionToken, QuestionsForm qf, @PathVariable int id) {

        // Validation Step for checking if user is Admin type of not
        Optional<UserSession> userOptional = Helpers.findUserSession(States.userSessions, sessionToken);
        if (!userOptional.isPresent() || !userOptional.get().getUser().getUser_type().equals("Admin")) {
            return ResponseEntity.status(401).body(null);
        }

        System.out.println("qf: " + qf.toString());
        System.out.println("id: " + id);

        try {
            Optional<QuestionsResponse> responseBody = questionRepo.updateQuestionById(qf, id);
            if (responseBody == null) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.ok().body(responseBody);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public ResponseEntity<Optional<QuestionsResponse>> deleteQuestion(@RequestHeader String sessionToken, @PathVariable int id) {

        // Validation Step for checking if user is Admin type of not
        Optional<UserSession> userOptional = Helpers.findUserSession(States.userSessions, sessionToken);
        if (!userOptional.isPresent() || !userOptional.get().getUser().getUser_type().equals("Admin")) {
            return ResponseEntity.status(401).body(null);
        }

        try {

            Optional<QuestionsResponse> responseBody = questionRepo.deleteQuestionById(id);
            if (responseBody == null) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.ok().body(responseBody);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }

    }

}
