package com.example.asus.myapplication;




import android.support.annotation.NonNull;

import java.security.InvalidParameterException;

public class User {
    private static final String DELIMITER = "&";
    private String userName, password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    User(String userAsString){
        if(userAsString == null)
            throw new InvalidParameterException();
        String[] parts = userAsString.split(DELIMITER);
        if(parts.length != 2)
            throw new InvalidParameterException("must have two parts");
        this.userName = parts[0];
        this.password = parts[1];
    }

    String getUserName() {
        return userName;
    }


    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    public String toString() {
        return userName + DELIMITER + password;
    }
}