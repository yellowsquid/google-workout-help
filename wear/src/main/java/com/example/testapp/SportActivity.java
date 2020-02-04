package com.example.testapp;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Exercise;

import java.util.LinkedList;
import java.util.List;

public class SportActivity extends WearableActivity {
    String msg = "WatchApp";

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;

    private CountDownTimer startTimer;
    private CountDownTimer workoutTimer;

    private ImageView iconStill;
    private AnimationDrawable iconAnimated;

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



        iconStill = (ImageView) findViewById(R.id.sportsIcon);


        // Enables Always-on
        setAmbientEnabled();

        List<Exercise> c = new LinkedList<>();
        c.add(new Exercise(ExerciseType.SITUPS, 6));
        c.add(new Exercise(ExerciseType.SQUATS,  5));
        c.add(new Exercise(ExerciseType.REST, 10));
        c.add(new Exercise(ExerciseType.STAR_JUMPS, 5));
        c.add(new Exercise(ExerciseType.BURPEES, 10));
        c.add(new Exercise(ExerciseType.PUSHUPS, 8));
        c.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 8));



        Circuit cir = new Circuit(c, 2);

        workout(cir, 0, 0);
        //newSport("StarJumps", 10000);
//        newSport("Rest", 5000);
//        newSport("Press ups", 10000);
//        newSport("DONE", 5000);
//        finish();
    }


    public void workout(final Circuit cir, final int currentTask, final int currentLap){

        // Exits workout once done
        if (cir.getNumberOfExercises() <= currentTask ){
            if (cir.getLaps() <= currentLap + 1){
                finish();
            }else{
                workout(cir, 0, currentLap + 1);
            }

            return;
        }
        Log.d(msg, "Init Task:Lap " + currentTask + ":" + currentLap);


        final Exercise currentExercise = cir.getExercises().get(currentTask);
        final long workoutLength = (long) currentExercise.getTime() * 1000;


        // Count down timer
        final int interval = 100;
        workoutTimer = new CountDownTimer(workoutLength, interval) {

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
                Log.d(msg, "Finished Task " + currentTask);
                workout(cir, currentTask + 1, currentLap);

            }

        };

        // 3 second start timer
        final long startTimerLength = 3000;
        startTimer = new CountDownTimer(startTimerLength, interval) {

            public void onTick(long millisUntilFinished) {
                // Progress Bar
                double progress = ((double)(startTimerLength - millisUntilFinished)/startTimerLength) * 100;
                pBar.setProgress((int) progress);
                // Timer
                double timeLeft = (double) millisUntilFinished / 1000;
                timeText.setText("" + String.format("%.1f", timeLeft));
            }

            // Starts activity
            public void onFinish() {
                //set text to workout name
                activityText.setText(currentExercise.getExerciseType().getName());
                workoutTimer.start();
                Log.d(msg, "Started Task " + currentTask);

            }

        };

        // set test to countdown
        activityText.setText("NEXT: " + currentExercise.getExerciseType().getName() + " ["+currentExercise.getTime() + "s]");

        //Set Icon
        iconStill.setBackgroundResource(currentExercise.getExerciseType().getIcon());
        iconAnimated = (AnimationDrawable) iconStill.getBackground();
        iconAnimated.start();


        startTimer.start();
    }



    protected void onDestroy() {
        super.onDestroy();
        workoutTimer.cancel();
        startTimer.cancel();
        Log.d(msg, "SportActivity Destroyed");
    }

    public void hapticFeedback(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 800};
        //-1 - don't repeat
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1));
        }
    }
}
