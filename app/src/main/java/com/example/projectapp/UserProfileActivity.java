package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserProfileActivity extends AppCompatActivity {
    String collegeEmail, password, firstName, lastName, classYear, studentID;
    public static final int MAIN_ID = 1, EDIT_USER_ID = 10;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        collegeEmail = getIntent().getStringExtra("cemail");
        //System.out.println(collegeEmail);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    URL url = new URL("http://10.0.2.2:3000/users");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    Scanner in = new Scanner(url.openStream());
                    boolean done = false;
                    while (in.hasNext()) {
                        String response = in.nextLine();
                        System.out.println("response: " + response);
                        JSONArray ja = new JSONArray(response);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = (JSONObject) ja.get(i);
                            String cE = jo.getString("collegeEmail");
                            System.out.println("cE: " + cE);
                            if (cE.equals(collegeEmail)) {
                                System.out.println(jo);
                                firstName = jo.getString("firstName");
                                lastName = jo.getString("lastName");
                                password = jo.getString("password");
                                studentID = jo.getString("id");
                                classYear = jo.getString("classYear");
                                done = true;

                                TextView pwd = findViewById(R.id.password);
                                TextView fname = findViewById(R.id.firstname);
                                TextView lname = findViewById(R.id.lastname);
                                TextView year = findViewById(R.id.gradYear);
                                TextView email = findViewById(R.id.collegeEmail);
                                TextView id = findViewById(R.id.collegeID);

                                pwd.setText(password);
                                fname.setText(firstName);
                                lname.setText(lastName);
                                year.setText(classYear);
                                email.setText(collegeEmail);
                                id.setText(studentID);

                                break;
                            }
                        }
                        System.out.println("STAYYYY");
                        if (done) break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("test");
                }
            });
            System.out.println("test2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEditProfileClick(View v) {
        Intent i = new Intent(this, EditUserActivity.class);
        i.putExtra("College Email", collegeEmail);
        i.putExtra("Student ID", studentID);
        startActivityForResult(i, EDIT_USER_ID);
    }

    public void onLogOutClick(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, MAIN_ID);
    }
}