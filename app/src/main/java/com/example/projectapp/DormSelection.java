package com.example.commonroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class DormSelection extends AppCompatActivity {
    public static final int ACCESS_ID = 4, AVAILRES_ID = 8;
    private String dormType = "", email, roomChoice;
    private List<String> allRoomSpinner = new ArrayList<>();
//    ArrayList<String> reserve = new ArrayList<>();

    // onCreate method: Sets up the Activity when it is first created using the activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dorm_selection);

        email = getIntent().getStringExtra("cemail");
        dormType = getIntent().getStringExtra("DormType");

        populateRoomSpinner();

        // Creates a spinner using the difficulty_spinner Spinner from activity_main.xml
        Spinner spinnerDorm = (Spinner) findViewById(R.id.dorm_spinner);
        Spinner spinnerRoom = (Spinner) findViewById(R.id.room_spinner);
        // Creates an ArrayAdapter using the string array "dorm_array" and a default spinner layout
        ArrayAdapter<CharSequence> adapterDorm = ArrayAdapter.createFromResource(this,
                R.array.dorm_array, android.R.layout.simple_spinner_item);
        // Specifies the layout to use when the list of choices appears
        adapterDorm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applies the adapter to the spinner
        spinnerDorm.setAdapter(adapterDorm);

        spinnerDorm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Creates an ArrayAdapter using the arraylist allDorms and a default spinner layout
        ArrayAdapter<String> adapterRoom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allRoomSpinner);
        // Specifies the layout to use when the list of choices appears
        adapterRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(adapterRoom);

        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // onItemSelected method: Finds what items was selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An item (dormType) was selected and can be retrieved
                roomChoice = (String)parent.getItemAtPosition(pos);
            }

            // onNothingSelected method: Finds if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback, did not use for this assignment
            }
        });
    }

    public void populateRoomSpinner() {
        allRoomSpinner.add("None");
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    URL url = new URL("http://10.0.2.2:3000/rooms");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner scan = new Scanner(url.openStream());

                    while (scan.hasNext()) {
                        String line = scan.nextLine();
                        JSONArray jsArr = new JSONArray(line);

                        int num = 1;

                        for (int i = 0; i < jsArr.length(); i++) {
                            JSONObject jsObj = (JSONObject) jsArr.get(i);

                            String roomN = jsObj.getString("name");
                            String roomName = roomN.toLowerCase().replaceAll("\\s+","");
                            String outputArr = roomN;
                            allRoomSpinner.add(outputArr);
                        }
                    }
                } catch (Exception e) {
                    // An issue with above URL connection
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            // An issue with executor
            e.printStackTrace();
        }
    }

    // onStartButtonClick method: Used when start button is clicked (activity_main.xml)
    public void onNextButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentC = new Intent(this, AvailRoomsActivity.class);
        // Arguments passed into the new Activity using key/value pairs in the Intent
        intentC.putExtra("DormType", dormType);
        intentC.putExtra("RoomChoice", roomChoice);
        intentC.putExtra("cemail", email);
        startActivityForResult(intentC, AVAILRES_ID);
    }

    public void onHomePageButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentA = new Intent(this, Access.class);
        intentA.putExtra("DormType", dormType);
        intentA.putExtra("cemail", email);
        // Passes the Intent to the Activity, using the request code
        startActivityForResult(intentA, ACCESS_ID);
    }
}