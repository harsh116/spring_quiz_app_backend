package com.example.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.model.Users.User;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs,int rowNumber) throws SQLException{

        int id= rs.getInt("id");
        String username = rs.getString("username");
        String password_hash = rs.getString("password_hash");
//        int correct_option_number= rs.getInt("correct_option_number");
        String user_type = rs.getString("user_type");
//        boolean isDeleted = rs.getBoolean("isDeleted");

        User u = new User(id, username, password_hash, user_type);

        return u;



        
    }
}
