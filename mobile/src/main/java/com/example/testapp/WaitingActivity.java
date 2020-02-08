package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Exercise;
import com.example.testapp.shared.ExerciseType;
import com.example.testapp.shared.Serializer;
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

import static com.example.testapp.MainActivity.CIRCUIT_MESSAGE_PATH;

public class WaitingActivity extends AppCompatActivity {
    private Circuit circuit;
    public final static String CIRCUIT_ID = "com.example.testapp.CIRCUIT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        // Retrieve circuit and token.
        Circuit circuit = (Circuit) getIntent().getSerializableExtra(CIRCUIT_ID);

        // FIXME: get token and list of nodes (maybe via server)
        LiveData<List<String>> connectedNodes;
        String accessToken;

        // Create list
        RecyclerView recyclerView = findViewById(R.id.devicesConnectedRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        final NodeAdapter adaptor = new NodeAdapter(); // Make and implement adaptor

        // FIXME: replace with live data
        List<String> testData = new ArrayList<>(3);
        testData.add("first");
        testData.add("second");
        testData.add("third");
        adaptor.submitList(testData);

        // connectedNodes.observe(this, adaptor::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);

        // start activity
        List<Exercise> exercises = new ArrayList<>(5);
        exercises.add(new Exercise(ExerciseType.BURPEES, 30));
        exercises.add(new Exercise(ExerciseType.PUSHUPS, 30));
        exercises.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 30));
        exercises.add(new Exercise(ExerciseType.SQUATS, 30));
        exercises.add(new Exercise(ExerciseType.STAR_JUMPS, 30));
        circuit = new Circuit(exercises, 5);
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

    private void sendDataToWatches(byte[] bytesData) throws ExecutionException, InterruptedException {
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

    public void startClicked(View v) {
        Signal startSignal = new Signal("START");
        try {
            byte[] startSignalBytes = com.example.testapp.shared.Serializer.serialize(startSignal);
            byte[] circuitBytes = Serializer.serialize(circuit);
            sendDataToWatches(startSignalBytes);
            sendDataToWatches(circuitBytes);
            Intent intent = new Intent(this, DuringActivity.class);
            startActivity(intent);
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed");
        } catch (InterruptedException |  ExecutionException e) {
            // ignore
        }
    }
}
