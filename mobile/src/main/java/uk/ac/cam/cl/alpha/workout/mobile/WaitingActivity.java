package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.NodeAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitModel;
import uk.ac.cam.cl.alpha.workout.mobile.model.ServerModel;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.PureCircuit;

public class WaitingActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    private ServerModel serverModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        // Retrieve circuit
        PureCircuit pureCircuit = (PureCircuit) getIntent().getSerializableExtra(CIRCUIT_ID);
        long circuitId = pureCircuit.getId();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        LiveData<Circuit> circuitData =
                viewModelProvider.get(CircuitModel.class).getCircuit(circuitId);

        // Create list
        RecyclerView recyclerView = findViewById(R.id.devicesConnectedRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        NodeAdapter adaptor = new NodeAdapter(); // Make and implement adaptor

        serverModel = viewModelProvider.get(ServerModel.class);
        serverModel.setCircuitData(circuitData);
        serverModel.setPeriodicDeviceListener(adaptor::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);
    }

    public void startClicked(View v) {
        serverModel.sendStartSignal();
        Intent intent = new Intent(this, DuringActivity.class);
        startActivity(intent);
    }
}
