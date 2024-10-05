package com.example.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.model.Questions.Questions;

public class QuestionRowMapper implements RowMapper<Questions> {
    @Override
    public Questions mapRow(ResultSet rs,int rowNumber) throws SQLException{

        int id= rs.getInt("id");
        String question = rs.getString("question");
        String options = rs.getString("options");
        int correct_option_number= rs.getInt("correct_option_number");
        String technology = rs.getString("technology");
        boolean isDeleted = rs.getBoolean("isDeleted");

        Questions q = new Questions(id,question,options,correct_option_number,technology,isDeleted);

        return q;



        
    }
}
