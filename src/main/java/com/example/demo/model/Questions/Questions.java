package com.example.demo.model.Questions;

// directly linked with fields of table name 'questions'
public class Questions {

    int id;
    String question;
    String options;
    int correct_option_number;
    String technology;
    boolean isDeleted;

    public Questions(int id, String question, String options, int correct_option_number, String technology,
            boolean isDeleted) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.correct_option_number = correct_option_number;
        this.technology = technology;
        this.isDeleted = isDeleted;
    }

    public Questions() {
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

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getCorrect_option_number() {
        return correct_option_number;
    }

    public void setCorrect_option_number(int correct_option_number) {
        this.correct_option_number = correct_option_number;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Questions [id=" + id + ", question=" + question + ", options=" + options + ", correct_option_number="
                + correct_option_number + ", technology=" + technology + ", isDeleted=" + isDeleted + "]";
    }

}
