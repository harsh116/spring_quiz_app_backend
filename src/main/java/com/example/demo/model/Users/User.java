/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.model.Users;

// directly linked with fields of table name 'users'
public class User {

    int id;
    String username;
    String password_hash;
    String user_type;

    public User(int id, String username, String password_hash, String user_type) {
        this.id = id;
        this.username = username;
        this.password_hash = password_hash;
        this.user_type = user_type;
    }

    public User(Object obj) {
        User usr = (User) obj;

        this.id = usr.id;
        this.username = usr.username;
        this.password_hash = usr.password_hash;
        this.user_type = usr.user_type;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password_hash=" + password_hash + ", user_type=" + user_type + '}';
    }

}
