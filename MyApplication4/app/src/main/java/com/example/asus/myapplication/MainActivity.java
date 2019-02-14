package com.example.asus.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import java.util.Set;

public class MainActivity extends Activity {

    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static final String PREFS = "prefs";
    public static final String USERS = "users";

    private Users users;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        users = Users.getUsers();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (prefs.contains(USERS)) {
            Set<String> usersAsString = prefs.getStringSet(USERS, null);
            users.loadUsers(usersAsString);
        }
    }
    public void goToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void goToSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}

