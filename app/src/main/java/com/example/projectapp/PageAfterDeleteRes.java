package com.example.commonroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PageAfterDeleteRes extends AppCompatActivity {
    public static final int PERSRES_ID = 9;
    private String email, dorm;

    // onCreate method: Sets up the Activity when it is first created using the activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_after_delete_res);

        email = getIntent().getStringExtra("cemail");
        dorm = getIntent().getStringExtra("DormType");
    }

    public void onBackPersButtonClick(View v) {
        // Creates an Intent using the current Activity and the class to be created
        Intent intentC = new Intent(this, PersResActivity.class);
        intentC.putExtra("cemail", email);
        intentC.putExtra("DormType", dorm);
        startActivityForResult(intentC, PERSRES_ID);
    }
}
