package com.example.commonroom;

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

public class DeleteUser extends AppCompatActivity {

    private String collegeEmail, dorm;

    public static final int MAIN_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dorm = getIntent().getStringExtra("DormType");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_user);
        collegeEmail = getIntent().getStringExtra("cemail");
        System.out.println("COLLEGE EMAIL" +collegeEmail);
    }

    public void onDeleteClick(View v){
        deleteReq();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("DormType", dorm);
        startActivityForResult(i, MAIN_ID);
    }

    private void deleteReq() {

        String param = "collegeEmail=" + collegeEmail;
        String link = "http://10.0.2.2:3000/deleteAppUser?";
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