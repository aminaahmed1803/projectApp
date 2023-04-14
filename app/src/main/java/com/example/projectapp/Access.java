package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class contains the methods for the Access Selection components.
 *
 * @gchoe
 */

public class Access extends AppCompatActivity {
    public static final int DORMS_ID = 5, SETTINGS_ID = 6;
    private String dormType = "", email;
    ArrayList<String> reserve = new ArrayList<>();

    // onCreate method: Sets up the Activity when it is first created using the activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access);

        email = getIntent().getStringExtra("College Email");
    }

    // onStartButtonClick method: Used when start button is clicked (activity_main.xml)
    public void onSettingsButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentC = new Intent(this, UserProfileActivity.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentC.putExtra("DormType", dormType);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Res", reserve);
        intentC.putExtras(bundle);
        intentC.putExtra("cemail", email);
        // Passes the Intent to the Activity, using the request code
        startActivityForResult(intentC, SETTINGS_ID);
    }

    public void onDormsButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentC = new Intent(this, DormSelection.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentC.putExtra("DormType", dormType);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Res", reserve);
        intentC.putExtras(bundle);
        // Passes the Intent to the Activity, using the request code
        startActivityForResult(intentC, DORMS_ID);
    }
}