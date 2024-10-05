/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.dao;

import com.example.demo.Constants;
import com.example.demo.Helpers;
import com.example.demo.model.AuthenticateStatus;
import com.example.demo.model.Questions.QuestionsResponse;
import com.example.demo.model.Quiz.Quiz;
import com.example.demo.model.Users.Answer;
import com.example.demo.model.Users.UserForm;
import com.example.demo.model.Users.User;
import com.example.demo.model.Users.User_Quiz;
import com.example.demo.model.Users.User_Quizzes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.security.crypto.bcrypt.BCrypt;

@Repository
public class UserRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    QuizRepo QuizRepo;

    @Autowired
    ObjectMapper objectMapper;

    public Optional<User> getRecordByUsername(String username) {
        String sql = String.format("select * from %s where username=?", Constants.USER_TABLE_NAME);
        List<User> records = jdbcTemplate.query(sql, new UserRowMapper(), username);

        return records.stream().findFirst();
    }

    // Score is in string form 'correctAnswers/total number of questions'
    // TODO correctoptions, selectoptions out of bound
    public String calculateScore(Answer answer) throws Exception {
        Quiz quiz = QuizRepo.findQuizByID(answer.getQuizID()).get();

        System.out.println("quiz: " + quiz.toString());

        List<Integer> selectedOptions = answer.getSelectedOptionNumbersArray();

        List<QuestionsResponse> questionsResponses = QuizRepo.getAllQuestionsFromQuiz(answer.getQuizID());

        List<Integer> correctOptions = questionsResponses.stream().map(questionsResponse -> questionsResponse.getCorrectOptionNumber()).toList();

        System.out.println("selectedOptions: " + selectedOptions);
        System.out.println("correctOptions: " + correctOptions);

        int correctAnswers = 0;
        for (int i = 0; i < correctOptions.size(); i++) {
            int correctOption = correctOptions.get(i);
            int selectedOption = selectedOptions.get(i);
            if (correctOption == selectedOption) {
                correctAnswers++;
            }

        }
        int totalNumberOfQuestions = questionsResponses.size();

        String score = String.format("%d/%d", correctAnswers, totalNumberOfQuestions);

        return score;
    }

    public AuthenticateStatus authenticate(UserForm uf) {

        System.out.println("in autheticate");

        String username = uf.getUsername();
        String password = uf.getPassword();

        Optional<User> actualRecordNullable = getRecordByUsername(username);

//        jdbcTemplate.queryforOb
        if (actualRecordNullable.isPresent()) {
            System.out.println("username found");
            User actualRecord = actualRecordNullable.get();
            System.out.println(actualRecord.toString());
//            if (actualRecord.getPassword_hash().equals(password)) {
            // checking password hash with password sent by user
            if (BCrypt.checkpw(password, actualRecord.getPassword_hash())) {

                System.out.println("password matches");

                // if both are correct then 2
                int status = actualRecord.getUser_type().equals("Admin") ? 3 : 2;
                return new AuthenticateStatus(status, actualRecord);
//                String randStrng=Helpers.generateRandomString(10);
            } else {
                // if only 1 is correct then 1
                int status = 1;
                return new AuthenticateStatus(status, actualRecord);
            }
        }

        // if none of them are correct then 0
        int status = 0;
        return new AuthenticateStatus(status, null);

    }

    public User_Quiz addAnswer(User user, Answer answer) throws JsonProcessingException, Exception {

//        List<Integer> correctoptions = QuizRepo.findQuizByID(answer.getQuizID()).get().get
        String sql = String.format("insert into %s (userID,quizID,selectedOptions,score) values (?,?,?,?)", Constants.USER_QUIZZES_TABLE_NAME);
        String selectedOptionNumbersArrayJSON = objectMapper.writeValueAsString(answer.getSelectedOptionNumbersArray());
        String score = calculateScore(answer);
        jdbcTemplate.update(sql, user.getId(), answer.getQuizID(), selectedOptionNumbersArrayJSON, score);

        Quiz quiz = QuizRepo.findQuizByID(answer.getQuizID()).get();

        String quizName = quiz.getName();
        List<QuestionsResponse> questionsResponses = QuizRepo.getAllQuestionsFromQuiz(answer.getQuizID());

        User_Quiz user_quiz = new User_Quiz(answer.getQuizID(), quizName, questionsResponses, answer.getSelectedOptionNumbersArray(), score);

        return user_quiz;

    }

    // {userID : {id: userID, name: username ,quizes : {quizid : User_Quiz, ...}
//                }
//     }             
    public Map<Integer, User_Quizzes> getAllUserResponse() throws JsonProcessingException, Exception {
//        System.out.println("");

        String sql = String.format("SELECT uq.userID,uq.quizID, \n"
                + "u.username,  uq.selectedOptions, uq.score, q.technology \n"
                + "FROM  %s u join %s uq \n"
                + "on u.id =uq.userID \n"
                + "JOIN  %s q on q.id = uq.quizID; ", Constants.USER_TABLE_NAME, Constants.USER_QUIZZES_TABLE_NAME,
                Constants.QUIZZES_TABLE_NAME);
        List<Map<String, Object>> records = jdbcTemplate.queryForList(sql);

        Map<Integer, User_Quizzes> users_quizzes_Map = new HashMap<>();

        for (Map<String, Object> record : records) {

            int userID = ((BigInteger) record.get("userID")).intValue();
            int quizID = ((BigInteger) record.get("quizID")).intValue();

            String username = (String) record.get("username");
            String selectedOptionsJSON = (String) record.get("selectedOptions");
            String score = (String) record.get("score");
            String technology = (String) record.get("technology");
//            String question = (String) record.get("question");
//            String options = (String) record.get("options");
//            int correct_option_number = (int) record.get("correct_option_number");

            List<Integer> selectedOptions = objectMapper.readValue(selectedOptionsJSON,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));

            // empty quizmap as there may be no {quizID : User_quiz} at first
            Map<Integer, User_Quiz> quizzes = new HashMap<>();
            User_Quizzes user_Quizzes = new User_Quizzes(userID, username, quizzes);

            // if this map contains current userid then extracting User_Quizzes
            if (users_quizzes_Map.containsKey(userID)) {
                user_Quizzes = users_quizzes_Map.get(userID);
            }

            // extracting quizzes map from user_Quizzes
            quizzes = user_Quizzes.getQuizzes();

            // if quizID is not present then only add quiz to it
            // this is to prevent user from giving same quiz multple time
            // user can still give quiz technically but it wont be shown
            if (!quizzes.containsKey(quizID)) {
                String quizname = QuizRepo.findQuizByID(quizID).get().getName();
                List<QuestionsResponse> questions = QuizRepo.getAllQuestionsFromQuiz(quizID);
                User_Quiz user_Quiz = new User_Quiz(quizID, quizname, questions, selectedOptions, score);
                //2nd parameter User_Quiz
                quizzes.put(quizID, user_Quiz);

                // as new quiz is added to map so setting quizzes of user_Quizzes
                user_Quizzes.setQuizzes(quizzes);
            }

            users_quizzes_Map.put(userID, user_Quizzes);

        }

        return users_quizzes_Map;

    }
}
