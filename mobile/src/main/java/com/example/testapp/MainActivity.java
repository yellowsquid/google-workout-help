package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static final String CIRCUIT_MESSAGE_PATH = "/circuit_path_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void waitClicked(View v) throws ExecutionException, InterruptedException, IOException {
        Intent intent = new Intent(this, WaitingActivity.class);
        startActivity(intent);
    }

    public void editClicked(View v) {
        Intent intent = new Intent(this, EditActivity.class);

        startActivity(intent);
    }
}
