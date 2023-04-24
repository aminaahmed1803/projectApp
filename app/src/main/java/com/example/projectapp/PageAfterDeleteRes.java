package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PageAfterDeleteRes extends AppCompatActivity {
    public static final int MAIN_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pageafterdeleteres);

    }
    public void onBackButton(View v) {
        Intent intent = new Intent(this, PersResActivity.class);
        startActivityForResult(intent, MAIN_ID);
    }
}
