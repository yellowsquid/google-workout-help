package com.example.testapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityText = (TextView) findViewById(R.id.exerciseName);
        timeText = (TextView) findViewById(R.id.timeValue);
        pBar = (ProgressBar) findViewById(R.id.determinateBar);


        // Enables Always-on
        setAmbientEnabled();


        newSport("StarJumps", 15000);
    }

    public void newSport(String name, final long workoutLength){
        activityText.setText(name);

        new CountDownTimer(workoutLength, 100) {

            public void onTick(long millisUntilFinished) {
                double timeLeft = (double) millisUntilFinished / 1000;

                //pBar.setProgress((int) ((L-millisUntilFinished)/L *100));
                timeText.setText("" + String.format("%.1f", timeLeft));
            }

            public void onFinish() {
                timeText.setText("done!");
            }
        }.start();
    }

}
