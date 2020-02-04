package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends WearableActivity {
    String msg = "WatchApp";

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;


    private TextView statText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);


        statText = (TextView) findViewById(R.id.statusText) ;


        final Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //statText.setText("Hello World");
                openSportActivity();
            }
        });



    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(msg, "Started MainActivity");
//    }

    public void openSportActivity(){
        Intent intent = new Intent(this, SportActivity.class);
        // Use to pass byte array to sports
        //intent.putExtra("Circuit", )
        startActivity(intent);
    }





}


