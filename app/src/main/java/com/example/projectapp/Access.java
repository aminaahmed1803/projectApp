package com.example.commonroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Access extends AppCompatActivity {
    public static final int SETTINGS_ID = 6, CHOICE_ID = 7;
    private String dormType = "", email, password, firstName;
//    ArrayList<String> reserve = new ArrayList<>();

    // onCreate method: Sets up the Activity when it is first created using the activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access);

        email = getIntent().getStringExtra("cemail");
        dormType = getIntent().getStringExtra("DormType");

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
                        JSONArray ja = new JSONArray(response);

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = (JSONObject) ja.get(i);
                            String cE = jo.getString("collegeEmail");

                            if (email.equals(cE)) {
                                firstName = jo.getString("firstName");
                                done = true;

                                TextView fname = findViewById(R.id.welcome);
                                fname.setText("Welcome " + firstName + "!");
                                break;
                            }
                        }
                        if (done) break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //fname.setText("Welcome " + firstName + "!");

    // onStartButtonClick method: Used when start button is clicked (activity_main.xml)
    public void onSettingsButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentC = new Intent(this, UserProfileActivity.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentC.putExtra("DormType", dormType);
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("Res", reserve);
//        intentC.putExtras(bundle);
        intentC.putExtra("cemail", email);
        // Passes the Intent to the Activity, using the request code
        startActivityForResult(intentC, SETTINGS_ID);
    }

    public void onResButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentC = new Intent(this, Choice.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentC.putExtra("DormType", dormType);
        intentC.putExtra("UserName", firstName);
        intentC.putExtra("cemail", email);
        startActivityForResult(intentC, CHOICE_ID);
    }
}