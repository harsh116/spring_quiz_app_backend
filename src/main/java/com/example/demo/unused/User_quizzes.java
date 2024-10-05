/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.unused;

import com.example.demo.model.Quiz.QuizResponse;
import com.example.demo.model.Users.Answer;
import java.util.List;

public class User_quizzes {

    int user_id;
    String username;

    List<QuizResponse> quizzes;

    List<Answer> answers;

    public User_quizzes(int user_id, String username, List<QuizResponse> quizzes, List<Answer> answers) {
        this.user_id = user_id;
        this.username = username;
        this.quizzes = quizzes;
        this.answers = answers;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<QuizResponse> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<QuizResponse> quizzes) {
        this.quizzes = quizzes;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "User_quizzes{" + "user_id=" + user_id + ", username=" + username + ", quizzes=" + quizzes + ", answers=" + answers + '}';
    }

}
