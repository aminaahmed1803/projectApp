package com.example.projectapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeleteDorm extends AppCompatActivity {
    String colEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_dorm);
        colEmail = getIntent().getStringExtra("College Email");
        Button dB = (Button) findViewById(R.id.deleteButton);

        dB.setOnClickListener(new View.OnClickListener() {
            @Override
            @Deprecated
            public void onClick(View v) {
                try {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute( () -> {
                        try {
                            URL url = new URL("http://10.0.2.2:3000/deleteRes");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("DELETE");
                            conn.connect();
                        } catch (Exception e) {

                        }
                    });
                } catch (Exception e) {

                }
            }
        });
    }
}