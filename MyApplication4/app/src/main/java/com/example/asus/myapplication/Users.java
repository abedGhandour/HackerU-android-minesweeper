package com.example.asus.myapplication;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class Users {

    private static Users users;
    private Map<String, User> userMap;

    private Users(){
        //singleton - prevent creation of an instance
        userMap = new HashMap<>();
    }
    static Users getUsers(){
        if(users == null)
            users = new Users();
        return users;
    }


    void loadUsers(Set<String> usersAsString){
        for(String userAsString : usersAsString){
            User user = new User(userAsString);
            Log.d("ABED", String.valueOf(user));
            userMap.put(user.getUserName(), user);
        }
    }
}
