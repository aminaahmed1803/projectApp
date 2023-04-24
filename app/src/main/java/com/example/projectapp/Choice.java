package com.example.commonroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class Choice extends AppCompatActivity {
    public static final int ACCESS_ID = 4, DORMS_ID = 5, PERSRES_ID = 9;
    private String dorm, email;

    // onCreate method: Sets up the Activity when it is first created using the activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
        dorm = getIntent().getStringExtra("DormType");
        email = getIntent().getStringExtra("cemail");
    }

    // onStartButtonClick method: Used when start button is clicked (activity_main.xml)
    public void onAvailResClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentAR = new Intent(this, DormSelection.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentAR.putExtra("DormType", dorm);
        intentAR.putExtra("cemail", email);
        startActivityForResult(intentAR, DORMS_ID);
    }

    public void onPersResClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentPR = new Intent(this, PersResActivity.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentPR.putExtra("DormType", dorm);
        intentPR.putExtra("cemail", email);
        startActivityForResult(intentPR, PERSRES_ID);
    }

//    public void onDormButtonClick(View v) {
//        Intent intentD = new Intent(this, DormSelection.class);
//        intentD.putExtra("cemail", email);
//        intentD.putExtra("DormType", dorm);
//        startActivityForResult(intentD, DORMS_ID);
//    }

    // onHomePageButtonClick method: Used when the "Home Page" button is clicked (activity_quiz.xml)
    public void onHomePageButtonClick(View v) {
        Intent intentA = new Intent(this, Access.class);
        intentA.putExtra("cemail", email);
        intentA.putExtra("DormType", dorm);
        startActivityForResult(intentA, ACCESS_ID);
    }
}