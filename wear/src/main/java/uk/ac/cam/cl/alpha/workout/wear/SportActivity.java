package uk.ac.cam.cl.alpha.workout.wear;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.Locale;

import uk.ac.cam.cl.alpha.workout.BuildConfig;
import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Constants;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.PausableTimer;
import uk.ac.cam.cl.alpha.workout.shared.Serializer;
import uk.ac.cam.cl.alpha.workout.shared.Signal;

public class SportActivity extends WearableActivity
        implements SensorEventListener,
            MessageClient.OnMessageReceivedListener{
    static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.wear.CIRCUIT_ID";
    private static final long EXERCISE_COUNTDOWN = 5000L;
    private static final long TICK_RATE = 100L;
    private static final int MILLIS_IN_SECOND = 1000;
    private static final String MESSAGE = "SportActivity";
    private static final  long[] VIBRATION_PATTERN_LONG = {0, 500, 50, 800};
    private static final  long[] VIBRATION_PATTERN_SHORT = {0, 500};
    public static final String NULL_MESSAGE_RECEIVED = "Null Message Received";
    public static final String FAILED_TO_RECEIVE_MESSAGE = "Failed to receive message";

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;
    private PausableTimer currentTimer;
    private ImageView iconStill;
    private Circuit cir;
    private int currentLap;     // Index of current lap
    /**
     * Index of current exercise, -1 as pre-increment in nextExercise()
     */
    private int currentExerciseNo = -1;


    // For detecting the activities
    private SensorManager sensorManager;
    private Sensor mAccelerometer;

    private Exercise exercise;
    private Boolean exerciseStarted = false;
    private int count;


    // Run on activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Enables Always-on
        setAmbientEnabled();

        Wearable.getMessageClient(this).addListener(this);
        // Which view to use
        setContentView(R.layout.activity_sports);

        // Find elements in the view
        activityText = findViewById(R.id.exerciseName);
        timeText = findViewById(R.id.timeValue);
        pBar = findViewById(R.id.progressBar);

        iconStill = findViewById(R.id.sportsIcon);


        Wearable.getMessageClient(this).addListener(this);

        Intent intent = getIntent();
        byte[] serial = intent.getByteArrayExtra(CIRCUIT_ID);
        if (serial != null) {
            try {
                cir = (Circuit) Serializer.deserialize(serial);

                if (cir.getLaps() <= 0 || cir.countExercises() <= 0){
                    Log.d(MESSAGE, "D");
                    finish();
                }
                nextExercise();
            } catch (IOException | ClassNotFoundException e) {
                Log.d(MESSAGE, "Issue de-serializing circuit");
                finish();
            }
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Wearable.getMessageClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        Wearable.getMessageClient(this).removeListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentTimer != null){
            currentTimer.cancel();
        }


        Log.d(MESSAGE, "SportActivity Destroyed");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (exerciseStarted){
            if (DetectActivities.detectActivity(event.values, event.timestamp, exercise)){
                count ++;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Displays progress in the current activity (exercise or rest)
     * on the progress bar and textview.
     * @param msRemaining
     * @param msTotal
     */
    private void displayProgress(long msRemaining, long msTotal) {
        // Progress Bar
        // TODO Progress bar has max of 100, so limited resolution, maybe increase max & progress
        int progress = Math.toIntExact(100 * (msTotal - msRemaining) / msTotal);
        pBar.setProgress(progress);

        // Timer display
        double timeLeft = msRemaining / (double) MILLIS_IN_SECOND;

        if (count == 0){
            timeText.setText(String.format(Locale.getDefault(), "Time: %.0f s", timeLeft));
        } else {
            timeText.setText(String.format(Locale.getDefault(), "Time: %.0f s\nDone: %d", timeLeft, count));
        }

    }

    /**
     * Creates timers (5s start & exercise) for the next exercise
     * in the circuit and starts them. If the end of the circuit has
     * been reached the activity is closed.
     */
    private void nextExercise() {
        ++currentExerciseNo;
        // Exits workout once done

        if (currentExerciseNo >= cir.countExercises()) {
            // Finished last exercise in lap
            currentLap++;
            if (currentLap >= cir.getLaps()) {
                // Finished last lap
                finish();
            } else {
                // More laps to go
                currentExerciseNo = 0;
            }
        }
        // Update current variables
        Exercise currentExercise = cir.getExercises().get(currentExerciseNo);
        exercise = currentExercise;
        long currentExerciseDuration_ms = currentExercise.getDuration() * (long) MILLIS_IN_SECOND;
        String currentExerciseName = getResources().getString(currentExercise.getName());

        if (BuildConfig.DEBUG) {
            Log.d(MESSAGE, String.format("Init Task:Lap %d:%d", currentExerciseNo, currentLap));
        }

        // set test to countdown
        int duration = currentExercise.getDuration();
        String next = getResources().getString(R.string.next_activity, currentExerciseName, duration);
        activityText.setText(next);

        // Set Icon
        iconStill.setBackgroundResource(currentExercise.getIcon());
        AnimationDrawable iconAnimated = (AnimationDrawable) iconStill.getBackground();
        iconAnimated.start();

        // Count down timer
        PausableTimer exerciseTimer = new PausableTimer(currentExerciseDuration_ms, TICK_RATE) {
            @Override
            public void onTick(long millisUntilFinished) {
                displayProgress(millisUntilFinished,currentExerciseDuration_ms);
            }

            // Ends activity
            @Override
            public void onFinish() {
                if (BuildConfig.DEBUG) {
                    Log.d(MESSAGE, String.format("Finished Task %d", currentExerciseNo));
                }
                pBar.setProgress(100);  // Should be unnecessary
                hapticFeedback(new long[] {0, 500, 50, 800});

                nextExercise();
            }
        };

        // 5 second start timer
        PausableTimer startTimer = new PausableTimer(EXERCISE_COUNTDOWN, TICK_RATE) {

            @Override
            public void onTick(long millisUntilFinished) {
                displayProgress(millisUntilFinished,EXERCISE_COUNTDOWN);
            }

            @Override
            public void onFinish() {
                if (BuildConfig.DEBUG) {
                    Log.d(MESSAGE, String.format("Started Task %d", currentExerciseNo));
                }
                pBar.setProgress(100);  // Should be unnecessary
                hapticFeedback(new long[] {0, 500});
                // Set text to exercise name
                activityText.setText(currentExerciseName);

                exerciseStarted = true;
                count = 0;


                currentTimer = exerciseTimer;
                currentTimer.start();
            }
        };

        currentTimer = startTimer;
        currentTimer.start();
    }


    public void hapticFeedback(long[] vibrationPattern) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //-1 - don't repeat
        if (vibrator != null) {
            // -1 means don't repeat
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1));
        }
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        String messagePath = messageEvent.getPath();

        if(messageEvent.getData() == null) {
            Log.d(MESSAGE, NULL_MESSAGE_RECEIVED);
            return;
        }

        if (!(messagePath.equals(Constants.CIRCUIT_PATH) || messagePath.equals(Constants.SIGNAL_PATH)))  {
            return;
        }

        byte[] data = messageEvent.getData();
        Object message;

        try {
            message = Serializer.deserialize(data);
        } catch (ClassNotFoundException | IOException e) {
            Log.e(MESSAGE, FAILED_TO_RECEIVE_MESSAGE, e);
            return;
        }

        if (messagePath.equals(Constants.SIGNAL_PATH)) {
            switch ((Signal) message) {

                case PAUSE:
                    currentTimer.pause();
                    break;
                case RESUME:
                    currentTimer.resume();
                    break;
                case STOP:
                    finish();
                    break;

            }
        } else {
            Log.w(MESSAGE, "Unknown msg");
        }
    }
}