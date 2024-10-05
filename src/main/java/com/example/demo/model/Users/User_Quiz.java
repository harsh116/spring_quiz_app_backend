/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.model.Users;

import com.example.demo.model.Questions.QuestionsResponse;
import java.util.List;

public class User_Quiz {

    // quizID
    int id;
    // quizname
    String name;

    List<QuestionsResponse> questions;
    List<Integer> selectedOptions;
    String score;

    public User_Quiz(int id, String name, List<QuestionsResponse> questions, List<Integer> selectedOptions, String score) {
        this.id = id;
        this.name = name;
        this.questions = questions;
        this.selectedOptions = selectedOptions;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuestionsResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsResponse> questions) {
        this.questions = questions;
    }

    public List<Integer> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<Integer> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "User_Quiz{" + "id=" + id + ", name=" + name + ", questions=" + questions + ", selectedOptions=" + selectedOptions + ", score=" + score + '}';
    }

}
