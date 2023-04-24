package com.example.commonroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class EditUserActivity extends AppCompatActivity {
    String collegeEmail, studentID, firstName, lastName, gradYear, password;

    public static final int ACCESS_ID = 4, USER_PROFILE_ID = 11, DELETE_PROFILE_ID = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        collegeEmail = getIntent().getStringExtra("cemail");
        System.out.println("College Email!!  " + collegeEmail);
        studentID = getIntent().getStringExtra("Student ID");
    }

    public void onSaveClick(View v) {
        EditText a = (EditText) findViewById(R.id.firstname);
        EditText b = (EditText) findViewById(R.id.lastname);
        EditText c = (EditText) findViewById(R.id.gradYear);
        EditText d = (EditText) findViewById(R.id.password);

        firstName = a.getText().toString();
        lastName = b.getText().toString();
        gradYear = c.getText().toString();

        password = d.getText().toString();

        if (password.isBlank() || gradYear.isBlank() || lastName.isBlank()  || firstName.isBlank() ){
            Toast.makeText(getApplicationContext(), "Please fill all information.", Toast.LENGTH_LONG).show();
        }
        else {
            //post command for editing user
            try {
                editUser();
            }catch (IOException ioe){
                System.out.println(ioe);
            }

            Intent i = new Intent(this, UserProfileActivity.class);
            i.putExtra("cemail", collegeEmail);
            startActivityForResult(i, USER_PROFILE_ID);
        }
    }

    private void editUser() throws IOException {
        String param = "firstName="+firstName + "&lastName="+lastName +"&id=" + studentID + "&classYear="+ gradYear + "&collegeEmail=" + collegeEmail+ "&password="+ password;
        String link = "http://10.0.2.2:3000/editUser?";
        System.out.println(param);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    URL url = new URL(link+param);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    try {
                        Scanner in = null;
                        conn.connect();

                        // now the response comes back
                        int responsecode = conn.getResponseCode();

                        // make sure the response has "200 OK" as the status
                        if (responsecode != 200) {
                            System.out.println("Unexpected status code: " + responsecode);
                        } else {
                            // made it here, we got a good response, let's read it
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

    public void onCancelClick(View v) {
        Intent i = new Intent(this, UserProfileActivity.class);
        i.putExtra("cemail", collegeEmail);
        startActivityForResult(i, USER_PROFILE_ID);
    }

    public void onHomePageButtonClick(View v) {
        Intent intentA = new Intent(this, Access.class);
        startActivityForResult(intentA, ACCESS_ID);
    }

    public void onDeleteProfileClick(View v){
        Intent i = new Intent(this, DeleteUser.class);
        i.putExtra("cemail", collegeEmail);
        startActivityForResult(i, DELETE_PROFILE_ID);
    }
}