package com.example.asus.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.asus.myapplication.MainActivity.PASSWORD;
import static com.example.asus.myapplication.MainActivity.PREFS;
import static com.example.asus.myapplication.MainActivity.USERNAME;

public class SignUpActivity extends Activity {
    private static final int REQUEST_CODE = 123;
    public static final String MY_FILE_TXT = "my_file.txt";
    private EditText userName, password1, password2, fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }


    public void signUp(View view) {
        Button button = findViewById(R.id.signUpBtn);
        Toast.makeText(this, "please wait...", Toast.LENGTH_SHORT).show();
        button.setEnabled(false);
        fullName = findViewById(R.id.fullName);
        userName = findViewById(R.id.userName);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        String fullNameString = fullName.getText().toString();
        String userNameString = userName.getText().toString();
        String password1String = password1.getText().toString();
        String password2String = password2.getText().toString();

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (userNameString.isEmpty() || password1String.isEmpty() || password2String.isEmpty() || fullNameString.isEmpty()) {
            Toast.makeText(this, "all data is mandatory!", Toast.LENGTH_SHORT).show();
        } 
        else {
            if (password1String.equals(password2String)) {
                if (prefs.contains(USERNAME+userNameString)) {
                    Toast.makeText(this, "User Name Taken", Toast.LENGTH_LONG).show();
                } 
                else {
                    //User(userName, password);
                    editor.putString(USERNAME+userNameString ,userNameString);
                    editor.putString(PASSWORD+password1String, password1String);

                    // editor.putInt("key_name2", 1);        // Saving integer
                    // editor.putString("key_name5", "string value");  // Saving string
                    editor.apply(); // commit changes
                    Toast.makeText(this, "Saved New User", Toast.LENGTH_SHORT).show();
                    goToSettingActivity(view);
                }
            }
            else {
                Toast.makeText(this, "passwords aren't identical", Toast.LENGTH_SHORT).show();
            }
        }
        button.setEnabled(true);
    }

    public void goToSettingActivity(View view) {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

}