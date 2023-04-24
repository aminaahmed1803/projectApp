package com.example.commonroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AvailRoomsActivity extends AppCompatActivity {
    public static final int DORMS_ID = 5, ACCESS_ID = 4, CHOICE_ID = 7;
    private String dorm, selectedDorm, selectedRoom, email, output = "", choice, choiceRoom;
    private TextView textV;
    private List<String> allRoomSpinner = new ArrayList<>();
    public List<String> allRoomsInfo = new ArrayList<>();
    private int highScore = 0;

    // onCreate method: Sets up the Activity when it is first created using the activity_quiz.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avail_rooms);

        email = getIntent().getStringExtra("cemail");
        dorm = getIntent().getStringExtra("DormType");
        choiceRoom = getIntent().getStringExtra("RoomChoice");

        roomAppointmentMaker(dorm, choiceRoom);

        TextView dname = findViewById(R.id.dormInfo);

        if (dorm.equals("None")) {
            dname.setText("Selected dorm: All");
        } else {
            dname.setText("Selected dorm: " + dorm + "!");
        }

        Spinner spinner = (Spinner) findViewById(R.id.res_spinner);

        // Creates an ArrayAdapter using the arraylist allDorms and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allRoomSpinner);
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

    public void roomView(String dormNameInput, String roomNameInput) {
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

                        int num = 1;

                        for (int i = 0; i < jsArr.length(); i++) {
                            JSONObject jsObj = (JSONObject) jsArr.get(i);

                            String roomN = jsObj.getString("name");
                            int capac = jsObj.getInt("capacity");
                            String dormName = jsObj.getString("dorm");
                            int floor = jsObj.getInt("floor");
                            String timeS = jsObj.getString("timeSlots");
                            String dateSlots = jsObj.getString("dateSlots");
                            boolean available = jsObj.getBoolean("avail");

                            dormName = dormName.toLowerCase().replaceAll("\\s+","");
                            String inputDorm = dorm.toLowerCase().replaceAll("\\s+","");

                            roomN = roomN.toLowerCase().replaceAll("\\s+","");
                            String inputRoom = choiceRoom.toLowerCase().replaceAll("\\s+","");

                            String[] time = timeS.split(",");
                            String outputArr = "";

                            if (num > 1) {
                                output += "\n";
                            }

                            if ((available == true) &&  (inputDorm.equals(dormName) || inputDorm.equals("none")) &&
                                (inputRoom.equals(roomN) || inputRoom.equals("none"))) {
                                output += "\t\t\t\tSlot " + num + "    Room name: " + roomN +
                                    "\n\t\t\t\t               Capacity: " + capac +
                                    "\n\t\t\t\t               Floor: " + floor +
                                    "\n\t\t\t\t               Date: " + dateSlots +
                                    "\n\t\t\t\t               Start Times (2 hours): " +
                                    "\n\t\t\t\t               " + timeS;

                                for (String t : time) {
                                    String oneRoomInfo = "roomName=" + roomN + "&dorm=" + dormName +
                                        "&floor=" + String.valueOf(floor) + "&date=" + dateSlots +
                                        "&time=" + t;
                                    allRoomsInfo.add(oneRoomInfo);
                                    outputArr = "Slot " + num + " - Room name = " + roomN + "  Time = " + t;
                                    allRoomSpinner.add(outputArr);
                                }

                                num++;
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

    public void roomAppointmentMaker(String dorm, String room) {
        selectedDorm = dorm.toLowerCase().replaceAll("\\s+","");
        selectedRoom = room.toLowerCase().replaceAll("\\s+","");
        roomView(selectedDorm, selectedRoom);
    }

    public void onReserveButtonClick(View v) {
        try {
            addReservation();
            Toast toast = Toast.makeText(getApplicationContext(),"Reservation successful!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 1030);
            toast.show();
        } catch (IOException ioe){
            System.out.println(ioe);
        }
    }

    private void addReservation() throws IOException {
        choice = choice.replaceAll("\\s+", "");
        String[] choiceParts = choice.split("=");
        String choiceRoom = choiceParts[1].replaceAll("Time", "");
        String choiceTime = choiceParts[2];

        System.out.println(choiceRoom + "    " + choiceTime);

        String param = "";

        for (String roomInfo : allRoomsInfo) {
            if (roomInfo.contains(choiceRoom) && roomInfo.contains(choiceTime)) {
                param = roomInfo + "&userEmail=" + email;
            }
        }

        String urlLink = "http://10.0.2.2:3000/addUserReservation?" + param;

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            executor.execute(() -> {
                try {
                    URL url = new URL(urlLink);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    try {
                        Scanner in = null;
                        conn.connect();

                        // Now the response comes back
                        int responseCode = conn.getResponseCode();

                        // Makes sure the response has "200 OK" as the status
                        if (responseCode != 200) {
                            System.out.println("Unexpected status code: " + responseCode);
                        } else {
                            // Made it here, we got a good response, let's read it
                            in = new Scanner(url.openStream());

                            while (in.hasNext()) {
                                String line = in.nextLine();
                            }
                        }
                    } catch (java.net.ConnectException c) {
                        c.printStackTrace();
                    }
                } catch (Exception a) {
                    a.printStackTrace();
                }
            });

            executor = Executors.newSingleThreadExecutor();
            try {
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        } finally {
            try {

            } catch (Exception f) {

            }
        }
    }

    public void onBackButtonClick(View v) {
        Intent intentC = new Intent(this, DormSelection.class);
        intentC.putExtra("DormType", dorm);
        intentC.putExtra("cemail", email);
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("Res", allRes);
//        intentC.putExtras(bundle);
        startActivityForResult(intentC, DORMS_ID);
    }

    public void onResButtonClick(View v) {
        Intent intentD = new Intent(this, Choice.class);
        intentD.putExtra("cemail", email);
        intentD.putExtra("DormType", dorm);
        startActivityForResult(intentD, CHOICE_ID);
    }

    // onHomePageButtonClick method: Used when the "Home Page" button is clicked (activity_quiz.xml)
    public void onHomePageButtonClick(View v) {
        Intent intentA = new Intent(this, Access.class);
        intentA.putExtra("cemail", email);
        intentA.putExtra("DormType", dorm);
        startActivityForResult(intentA, ACCESS_ID);
    }
}