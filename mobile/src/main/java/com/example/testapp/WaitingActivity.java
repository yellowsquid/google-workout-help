package com.example.testapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapter.NodeAdapter;
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
    }



    @WorkerThread
    private Collection<String> getNodes() {
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
          //  Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
           // Log.e(TAG, "Interrupt occurred: " + exception);
        }

        return results;
    }


    private class StartSignalInit extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartMessage(node);
            }
            return null;
        }
    }

    @WorkerThread
    private void sendStartMessage(String node) {


        try {

            Task<Integer> sendMessageStart =
                    Wearable.getMessageClient(this).sendMessage(node, "/circuit_path_name",Serializer.serialize(Signal.START));

            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await(sendMessageStart);
            //LOGD(TAG, "Message sent: " + result);

        } catch (ExecutionException exception) {
         //   Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
          //  Log.e(TAG, "Interrupt occurred: " + exception);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class CircuitSignalInit extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendCircuitMessage(node);
            }
            return null;
        }
    }

    @WorkerThread
    private void sendCircuitMessage(String node) {


        try {

            Task<Integer> sendMessageCircuit =
                    Wearable.getMessageClient(this).sendMessage(node, "/circuit_path_name",Serializer.serialize(circuit));

            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await(sendMessageCircuit);
            //LOGD(TAG, "Message sent: " + result);

        } catch (ExecutionException exception) {
            //   Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
            //  Log.e(TAG, "Interrupt occurred: " + exception);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startClicked(View v) {
            new CircuitSignalInit().execute();
            new StartSignalInit().execute();
            Intent intent = new Intent(this, DuringActivity.class);
            startActivity(intent);

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


}
