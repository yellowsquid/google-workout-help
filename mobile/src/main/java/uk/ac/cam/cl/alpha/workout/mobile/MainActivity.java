package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.CircuitAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitModel;

public class MainActivity extends AppCompatActivity {
    private CircuitModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.circuitSelectRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        CircuitAdapter circuitSelectAdapter = new CircuitAdapter();
        model = new ViewModelProvider(this).get(CircuitModel.class);

        // FIXME: get circuit from user
        model.setCircuitId(1);
        model.getCircuits().observe(this, circuitSelectAdapter::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(circuitSelectAdapter);
    }

    public void waitClicked(View v) {
        // TODO: send only circuit id, not full circuit
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra(WaitingActivity.CIRCUIT_ID, model.getCircuitId());
        startActivity(intent);
    }

    public void editClicked(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        // TODO: send only circuit id, not full circuit
        intent.putExtra(EditActivity.CIRCUIT_ID, model.getCircuitId());
        startActivity(intent);
    }
}
