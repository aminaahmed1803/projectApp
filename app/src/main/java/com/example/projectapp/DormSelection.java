package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * This class contains the methods for the Dorm Selection components.
 *
 * @gchoe
 */

public class DormSelection extends AppCompatActivity {
    public static final int CHOICE_ID = 7;
    private String dormType = "";
    ArrayList<String> reserve = new ArrayList<>();

    // onCreate method: Sets up the Activity when it is first created using the activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dorm_selection);

        // Creates a spinner using the difficulty_spinner Spinner from activity_main.xml
        Spinner spinner = (Spinner) findViewById(R.id.dorm_spinner);
        // Creates an ArrayAdapter using the string array "dorm_array" and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dorm_array, android.R.layout.simple_spinner_item);
        // Specifies the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applies the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // onItemSelected method: Finds what items was selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An item (dormType) was selected and can be retrieved
                dormType = (String)parent.getItemAtPosition(pos);
            }

            // onNothingSelected method: Finds if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback, did not use for this assignment
            }
        });
    }

    // onStartButtonClick method: Used when start button is clicked (activity_main.xml)
    public void onNextButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentC = new Intent(this, Choice.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentC.putExtra("DormType", dormType);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Res", reserve);
        intentC.putExtras(bundle);
        // Passes the Intent to the Activity, using the request code
        startActivityForResult(intentC, CHOICE_ID);
    }
}