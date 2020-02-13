package uk.ac.cam.cl.alpha.workout.wear;

import android.content.Intent;
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

import java.io.IOException;
import java.util.Locale;

import uk.ac.cam.cl.alpha.workout.BuildConfig;
import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.Serializer;

public class SportActivity extends WearableActivity {
    static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.wear.CIRCUIT_ID";
    private static final long EXERCISE_COUNTDOWN = 5L;
    private static final String TAG = "SportActivity";
    private static final long[] VIBRATION_PATTERN = {0, 500, 50, 800};
    private TextView activityText;
    private TextView timeText;
    private ProgressBar progress;
    private CountDownTimer startTimer;
    private CountDownTimer workoutTimer;
    private ImageView iconStill;

    // Run on activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "SportActivity Created");
        super.onCreate(savedInstanceState);

        // Which view to use
        setContentView(R.layout.activity_sports);

        // Find elements in the view
        activityText = findViewById(R.id.exerciseName);
        timeText = findViewById(R.id.timeValue);
        progress = findViewById(R.id.progressBar);

        iconStill = findViewById(R.id.sportsIcon);

        // Enables Always-on
        setAmbientEnabled();

        Intent intent = getIntent();
        Circuit circuit;
        byte[] serial = intent.getByteArrayExtra(CIRCUIT_ID);
        if (serial != null) {
            try {
                circuit = (Circuit) Serializer.deserialize(serial);
                workout(circuit, 0, 0);
            } catch (IOException | ClassNotFoundException e) {
                Log.e(TAG, "Failed to deserialize the circuit");
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        workoutTimer.cancel();
        startTimer.cancel();
        Log.d(TAG, "SportActivity Destroyed");
    }

    private void workout(Circuit circuit, int task, int lap) {

        // TODO: make a circuit iterable instead?
        // Exits workout once done
        if (task >= circuit.getExerciseCount()) {
            if (lap >= circuit.getLaps()) {
                finish();
            } else {
                workout(circuit, 0, lap + 1);
            }

            return;
        }

        Exercise exercise = circuit.getExercise(task);
        int duration = exercise.getDuration();
        String exerciseName = getResources().getString(exercise.getName());

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("Init Task:Lap %d:%d", task, lap));
        }

        workoutTimer = new Timer(duration, progress::setProgress, this::setTimeLeft, () -> {
            progress.setProgress(100);
            hapticFeedback();

            if (BuildConfig.DEBUG) {
                Log.d(TAG, String.format("Finished task %d", task));
            }

            workout(circuit, task + 1, lap);
        });

        // 5 second start timer
        startTimer = new Timer(EXERCISE_COUNTDOWN, progress::setProgress, this::setTimeLeft, () -> {
            // set text to workout name
            activityText.setText(exerciseName);
            workoutTimer.start();

            if (BuildConfig.DEBUG) {
                Log.d(TAG, String.format("Started task %d", task));
            }
        });

        // set test to countdown
        String next = getResources().getString(R.string.next_activity, exerciseName, duration);
        activityText.setText(next);

        //Set Icon
        iconStill.setBackgroundResource(exercise.getIcon());
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

    private void setTimeLeft(double timeLeft) {
        timeText.setText(String.format(Locale.getDefault(), "%02.0f", timeLeft));
    }

    @FunctionalInterface
    private interface SetProgressCallback {
        void setProgress(int progress);
    }

    @FunctionalInterface
    private interface SetSecondsLeftCallback {
        void setSecondsLeft(double timeLeft);
    }

    @FunctionalInterface
    private interface OnFinished {
        void onFinish();
    }

    private static class Timer extends CountDownTimer {
        private static final long TICK_RATE = 100L;
        private static final int MILLIS_IN_SECOND = 1000;
        // Time in seconds.
        private final long duration;
        private final SetProgressCallback progressCallback;
        private final SetSecondsLeftCallback secondsLeftCallback;
        private final OnFinished onFinished;

        Timer(long duration, SetProgressCallback progressCallback,
              SetSecondsLeftCallback secondsLeftCallback, OnFinished onFinished) {
            super(duration * MILLIS_IN_SECOND, TICK_RATE);
            this.duration = duration * MILLIS_IN_SECOND;
            this.progressCallback = progressCallback;
            this.secondsLeftCallback = secondsLeftCallback;
            this.onFinished = onFinished;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // Progress Bar
            long remaining = duration - millisUntilFinished;
            //noinspection NumericCastThatLosesPrecision
            int progress = (int) ((100 * remaining) / duration);
            progressCallback.setProgress(progress);

            // Timer
            double timeLeft = ((double) millisUntilFinished) / MILLIS_IN_SECOND;
            secondsLeftCallback.setSecondsLeft(timeLeft);
        }

        @Override
        public void onFinish() {
            onFinished.onFinish();
        }
    }
}
