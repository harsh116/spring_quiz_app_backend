package com.example.demo.model.Questions;

import java.util.List;

public class QuestionsResponse {
    int id;
    
    String question;
   
    List<String> options;
    int correctOptionNumber;
    String technology;

    public QuestionsResponse() {
    }


    public QuestionsResponse(int id, String question, List<String> options, int correctOptionNumber,
            String technology) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.correctOptionNumber = correctOptionNumber;
        this.technology = technology;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public List<String> getOptions() {
        return options;
    }
    public void setOptions(List<String> options) {
        this.options = options;
    }
    public int getCorrectOptionNumber() {
        return correctOptionNumber;
    }
    public void setCorrectOptionNumber(int correctOptionNumber) {
        this.correctOptionNumber = correctOptionNumber;
    }
    public String getTechnology() {
        return technology;
    }
    public void setTechnology(String technology) {
        this.technology = technology;
    }
    
    @Override
    public String toString() {
        return "QuestionsResponse [id=" + id + ", question=" + question + ", options=" + options
                + ", correctOptionNumber=" + correctOptionNumber + ", technology=" + technology + "]";
    }

    

}
