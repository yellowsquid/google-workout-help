package uk.ac.cam.cl.alpha.workout.wear;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Constants;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;
import uk.ac.cam.cl.alpha.workout.shared.PureCircuit;
import uk.ac.cam.cl.alpha.workout.shared.Serializer;
import uk.ac.cam.cl.alpha.workout.shared.Signal;

public class MainActivity extends WearableActivity implements
        MessageClient.OnMessageReceivedListener {
    private static final String TAG = "MainActivity";
    private TextView statText;
    private byte[] circuit;

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        String messagePath = messageEvent.getPath();

        if(messageEvent.getData() == null) {
            Log.d(TAG, "Null Message Received");
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
            Log.e(TAG, "Failed to receive message", e);
            return;
        }

        if (messagePath.equals(Constants.CIRCUIT_PATH)) {
            statText.setText(((PureCircuit) message).getName());
            circuit = data;
        } else if (messagePath.equals(Constants.SIGNAL_PATH)) {
            switch ((Signal) message) {
                case START:
                    openSportActivity();
                    break;
                case STOP:
                case PAUSE:
                case RESUME:
                    Log.w(TAG, "Signal not yet implemented");
            }
        } else {
            Log.w(TAG, "Unknown message");
        }
    }

    private void openSportActivity() {
        Intent intent = new Intent(this, SportActivity.class);
        intent.putExtra("Circuit", circuit);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        Wearable.getMessageClient(this).addListener(this);
        statText = findViewById(R.id.statusText);

        // Enables Always-on
        setAmbientEnabled();

        List<Exercise> exercises = new ArrayList<>(5);
        exercises.add(Exercise.create(0, 5, 0, ExerciseType.BURPEES));
        exercises.add(Exercise.create(0, 5, 1, ExerciseType.STAR_JUMPS));
        exercises.add(Exercise.create(0, 5, 2, ExerciseType.RUSSIAN_TWISTS));
//        exercises.add(Exercise.create(0, 5, 3, ExerciseType.SITUPS));
//        exercises.add(Exercise.create(0, 5, 4, ExerciseType.REST));
        BareCircuit pureCircuit = BareCircuit.create(0, "fred", 100);
        try {
            circuit = Serializer.serialize(new Circuit(pureCircuit, exercises));
            openSportActivity();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(e.hashCode());
        }
    }
}


