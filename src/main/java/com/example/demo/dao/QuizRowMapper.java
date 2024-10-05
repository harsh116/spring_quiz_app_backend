/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.dao;

import com.example.demo.model.Quiz.Quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class QuizRowMapper implements RowMapper<Quiz> {

    @Override
    public Quiz mapRow(ResultSet rs, int rowNumber) throws SQLException {

        int id = rs.getInt("id");
        String technology = rs.getString("technology");
        String name = rs.getString("name");
//        int correct_option_number= rs.getInt("correct_option_number");
//        String user_type = rs.getString("user_type");
//        boolean isDeleted = rs.getBoolean("isDeleted");

        Quiz q = new Quiz(id, technology, name);

        return q;

    }
}
