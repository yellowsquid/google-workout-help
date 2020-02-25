package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.CircuitAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.database.Task;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitModel;
import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;
import uk.ac.cam.cl.alpha.workout.shared.PureCircuit;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_new_circuit:
                // Add new circuit
                Task<Long> task = model.createCircuit(BareCircuit.create(0, "New Circuit", 1)).chain(id-> {
                    model.setCircuitId(id);
                    editClicked(null);
                    return null;});
                model.dispatch(task);
                return true;
            case R.id.delete_selected_circuits:
                // Delete selected circuits
                return true;
            default:
                return false;
        }
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
