package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Serializer;
import com.example.testapp.shared.Exercise;
import com.example.testapp.shared.ExerciseType;
import com.example.testapp.shared.Signal;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private Circuit circuit;
    public static final String CIRCUIT_MESSAGE_PATH = "/circuit_path_name";
    private static final String VOICE_TRANSCRIPTION_CAPABILITY_NAME = "voice_transcription";
    //public static final Context a = null;
    private static final String TAG = "MainActivity";

    /* As simple wrapper around Log.d */
    private static void LOGD(final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Exercise> exercises = new ArrayList<>(5);
        exercises.add(new Exercise(ExerciseType.BURPEES, 30));
        exercises.add(new Exercise(ExerciseType.PUSHUPS, 30));
        exercises.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 30));
        exercises.add(new Exercise(ExerciseType.SQUATS, 30));
        exercises.add(new Exercise(ExerciseType.STAR_JUMPS, 30));
        circuit = new Circuit(exercises, 5);
    }

    public void waitClicked(View v) throws ExecutionException, InterruptedException, IOException {
        Intent intent = new Intent(this, WaitingActivity.class);
        Signal startSignal = new Signal("START");
        byte[] bytesD = Serializer.serialize(startSignal);
        byte[] circuitBytes = Serializer.serialize(circuit);
        sendStartActivity(bytesD);
        startActivity(intent);
    }

    public void editClicked(View v) {
        Intent intent = new Intent(this, EditActivity.class);

        startActivity(intent);
    }

    private void sendStartActivity(byte[] bytesData) throws ExecutionException, InterruptedException {
        //LOGD(TAG, "Sendign start signal + circuit info");
        HashSet<String> nodesToSend = (HashSet<String>) getNodes();
        for (String nodeID : nodesToSend){
            if (nodeID != null) {

                Task<Integer> sendTask = Wearable.getMessageClient(getApplicationContext()).sendMessage(
                        nodeID, CIRCUIT_MESSAGE_PATH, bytesData);
                //sendTask.addOnSuccessListener(...);
                //sendTask.addOnFailureListener(...);
            } else {
                // TODO: Unable to retrieve node with transcription capability
            }
        }
    }

    private Collection<String> getNodes() throws ExecutionException, InterruptedException {
        HashSet<String> results = new HashSet<>();
        List<Node> nodes =
                Tasks.await(Wearable.getNodeClient(getApplicationContext()).getConnectedNodes());
        for (Node node : nodes) {
            results.add(node.getId());
        }
        return results;
    }
}
