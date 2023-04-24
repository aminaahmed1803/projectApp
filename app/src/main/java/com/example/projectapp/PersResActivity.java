package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * This class contains the methods for the Room Activity's components.
 *
 * @gchoe
 */

public class PersResActivity extends AppCompatActivity {
    public static final int MAIN_ID = 1, CHOICE_ID = 7;
    private String allRes;
    private TextView textV;
    public ArrayList<String> reserve = new ArrayList<>();

    // onCreate method: Sets up the Activity when it is first created using the activity_quiz.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pers_res);
        // part of old code that doesn't work anymore (delete next two lines)
        roomName = getIntent().getStringExtra("Room Name"); // 1
        time = getIntent().getStringExtra("Time"); // 2

        reserve = getIntent().getExtras().getStringArrayList("allReserve");
        resView(reserve);
    }

    public void resView(ArrayList reservations) {
        textV = findViewById(R.id.roomInfo);

        for (int i = 0; i < reservations.size(); i++) {
            allRes += reservations.get(i) + "\n";
        }

        textV.setText(allRes);
    }

    public void onBackButtonClick() {
        Intent intentB = new Intent(this, Choice.class);
        startActivityForResult(intentB, CHOICE_ID);
    }

    // onHomePageButtonClick method: Used when the "Home Page" button is clicked (activity_quiz.xml)
    public void onHomePageButtonClick(View v) {
        Intent intentMA = new Intent(this, DormSelection.class);
        startActivityForResult(intentMA, MAIN_ID);
    }

// old code that doesnt work anymore
public void onDeleteButtonClick(View v) {
    deleteRes();
    Intent i = new Intent(this, PageAfterDeleteRes.class); // need to make a new class
    startActivityForResult(i, MAIN_ID);
}

private void deleteRes() {
    String paramRoom = "Room Name=" + roomName;
    String paramTime = "Time=" + time;
    String urlLink = "http://10.0.2.2:3000/deleteRes?";
    System.out.println(paramRoom);
    System.out.println(paramTime);

    try {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URL url = new URL(urlLink+paramRoom+paramTime);
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
}
