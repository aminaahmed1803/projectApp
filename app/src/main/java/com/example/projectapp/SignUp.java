package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    int MAIN_ID = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
    }

    public void signUpFinish(View v) {
        EditText fn = (EditText) findViewById(R.id.firstName);
        EditText ln = (EditText) findViewById(R.id.lastName);
        EditText cID = (EditText) findViewById(R.id.collegeID);
        EditText y = (EditText) findViewById(R.id.classYear);
        EditText e = (EditText) findViewById(R.id.email);
        EditText p = (EditText) findViewById(R.id.password);
        EditText r = (EditText) findViewById(R.id.role);

        String firstName = "firstName=" + fn.getText().toString() + "&";
        String lastName = "lastName=" + ln.getText().toString() + "&";
        String id = "id=" + cID.getText().toString() + "&";
        String classYear = "classYear=" + y.getText().toString() + "&";
        String collegeEmail = "collegeEmail=" + e.getText().toString() + "&";
        String password = "password=" + p.getText().toString() + "&";
        String role = "role=" + r.getText().toString();

        String link = "http://10.0.2.2:3000/addUserAccount?" + firstName + lastName + id + classYear + collegeEmail + password + role;

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {

                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    // open connection and send HTTP request
                    try {
                        Scanner in = null;
                        conn.connect();

                        // now the response comes back
                        int responseCode = conn.getResponseCode();

                        // make sure the response has "200 OK" as the status
                        if (responseCode != 200) {
                            System.out.println("Unexpected status code: " + responseCode);
                        } else {
                            // made it here, we got a good response, let's read it
                            in = new Scanner(url.openStream());

                            while (in.hasNext()) {
                                // read the next line of the body of the response
                                String line = in.nextLine();

                                // the rest of this code assumes that the body contains JSON
                                // first, create the parser
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
        } finally {
            try {

            } catch (Exception f) {
            }
        }

        Intent intentMA = new Intent(this, MainActivity.class);
        startActivityForResult(intentMA, MAIN_ID);
    }
}