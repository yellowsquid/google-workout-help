package com.example.testapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.testapp.shared.Circuit;

import java.util.List;

public class WaitingActivity extends AppCompatActivity {
    public final static String CIRCUIT_ID = "com.example.testapp.CIRCUIT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        // Retrieve circuit and token.
        Circuit circuit = (Circuit) getIntent().getSerializableExtra(CIRCUIT_ID);

        // FIXME: get token and list of nodes (maybe via server)
        LiveData<List> connectedNodes;
        String accessToken;

        // Init UI
    }
}
