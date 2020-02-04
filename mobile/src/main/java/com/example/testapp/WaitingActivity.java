package com.example.testapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class WaitingActivity extends AppCompatActivity {
    public final static String ACCESS_TOKEN_ID = "com.example.testapp.ACCESS_TOKEN_ID";
    public final static String CIRCUIT_ID = "com.example.testapp.CIRCUIT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
    }
}
