package com.example.testapp;

import android.graphics.drawable.Animatable;
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
    private static final long EXERCISE_COUNTDOWN = 3000L;
    private static final long TICK_RATE = 100L;
    private static final long MILLIS_IN_SECOND = 1000L;

    private static final String MESSAGE = "WatchApp";
    private static final long[] VIBRATION_PATTERN = {0, 500, 50, 800};

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;
    private CountDownTimer startTimer;
    private CountDownTimer workoutTimer;
    private ImageView iconStill;

    // Run on activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MESSAGE, "SportActivity Created");
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

        List<Exercise> exercises = new LinkedList<>();
        exercises.add(new Exercise(ExerciseType.SITUPS, 6));
        exercises.add(new Exercise(ExerciseType.SQUATS, 5));
        exercises.add(new Exercise(ExerciseType.REST, 10));
        exercises.add(new Exercise(ExerciseType.STAR_JUMPS, 5));
        exercises.add(new Exercise(ExerciseType.BURPEES, 10));
        exercises.add(new Exercise(ExerciseType.PUSHUPS, 8));
        exercises.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 8));

        Circuit cir = new Circuit(exercises, 2);

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
        Log.d(MESSAGE, "SportActivity Destroyed");
    }

    private void workout(Circuit cir, int currentTask, int currentLap) {
        // Exits workout once done
        if (cir.getNumberOfExercises() <= currentTask) {
            if (cir.getLaps() <= currentLap + 1) {
                finish();
            } else {
                workout(cir, 0, currentLap + 1);
            }

            return;
        }

        if (BuildConfig.DEBUG) {
            Log.d(MESSAGE, String.format("Init Task:Lap %d:%d", currentTask, currentLap));
        }

        Exercise currentExercise = cir.getExercises().get(currentTask);
        long workoutLength = currentExercise.getDuration() * MILLIS_IN_SECOND;

        // Count down timer
        workoutTimer = new CountDownTimer(workoutLength, TICK_RATE) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Progress Bar

                long remaining = workoutLength - millisUntilFinished;
                //noinspection NumericCastThatLosesPrecision
                int progress = (int) ((100 * remaining) / workoutLength);
                pBar.setProgress(progress);

                // Timer
                double timeLeft = ((double) millisUntilFinished) / MILLIS_IN_SECOND;
                timeText.setText(String.format(Locale.getDefault(), "%.1f", timeLeft));
            }

            // Ends activity
            @Override
            public void onFinish() {
                pBar.setProgress(100);
                hapticFeedback();

                if (BuildConfig.DEBUG) {
                    Log.d(MESSAGE, String.format("Finished Task %d", currentTask));
                }

                workout(cir, currentTask + 1, currentLap);
            }
        };

        String exerciseName = currentExercise.getName();

        startTimer = new CountDownTimer(EXERCISE_COUNTDOWN, TICK_RATE) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Progress Bar
                long remaining = EXERCISE_COUNTDOWN - millisUntilFinished;
                //noinspection NumericCastThatLosesPrecision
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

                if (BuildConfig.DEBUG) {
                    Log.d(MESSAGE, String.format("Started Task %d", currentTask));
                }
            }
        };

        // set test to countdown
        int duration = currentExercise.getDuration();
        String next = getResources().getString(R.string.next_activity, exerciseName, duration);
        activityText.setText(next);

        //Set Icon
        iconStill.setBackgroundResource(currentExercise.getIcon());
        Animatable iconAnimated = (Animatable) iconStill.getBackground();
        iconAnimated.start();

        startTimer.start();
    }

    private void hapticFeedback() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (vibrator != null) {
            // -1 means don't repeat
            vibrator.vibrate(VibrationEffect.createWaveform(VIBRATION_PATTERN, -1));
        }
    }
}
