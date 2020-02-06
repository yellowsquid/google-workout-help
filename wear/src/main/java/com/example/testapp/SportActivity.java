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
import com.example.testapp.shared.ExerciseType;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SportActivity extends WearableActivity {
    public static final long EXERCISE_COUNTDOWN = 3000L;
    public static final long TICK_RATE = 100L;
    public static final int MILLIS_IN_SECOND = 1000;

    String msg = "WatchApp";

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;

    private CountDownTimer startTimer;
    private CountDownTimer workoutTimer;

    private ImageView iconStill;

    // Run on activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(msg, "SportActivity Created");
        super.onCreate(savedInstanceState);

        // Which view to use
        setContentView(R.layout.activity_sports);

        // Find elements in the view
        activityText = findViewById(R.id.exerciseName);
        timeText = findViewById(R.id.timeValue);
        pBar = findViewById(R.id.progressBar);

        iconStill = findViewById(R.id.sportsIcon);

        // Enables Always-on
        setAmbientEnabled();

        List<Exercise> c = new LinkedList<>();
        c.add(new Exercise(ExerciseType.SITUPS, 6));
        c.add(new Exercise(ExerciseType.SQUATS, 5));
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

    protected void onDestroy() {
        super.onDestroy();
        workoutTimer.cancel();
        startTimer.cancel();
        Log.d(msg, "SportActivity Destroyed");
    }

    public void workout(final Circuit cir, final int currentTask, final int currentLap) {

        // Exits workout once done
        if (cir.getNumberOfExercises() <= currentTask) {
            if (cir.getLaps() <= currentLap + 1) {
                finish();
            } else {
                workout(cir, 0, currentLap + 1);
            }

            return;
        }
        Log.d(msg, "Init Task:Lap " + currentTask + ":" + currentLap);

        final Exercise currentExercise = cir.getExercises().get(currentTask);
        final long workoutLength = (long) currentExercise.getTime() * MILLIS_IN_SECOND;

        // Count down timer
        workoutTimer = new CountDownTimer(workoutLength, TICK_RATE) {

            public void onTick(long millisUntilFinished) {
                // Progress Bar

                long remaining = workoutLength - millisUntilFinished;
                int progress = (int) ((100 * remaining) / workoutLength);
                pBar.setProgress(progress);

                // Timer
                double timeLeft = ((double) millisUntilFinished) / MILLIS_IN_SECOND;
                timeText.setText(String.format(Locale.getDefault(), "%.1f", timeLeft));
            }

            // Ends activity
            public void onFinish() {
                pBar.setProgress(100);
                hapticFeedback();
                Log.d(msg, "Finished Task " + currentTask);
                workout(cir, currentTask + 1, currentLap);
            }
        };

        final String exerciseName = currentExercise.getName();

        startTimer = new CountDownTimer(EXERCISE_COUNTDOWN, TICK_RATE) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Progress Bar
                long remaining = EXERCISE_COUNTDOWN - millisUntilFinished;
                int progress = (int) ((100 * remaining) / EXERCISE_COUNTDOWN);
                pBar.setProgress(progress);

                // Timer
                double timeLeft = ((double) millisUntilFinished) / MILLIS_IN_SECOND;
                timeText.setText(String.format(Locale.getDefault(), "%.1f", timeLeft));
            }

            // Starts activity
            @Override
            public void onFinish() {
                //set text to workout name
                activityText.setText(exerciseName);
                workoutTimer.start();
                Log.d(msg, "Started Task " + currentTask);
            }
        };

        // set test to countdown
        int duration = currentExercise.getTime();
        String next = getResources().getString(R.string.next_activity, exerciseName, duration);
        activityText.setText(next);

        //Set Icon
        iconStill.setBackgroundResource(currentExercise.getIcon());
        final AnimationDrawable iconAnimated = (AnimationDrawable) iconStill.getBackground();
        iconAnimated.start();

        startTimer.start();
    }

    public void hapticFeedback() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 800};
        //-1 - don't repeat
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1));
        }
    }
}
