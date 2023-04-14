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
}