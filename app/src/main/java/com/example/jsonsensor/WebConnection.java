package com.example.jsonsensor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebConnection implements Runnable{

    private String json;

    public WebConnection(String json) {
        this.json = json;
    }

    @Override
    public void run() {
        try {
            URL url = new URL("https://android-sensor-api.herokuapp.com/api/v1/sensor/uploadSensor");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setUseCaches(false);
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setFixedLengthStreamingMode(json.length());

            try (OutputStream os = con.getOutputStream()) {
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(json);
                osw.flush();
                osw.close();
            }
        }
        catch (IOException e){

        }

    }
}
