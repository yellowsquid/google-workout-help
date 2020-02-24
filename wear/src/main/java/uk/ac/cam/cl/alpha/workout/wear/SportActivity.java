package uk.ac.cam.cl.alpha.workout.wear;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.Button;
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
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.PausableTimer;
import uk.ac.cam.cl.alpha.workout.shared.Serializer;
import uk.ac.cam.cl.alpha.workout.shared.Signal;

// TODO Get rid of this annotation (and possibly deal with what it's complaining about)
@SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
public class SportActivity extends WearableActivity
        implements MessageClient.OnMessageReceivedListener {
    public static final long EXERCISE_COUNTDOWN = 5000L;
    public static final long TICK_RATE = 100L;
    public static final int MILLIS_IN_SECOND = 1000;

    private static final String MESSAGE = "WatchApp";
    private static final long[] VIBRATION_PATTERN = {0, 500, 50, 800};
    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;
    private PausableTimer currentTimer;
    private ImageView iconStill;
    private static Circuit cir;
    private static int currentLap  = 0;     // Index of current lap
    /**
     * Index of current exercise, -1 as pre-increment in nextExercise()
     */
    private static int currentExerciseNo = -1;
    private Button resumeButton;
    private Button pauseButton;

    // Run on activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MESSAGE, "SportActivity Created");
        super.onCreate(savedInstanceState);
        Wearable.getMessageClient(this).addListener(this);
        // Which view to use
        setContentView(R.layout.activity_sports);

        // Find elements in the view
        activityText = findViewById(R.id.exerciseName);
        timeText = findViewById(R.id.timeValue);
        pBar = findViewById(R.id.progressBar);
        resumeButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        resumeButton.setOnClickListener(v -> currentTimer.resume());
        pauseButton.setOnClickListener(v -> currentTimer.pause());

        iconStill = findViewById(R.id.sportsIcon);

        // Enables Always-on
        setAmbientEnabled();

        Intent intent = getIntent();
        Circuit cirLoaded;
        byte[] serial = intent.getByteArrayExtra("Circuit");
        if (serial != null) {
            try {
                cirLoaded = (Circuit) Serializer.deserialize(serial);
                cir=cirLoaded;
                nextExercise();
            // TODO Duplication in catch clauses
            } catch (IOException e) {
                // TODO Logging
                e.printStackTrace();
                finish();
            } catch (ClassNotFoundException e) {
                // TODO Logging
                e.printStackTrace();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentTimer.cancel();
        Log.d(MESSAGE, "SportActivity Destroyed");
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
        timeText.setText(String.format(Locale.getDefault(), "%.1f", timeLeft));
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
            if (currentLap >= cir.getLaps()) {
                // Finished last lap
                finish();
            } else {
                // More laps to go
                currentExerciseNo = 0;
                currentLap++;
            }
        }
        // Update current variables
        Exercise currentExercise = cir.getExercises().get(currentExerciseNo);
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
        final AnimationDrawable iconAnimated = (AnimationDrawable) iconStill.getBackground();
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
                // TODO Haptic feedback?
                // Set text to exercise name
                activityText.setText(currentExerciseName);

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
            vibrator.vibrate(VibrationEffect.createWaveform(VIBRATION_PATTERN, -1));
        }
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/circuit_path_name")) {
            byte[] data = messageEvent.getData();
            try {
                Object message = Serializer.deserialize(data);

                if (message == Signal.PAUSE) {
                    //call the pauseButton function
                    currentTimer.pause();
                } else if (message == Signal.RESUME) {
                    //call the resume function
                    currentTimer.resume();
                }

            } catch (ClassNotFoundException | IOException e) {
                System.err.println("Failed to receive message");
            }
        }
    }
}