package com.example.commonroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PersResActivity extends AppCompatActivity {
    public static final int ACCESS_ID = 4, CHOICE_ID = 7, AVAILRES_ID = 8, PADR_ID = 12;
    private String output = "", email, choice, dorm;
    private List<String> allResSpinner = new ArrayList<>();
    private TextView textV;

    // onCreate method: Sets up the Activity when it is first created using the activity_quiz.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pers_res);
        email = getIntent().getStringExtra("cemail");
        dorm = getIntent().getStringExtra("DormType");

        // Create/add intent here
        resView();

        Spinner spinner = (Spinner) findViewById(R.id.res_spinner);

        // Creates an ArrayAdapter using the arraylist allDorms and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allResSpinner);
        // Specifies the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // onItemSelected method: Finds what items was selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An item (dormType) was selected and can be retrieved
                choice = (String)parent.getItemAtPosition(pos);
                System.out.println(choice);
            }

            // onNothingSelected method: Finds if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback, did not use for this assignment
            }
        });
    }

    public void resView() {
        textV = findViewById(R.id.resInfo);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    URL url = new URL("http://10.0.2.2:3000/reservation");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    Scanner in = new Scanner(url.openStream());

                    while (in.hasNext()) {
                        String response = in.nextLine();
                        JSONArray ja = new JSONArray(response);

                        int num = 1;
                        String outputArr = "";

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = (JSONObject) ja.get(i);

                            if (jo.has("userEmail")) {
                                String uEmail = jo.getString("userEmail");

                                System.out.println("Object email: " + uEmail);
                                System.out.println("input email: " + email);

                                if (email.equals(uEmail)) {
                                    if (num > 1) {
                                        output += "\n";
                                    }

                                    String roomN = jo.getString("roomName");
                                    String dormN = jo.getString("dorm");
                                    int floor = jo.getInt("floor");
                                    String date = jo.getString("date");
                                    String time = jo.getString("time");

                                    output += "\t\t\t\tReservation " + num + "    Dorm: " + dormN +
                                            "\n\t\t\t\t\t               Room name: " + roomN +
                                            "\n\t\t\t\t\t               Floor: " + floor +
                                            "\n\t\t\t\t\t               Date: " + date +
                                            "\n\t\t\t\t\t               Start Time (2 hours): " +
                                            "\n\t\t\t\t\t               " + time;

                                    outputArr = "Reservation " + num + " - " + roomN;
                                    allResSpinner.add(outputArr);
                                    num++;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Waits 2 seconds before setting output as textV for user interface stylization
            executor.awaitTermination(2, TimeUnit.SECONDS);
            textV.setText(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteRes() {
        //String[] choiceParts = choice.split("-");
        //String roomName = choiceParts[1].replaceAll(" ", "");
        String param = "userEmail=" + email;
        String urlLink = "http://10.0.2.2:3000/deleteResApp?" + param;

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    URL url = new URL(urlLink);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    try {
                        Scanner sc = null;
                        conn.connect();

                        // response comes back
                        int responseCode = conn.getResponseCode();

                        // make sure response status is 200 OK
                        if (responseCode != 200) {
                            System.out.println("Unexpected status code: " + responseCode);
                        } else {
                            // if we made it here, we got a good response to read
                            sc = new Scanner(url.openStream());

                            while (sc.hasNext()) {
                                String line = sc.nextLine();
                            }
                        }
                    } catch (java.net.ConnectException ce) {
                        ce.printStackTrace();
                    }
                } catch (Exception a) {
                    a.printStackTrace();
                }
            });
            executor = Executors.newSingleThreadExecutor();
            try {
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
                throw new RuntimeException(ie);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onBackButtonClick(View v) {
        Intent intentC = new Intent(this, Choice.class);
        intentC.putExtra("cemail", email);
        intentC.putExtra("DormType", dorm);
        startActivityForResult(intentC, CHOICE_ID);
    }

    public void onDeleteButtonClick(View v) {
        deleteRes();
        Intent intentMA = new Intent(this, PageAfterDeleteRes.class);
        intentMA.putExtra("DormType", dorm);
        intentMA.putExtra("cemail", email);
        startActivityForResult(intentMA, PADR_ID);
    }

    public void onMoreInfoButtonClick(View v) { //FIX
        Intent intentMA = new Intent(this, AvailRoomsActivity.class);
        intentMA.putExtra("cemail", email);
        intentMA.putExtra("DormType", dorm);
        startActivityForResult(intentMA, AVAILRES_ID);
    }

    // onHomePageButtonClick method: Used when the "Home Page" button is clicked (activity_quiz.xml)
    public void onHomePageButtonClick(View v) {
        Intent intentA = new Intent(this, Access.class);
        intentA.putExtra("cemail", email);
        intentA.putExtra("DormType", dorm);
        startActivityForResult(intentA, ACCESS_ID);
    }
}