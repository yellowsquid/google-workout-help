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
    private View startButton;
    private View editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.circuitSelectRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        CircuitAdapter circuitSelectAdapter = new CircuitAdapter(this::itemClicked);
        model = new ViewModelProvider(this).get(CircuitModel.class);
        model.getCircuits().observe(this, circuitSelectAdapter::submitList);

        startButton = findViewById(R.id.startButton);
        editButton = findViewById(R.id.editButton);

        if (!model.isCircuitSelected()) {
            startButton.setEnabled(false);
            editButton.setEnabled(false);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(circuitSelectAdapter);
    }

    public void itemClicked(long id, View v) {
        model.setCircuitId(id);
        v.setSelected(true);
        startButton.setEnabled(true);
        editButton.setEnabled(true);
    }

    public void editClicked(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.CIRCUIT_ID, model.getCircuitId());
        startActivity(intent);
    }

    public void startClicked(View v) {
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra(WaitingActivity.CIRCUIT_ID, model.getCircuitId());
        startActivity(intent);
    }
}
