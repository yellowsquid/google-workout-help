package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;


    private TextView statText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

//        activityText = (TextView) findViewById(R.id.exerciseName);
//        timeText = (TextView) findViewById(R.id.timeValue);
//        pBar = (ProgressBar) findViewById(R.id.progressBar);

        statText = (TextView) findViewById(R.id.statusText) ;
        // Enables Always-on
        setAmbientEnabled();

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
//        statText.setText("Status");
//    }

    public void openSportActivity(){
        Intent intent = new Intent(this, SportActivity.class);
        startActivity(intent);
    }





}


