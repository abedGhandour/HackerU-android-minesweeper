package com.example.asus.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.asus.myapplication.MainActivity.PASSWORD;
import static com.example.asus.myapplication.MainActivity.PREFS;
import static com.example.asus.myapplication.MainActivity.USERNAME;

public class LoginActivity extends Activity {

    private EditText Username, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void goToSettingActivity(View view) {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }
    public void checkLoginContent(View view) {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        Username = findViewById(R.id.UserName);
        Password = findViewById(R.id.Password);
        String UsernameString = Username.getText().toString();
        String PasswordString = Password.getText().toString();
        if (UsernameString.isEmpty() || PasswordString.isEmpty()) {
            Toast.makeText(this, "All Data Is Mandatory", Toast.LENGTH_SHORT).show();
        } else {
            if (prefs.contains(USERNAME + UsernameString)) {
                if (prefs.contains(PASSWORD + PasswordString)) {
                    goToSettingActivity(view);
                } else {
                    Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Wrong Username", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
