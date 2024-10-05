/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.model.Users;

import java.util.Map;

public class User_Quizzes {

    int id;
    String name;

    Map<Integer, User_Quiz> quizzes;

    public User_Quizzes(int id, String name, Map<Integer, User_Quiz> quizzes) {
        this.id = id;
        this.name = name;
        this.quizzes = quizzes;
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

    public Map<Integer, User_Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(Map<Integer, User_Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    @Override
    public String toString() {
        return "User_Quizzes{" + "id=" + id + ", name=" + name + ", quizzes=" + quizzes + '}';
    }

}
