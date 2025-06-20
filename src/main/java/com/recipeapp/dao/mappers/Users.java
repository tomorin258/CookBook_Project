package com.recipeapp.dao.mappers;

import java.io.Serializable;
/**
 * Represents a users entity.
 * Each Users entry contains information such as user ID, user name and password.
 * Maps to the 'users' table in the database.
 * 
 * @author: Pan Zitao
 */

public class Users implements Serializable {

    private int userId;
    private String userName;
    private String password;

    /**
     * Default constructor for the Users class.
     */
    public Users() {

    }
    /**
     * Constructs a Users object with all fields.  
     * @param userId The ID of the user (primary key).
     * @param userName The name of the user.
     * @param password The password of the user.
     * */
    public Users(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}