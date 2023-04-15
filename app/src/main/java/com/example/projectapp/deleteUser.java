package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class deleteUser extends AppCompatActivity {

    private String collegeEmail;

    public static final int MAIN_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_profile);
        collegeEmail = getIntent().getStringExtra("cemail");
    }

    public void onDeleteClick(View v){
        deleteReq();
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, MAIN_ID);
    }

    private void deleteReq() {

        String param = "collegeEmail=" + collegeEmail;
        String link = "http://10.0.2.2:3000/deleteUser?";
        System.out.println(param);
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    URL url = new URL(link+param);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    try {
                        Scanner in = null;
                        conn.connect();

                        // now the response comes back
                        int responsecode = conn.getResponseCode();

                        // make sure the response has "200 OK" as the status
                        if (responsecode != 200) {
                            System.out.println("Unexpected status code: " + responsecode);
                        } else {
                            // made it here, we got a good response, let's read it
                            in = new Scanner(url.openStream());

                            while (in.hasNext()) {
                                String line = in.nextLine();
                            }
                        }
                    } catch (java.net.ConnectException c) {
                        c.printStackTrace();
                    }
                } catch (Exception a) {
                    a.printStackTrace();
                }
            });
            executor = Executors.newSingleThreadExecutor();
            try {
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
