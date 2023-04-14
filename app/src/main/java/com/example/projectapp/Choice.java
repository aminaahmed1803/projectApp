package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * This class contains the methods for the Main Activity components.
 *
 * @gchoe
 */

public class Choice extends AppCompatActivity {
    public static final int AVAILRES_ID = 8, PERSRES_ID = 9;
    private String dorm;
    ArrayList<String> allRes = new ArrayList<>();

    // onCreate method: Sets up the Activity when it is first created using the activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);

        dorm = getIntent().getStringExtra("DormType");
        allRes = getIntent().getExtras().getStringArrayList("Res");
    }

    // onStartButtonClick method: Used when start button is clicked (activity_main.xml)
    public void onAvailResClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentAR = new Intent(this, AvailRoomsActivity.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentAR.putExtra("Dorm", dorm);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("allReserve", allRes);
        intentAR.putExtras(bundle);
        // Passes the Intent to the Activity, using the request code
        startActivityForResult(intentAR, AVAILRES_ID);
    }

//    public void onNextButtonClick(View v) {
//        // Creates an Intent using the current Activity and the class to be created
//        Intent intentC = new Intent(this, Choice.class);
//        // Arguments passed into the new Activity using key/value pairs in the Intent
//        intentC.putExtra("DormType", dormType);
//        // Passes the Intent to the Activity, using the request code
//        startActivityForResult(intentC, CHOICE_ID);
//    }

    public void onPersResClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentPR = new Intent(this, PersResActivity.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentPR.putExtra("Dorm", dorm);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("allReserve", allRes);
        intentPR.putExtras(bundle);
        // Passes the Intent to the Activity, using the request code
        startActivityForResult(intentPR, PERSRES_ID);
    }


    public void onHomePageButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentMA = new Intent(this, DormSelection.class);
        setResult(RESULT_OK, intentMA);
        finish();
    }

//    // onActivityResult method: Gets called when an Activity finishes
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        // The requestCode tells us which Activity just ended
//        switch (requestCode) {
//            case ROOM_ACTIVITY_ID:
//                // Gets the number of correct answers gotten in a row from the currently finished
//                // round of playing the math game. (Not the highest possible score from all plays,
//                // just for that one round of playing the game.)
//                int highestScore = intent.getIntExtra("HighestScore", 0);
//                // Displays the highest personal score as a pop-up so the Activity screen is not
//                // clustered.
//                Toast.makeText(this, "Highest Score: " + highestScore, Toast.LENGTH_LONG).show();
//                break;
//        }
//    }
}