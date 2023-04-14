package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * This class contains the methods for the Room Activity's components.
 *
 * @gchoe
 */

public class AvailRoomsActivity extends AppCompatActivity {
    public static final int MAIN_ID = 1, CHOICE_ID = 7;
    private String dorm, selectedDorm, output = "", choice;
    private TextView textV;
    private List<String> allRooms = new ArrayList<>();
    public List<String> reservations = new ArrayList<>();
    private int highScore = 0;

    // onCreate method: Sets up the Activity when it is first created using the activity_quiz.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avail_rooms);

        reservations = getIntent().getExtras().getStringArrayList("allReserve");
        dorm = getIntent().getStringExtra("Dorm");
        roomAppointmentMaker(dorm);

        Spinner spinner = (Spinner) findViewById(R.id.res_spinner);

        // Creates an ArrayAdapter using the arraylist allDorms and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allRooms);
        // Specifies the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        // Creates an ArrayAdapter using the string array "dorm_array" and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.allRooms, android.R.layout.simple_spinner_item);
//        // Specifies the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applies the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // onItemSelected method: Finds what items was selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An item (dormType) was selected and can be retrieved
                choice = (String)parent.getItemAtPosition(pos);
            }

            // onNothingSelected method: Finds if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback, did not use for this assignment
            }
        });
    }

    public void roomView(String dormNameInput) {
        textV = findViewById(R.id.roomInfo);

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

                        for (int i = 0; i < jsArr.length(); i++) {
                            JSONObject jsObj = (JSONObject) jsArr.get(i);

                            String dormName = jsObj.getString("dorm");
                            int capac = jsObj.getInt("capacity");
                            int floor = jsObj.getInt("floor");
                            String timeS = jsObj.getString("timeSlots");
                            boolean available = jsObj.getBoolean("avail");

                            System.out.println("1BLAHBLAHBLAH" + dormNameInput);

                            String compName = dormName.toLowerCase().replaceAll("\\s+","");
                            System.out.println("HO" + compName);
                            System.out.println("SO" + dormNameInput);
                            if (compName.equals(dormNameInput)) {
                                System.out.println("STOPPPPP " + dormNameInput);
                                output += "Room name: " + dormName + "\nCapacity: " + capac + "\nFloor: "
                                    + floor + "\nTime Slots: " + timeS + "\nAvailability: " + available + "\n\n";
                                String outputArr = "Room name: " + dormName + "\nCapacity: " + capac + "\nFloor: "
                                        + floor + "\nTime Slots: " + timeS + "\nAvailability: " + available + "\n\n";
                                allRooms.add(outputArr);
                            }
                        }
                    }
                } catch (Exception e) {
                    // An issue with above URL connection
                    e.printStackTrace();
                    output = e.toString();
                }
            });

            // Waits 2 seconds before setting output as textV for user interface stylization
            executor.awaitTermination(2, TimeUnit.SECONDS);
            textV.setText(output);


        } catch (Exception e) {
            // An issue with executor
            e.printStackTrace();
            textV.setText(e.toString());
        }
    }

    // questionMaker method:
    // Randomly generates two numbers for the math question based on the selected difficulty. Does
    // so by randomly generating each number one digit at a time. First finds the ones place and
    // tens place for the first number then the ones place and the tens place for the second number.
    public void roomAppointmentMaker(String dorm) {
        selectedDorm = dorm.toLowerCase().replaceAll("\\s+","");
        roomView(selectedDorm);

//        if (dorm.equals("Brecon")) {
//        } else if (dorm.equals("Denbigh")) {
//        } else if (dorm.equals("New Dorm")) {
//        } else if (dorm.equals("Erdman")) {
//        } else if (dorm.equals("Merion")) {
//        } else if (dorm.equals("Pembroke East")) {
//        } else if (dorm.equals("Pembroke West")) {
//        } else if (dorm.equals("Radnor")) {
//        } else if (dorm.equals("Rhoads North")) {
//        } else if (dorm.equals("Rhoads South")) {
//        } else if (dorm.equals("Rockefeller")) {
//        }
    }

    public void onReserveButtonClick() {
        reservations.add(choice);
        Toast.makeText(getApplicationContext(), "Reservation made!", Toast.LENGTH_SHORT).show();
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