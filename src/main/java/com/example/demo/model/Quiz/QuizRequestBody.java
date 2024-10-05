package com.example.demo.model.Quiz;

import com.example.demo.model.Questions.QuestionsResponse;

import java.util.List;

public  class QuizRequestBody {
    String quizName;
    String technology;
    List<QuestionsResponse> questions;

    public QuizRequestBody(String quizName, String technology, List<QuestionsResponse> questions) {
        this.quizName = quizName;
        this.technology = technology;
        this.questions = questions;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public List<QuestionsResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsResponse> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "QuizRequestBody{" +
                "quizName='" + quizName + '\'' +
                ", technology='" + technology + '\'' +
                ", questions=" + questions +
                '}';
    }
}