package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SportActivity extends WearableActivity {
    String msg = "WatchApp";

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;


    // Run on activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(msg, "SportActivity Created");
        super.onCreate(savedInstanceState);

        // Which view to use
        setContentView(R.layout.activity_sports);

        // Find elements in the view
        activityText = (TextView) findViewById(R.id.exerciseName);
        timeText = (TextView) findViewById(R.id.timeValue);
        pBar = (ProgressBar) findViewById(R.id.progressBar);



        newSport("StarJumps", 10000);
//        newSport("Rest", 5000);
//        newSport("Press ups", 10000);
//        newSport("DONE", 5000);
//        finish();
    }




    public void newSport(String name, final long workoutLength){
        // Change sports name
        activityText.setText(name);

        // Count down timer
        final int interval = 100;
        new CountDownTimer(workoutLength, interval) {

            public void onTick(long millisUntilFinished) {
                // Progress Bar
                double progress = ((double)(workoutLength - millisUntilFinished)/workoutLength) * 100;
                pBar.setProgress((int) progress);

                // Timer
                double timeLeft = (double) millisUntilFinished / 1000;
                timeText.setText("" + String.format("%.1f", timeLeft));
            }

            // Ends activity
            public void onFinish() {
                pBar.setProgress(100);
                hapticFeedback();
                finish();
             //   timeText.setText("done!");

            }
        }.start();
    }


    public void hapticFeedback(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 300};
        //-1 - don't repeat
        vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1));
    }
}
