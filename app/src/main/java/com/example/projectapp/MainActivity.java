package com.example.commonroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    int SIGNUP_ID = 2, LOGIN_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSignUpClick(View v) {
        Intent i = new Intent(this, SignUp.class);

        //pass intent to activity, using specified request code
        startActivityForResult(i, SIGNUP_ID);
        setResult(RESULT_OK);
        finish();
    }

    public void onLogInClick(View v) {
        Intent i = new Intent(this, Login.class);

        //pass intent to activity, using specified request code
        startActivityForResult(i, LOGIN_ID);
        setResult(RESULT_OK);
        finish();
    }
}