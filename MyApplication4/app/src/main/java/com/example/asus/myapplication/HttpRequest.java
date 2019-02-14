package com.example.asus.myapplication;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

 class HttpRequest {

         static String httpGet(String spec){
            URL url;
            InputStream inputStream = null;
            HttpURLConnection connection = null;
            try{


                url = new URL(spec);
                connection = (HttpURLConnection) url.openConnection();
                connection.setUseCaches(false);
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                connection.connect();

                int responseCode = connection.getResponseCode();


                if(responseCode != 200){

                    return null;
                }
                inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                StringBuilder stringBuilder = new StringBuilder();
                int actuallyRead;
                while ((actuallyRead = inputStream.read(buffer)) != -1){
                    stringBuilder.append(new String(buffer, 0, actuallyRead));
                }
                return stringBuilder.toString();
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
            return null;
        }

}
