package com.example.demo.model.Users;

import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public class Answer {

    int quizID;
    List<Integer> selectedOptionNumbersArray;

    public Answer(int quizID, List<Integer> selectedOptionNumbersArray) {
        this.quizID = quizID;
        this.selectedOptionNumbersArray = selectedOptionNumbersArray;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public List<Integer> getSelectedOptionNumbersArray() {
        return selectedOptionNumbersArray;
    }

    public void setSelectedOptionNumbersArray(List<Integer> selectedOptionNumbersArray) {
        this.selectedOptionNumbersArray = selectedOptionNumbersArray;
    }

    @Override
    public String toString() {
        return "Answers{" + "quizID=" + quizID + ", selectedOptionNumbersArray=" + selectedOptionNumbersArray + '}';
    }

}
