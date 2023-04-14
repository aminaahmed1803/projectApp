package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {
    EditText email1, password;
    int MAIN_ID = 1, ACCESS_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    @Deprecated
    public void onClick(View v) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivityForResult(intent, MAIN_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onConnectButtonClick(View v) {
        email1 = (EditText) findViewById(R.id.email);
        String emailString = email1.getText().toString();
        //System.out.println("email: " + emailString);
        password = (EditText) findViewById(R.id.password);
        String passString = password.getText().toString();
        //System.out.println("password: " + passString);
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    URL url = new URL("http://10.0.2.2:3000/users");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    int count = 0;
                    while (in.hasNext()) {
                        String response = in.nextLine();
                        //System.out.println("response: " + response);
                        JSONArray ja = new JSONArray(response);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = (JSONObject) ja.get(i);
                            String cE = jo.getString("collegeEmail");
                            //System.out.println("cE: " + cE);
                            String cP = cP = jo.getString("password");
                            if (cE.equals(emailString)) {
                                System.out.println("here");

                                if (cP.equals(passString)) {
                                    count = 1;
                                    if (count == 1) {
                                        System.out.println(cE);
                                        Intent intent = new Intent(this, Access.class);
                                        intent.putExtra("College Email", cE);
                                        startActivityForResult(intent, ACCESS_ID);
                                    }
                                }
                            }
                        }
                    }

                    Looper.prepare();
                    if (count == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Email or Password is incorrect. Try Again!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 1030);
                        toast.show();
                    }
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}