package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.NodeAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.model.ServerModel;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;

public class WaitingActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    private ServerModel serverModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        // Retrieve circuit and token.
        Circuit circuit = (Circuit) getIntent().getSerializableExtra(CIRCUIT_ID);

        // FIXME: get token and list of nodes (maybe via server)

        // Create list
        RecyclerView recyclerView = findViewById(R.id.devicesConnectedRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        NodeAdapter adaptor = new NodeAdapter(); // Make and implement adaptor

        serverModel = new ViewModelProvider(this).get(ServerModel.class);
        serverModel.setCircuit(getApplication(), circuit);

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
        serverModel.sendStartSignal(getApplication());
        Intent intent = new Intent(this, DuringActivity.class);
        startActivity(intent);
    }
}
