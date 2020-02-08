package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapter.NodeAdapter;
import com.example.testapp.shared.Circuit;
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
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class WaitingActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "com.example.testapp.CIRCUIT_ID";
    private Circuit circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        // Retrieve circuit and token.
        circuit = (Circuit) getIntent().getSerializableExtra(CIRCUIT_ID);

        // FIXME: get token and list of nodes (maybe via server)
        LiveData<List<String>> connectedNodes;
        String accessToken;

        // Create list
        RecyclerView recyclerView = findViewById(R.id.devicesConnectedRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        NodeAdapter adaptor = new NodeAdapter(); // Make and implement adaptor

        // FIXME: replace with live data
        List<String> testData = new ArrayList<>(3);
        testData.add("first");
        testData.add("second");
        testData.add("third");
        adaptor.submitList(testData);

        // connectedNodes.observe(this, adaptor::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);
    }

    public void startClicked(View v) {
        Signal startSignal = Signal.START;
        try {
            byte[] startSignalBytes = Serializer.serialize(startSignal);
            byte[] circuitBytes = Serializer.serialize(circuit);
            sendDataToWatches(startSignalBytes);
            sendDataToWatches(circuitBytes);
            Intent intent = new Intent(this, DuringActivity.class);
            startActivity(intent);
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed");
        } catch (InterruptedException | ExecutionException e) {
            // ignore
        }
    }

    /*
    HashSet<String> results = new HashSet<>();

    Task<List<Node>> nodeListTask =
            Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();

        try {
        // Block on a task and get the result synchronously (because this is on a background
        // thread).
        List<Node> nodes = Tasks.await(nodeListTask);

        for (Node node : nodes) {
            results.add(node.getId());
        }

    } catch (ExecutionException exception) {
        Log.e(TAG, "Task failed: " + exception);

    } catch (InterruptedException exception) {
        Log.e(TAG, "Interrupt occurred: " + exception);
    }

        return results;
*/
    private void sendDataToWatches(byte[] bytesData) throws ExecutionException,
            InterruptedException {
        //LOGD(TAG, "Sendign start signal + circuit info");
        for (String nodeID : getNodes()) {
            if (nodeID != null) {

                Task<Integer> sendTask = Wearable.getMessageClient(getApplicationContext())
                        .sendMessage(nodeID, MainActivity.CIRCUIT_MESSAGE_PATH, bytesData);
                //sendTask.addOnSuccessListener(...);
                //sendTask.addOnFailureListener(...);
            } else {
                // TODO: Unable to retrieve node with transcription capability
            }
        }
    }

    @WorkerThread
    private Collection<String> getNodes() {
        Set<String> results = new HashSet<>();
        //List<Node> nodes = Tasks.await(Wearable.getNodeClient(getApplicationContext())
        // .getConnectedNodes());
        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            List<Node> nodes = Tasks.await(nodeListTask);

            for (Node node : nodes) {
                results.add(node.getId());
            }
        } catch (ExecutionException exception) {
            //Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
            //Log.e(TAG, "Interrupt occurred: " + exception);
        }

        return results;
    }
}
