package com.example.asus.myapplication;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CalculatorThread extends Thread{
    public static final String BASE_URL = "http://192.168.10.176:8080/bigBalagn_war_exploded/bestever";
    public static final String TAG = "ABED";
    private int num1, num2;
    private String action;
    private CalculatorThreadListener listener;

    public CalculatorThread(int num1, int num2, String action, CalculatorThreadListener listener) {
        this.num1 = num1;
        this.num2 = num2;
        this.action = "add";
        this.listener = listener;
    }


    @Override
    public void run() {
        URL url = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try{
            url = new URL(BASE_URL + "?action=" + action + "&num1=" + num1 + "&num2=" + num2);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "response code: " + responseCode);
            if(responseCode != 200){
                return;
            }
            inputStream = connection.getInputStream();
            byte[] buffer = new byte[64];
            int actuallyRead = inputStream.read(buffer);
            if(actuallyRead != -1) {
                String responseAsString = new String(buffer, 0, actuallyRead);
                try{
                    int result = Integer.valueOf(responseAsString);
                    if (listener != null) {
                        listener.onResult(result);
                    }
                }catch (Exception ex){

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public interface CalculatorThreadListener{
        void onResult(int result);
    }
}
