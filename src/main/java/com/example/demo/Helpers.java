package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.model.Questions.Questions;
import com.example.demo.model.Questions.QuestionsForm;
import com.example.demo.model.Questions.QuestionsResponse;
import com.example.demo.model.UserSession.UserSession;
import com.example.demo.model.Users.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Helpers {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static QuestionsResponse QuestionFormToQuestionResponse(QuestionsForm qf, int id) {
        String question = qf.getQuestion();
        int correctOptionNumber = Integer.parseInt(qf.getCorrectOption());
        String technology = qf.getTechnology();
        List<String> options = new ArrayList<String>();

        if (qf.getOption1() != null && qf.getOption1().length() > 0) {
            options.add(qf.getOption1());
        }
        if (qf.getOption2() != null && qf.getOption2().length() > 0) {
            options.add(qf.getOption2());
        }

        if (qf.getOption3() != null && qf.getOption3().length() > 0) {
            options.add(qf.getOption3());
        }

        if (qf.getOption4() != null && qf.getOption4().length() > 0) {
            options.add(qf.getOption4());
        }

        QuestionsResponse qr = new QuestionsResponse(id, question, options, correctOptionNumber, technology);

        return qr;
    }

    public static Questions QuestionFormsToQuestions(QuestionsForm qf, int id) throws Exception {

        String question = qf.getQuestion();
        int correctOptionNumber = Integer.parseInt(qf.getCorrectOption());
        String technology = qf.getTechnology();
        List<String> options = new ArrayList<String>();

        if (qf.getOption1() != null && qf.getOption1().length() > 0) {
            options.add(qf.getOption1());
        }
        if (qf.getOption2() != null && qf.getOption2().length() > 0) {
            options.add(qf.getOption2());
        }

        if (qf.getOption3() != null && qf.getOption3().length() > 0) {
            options.add(qf.getOption3());
        }

        if (qf.getOption4() != null && qf.getOption4().length() > 0) {
            options.add(qf.getOption4());
        }

        String optionsJSON = objectMapper.writeValueAsString(options);

        Questions q = new Questions(id, question, optionsJSON, correctOptionNumber, technology, false);

        return q;
    }

    public static QuestionsResponse QuestionsToQuestionsResponse(Questions q) throws Exception {
        int id = q.getId();
        String question = q.getQuestion();
        int correctOptionNumber = q.getCorrect_option_number();
        String technology = q.getTechnology();

        List<String> options = objectMapper.readValue(q.getOptions(), objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));

        QuestionsResponse qr = new QuestionsResponse(id, question, options, correctOptionNumber, technology);
        return qr;

    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int size = characters.length();
        String randomString = "";
        for (int i = 0; i < length; i++) {
            randomString += characters.charAt((int) Math.floor(Math.random() * size));
        }

        return randomString;

    }

    public static Optional<UserSession> findUserSession(List<UserSession> userSessions, String sessionToken) {
        return userSessions.stream()
                .filter(userSession -> userSession.getSessionToken()
                .equals(sessionToken)).findFirst();
    }

    public static void addUserSession(List<UserSession> userSessions, User record, String sessionToken) {

        // checking in loop if there is same user already in session.
        // if yes then updating sessiontoken
        // else adding new userSession in record
        for (int i = 0; i < userSessions.size(); i++) {
            UserSession userSession = userSessions.get(i);
            if (userSession.getUser().getUsername().equals(record.getUsername())) {
                userSession.setSessionToken(sessionToken);
                userSessions.set(i, userSession);
                break;
            }
        }
        UserSession userSession = new UserSession(record, sessionToken);
        userSessions.add(userSession);
    }

}
