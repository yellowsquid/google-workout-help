package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.databinding.ActivityWaitingBinding;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.NodeAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.model.ServerModel;

public class WaitingActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    private ServerModel serverModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWaitingBinding binding = ActivityWaitingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve circuit
        // TODO: something sensible on value 0
        long circuitId = getIntent().getLongExtra(CIRCUIT_ID, 0);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);

        // Create list
        RecyclerView recyclerView = binding.devicesView;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        NodeAdapter adaptor = new NodeAdapter(); // Make and implement adaptor

        serverModel = viewModelProvider.get(ServerModel.class);
        serverModel.setCircuitId(circuitId);
        serverModel.getDevicesList().observe(this, adaptor::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);

        // Display circuit name.
        serverModel.getCircuitName().observe(this, binding.name::setText);
    }

    public void startClicked(View v) {
        serverModel.sendStartSignal();
        Intent intent = new Intent(this, DuringActivity.class);
        intent.putExtra(CIRCUIT_ID, serverModel.getCircuitId());
        startActivity(intent);
    }
}
