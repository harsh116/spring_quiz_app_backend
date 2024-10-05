package com.example.demo.model.Questions;

public class QuestionsForm{
    String question;
    String option1;
    String option2;
    String option3;
    String option4;
    String correctOption;
    String technology;
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getOption1() {
        return option1;
    }
    public void setOption1(String option1) {
        this.option1 = option1;
    }
    public String getOption2() {
        return option2;
    }
    public void setOption2(String option2) {
        this.option2 = option2;
    }
    public String getOption3() {
        return option3;
    }
    public void setOption3(String option3) {
        this.option3 = option3;
    }
    public String getOption4() {
        return option4;
    }
    public void setOption4(String option4) {
        this.option4 = option4;
    }
    public String getCorrectOption() {
        return correctOption;
    }
    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }
    public String getTechnology() {
        return technology;
    }
    public void setTechnology(String technology) {
        this.technology = technology;
    }
    @Override
    public String toString() {
        return "QuestionsForm [question=" + question + ", option1=" + option1 + ", option2=" + option2 + ", option3="
                + option3 + ", option4=" + option4 + ", correct_option_numbeString=" + correctOption
                + ", technology=" + technology + "]";
    }

    
}