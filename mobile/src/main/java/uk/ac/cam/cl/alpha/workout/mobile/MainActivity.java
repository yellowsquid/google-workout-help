package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutionException;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.CircuitAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.database.Task;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitModel;
import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BareCircuit circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.circuitSelectRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        CircuitAdapter circuitSelectAdapter = new CircuitAdapter();
        CircuitModel model = new ViewModelProvider(this).get(CircuitModel.class);

        // FIXME: create pre-populated database instead.
        circuit = BareCircuit.create(0, "PureCircuit 1", 3);

        Task<Integer[]> task = model.createCircuit(circuit).chainTask(id -> {
            circuit = BareCircuit.create(id, String.format("Circuit %d", id), 3);
            return Task
                    .group(new Integer[0], model.appendExercise(circuit, ExerciseType.BURPEES, 30),
                           model.appendExercise(circuit, ExerciseType.PUSHUPS, 30),
                           model.appendExercise(circuit, ExerciseType.RUSSIAN_TWISTS, 30),
                           model.appendExercise(circuit, ExerciseType.SQUATS, 30),
                           model.appendExercise(circuit, ExerciseType.STAR_JUMPS, 30),
                           model.appendExercise(circuit, ExerciseType.REST, 30));
        });
        try {
            model.dispatch(task).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.w(TAG, "failed to create model", e);
        }

        model.getCircuits().observe(this, circuitSelectAdapter::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(circuitSelectAdapter);
    }

    public void waitClicked(View v) {
        // TODO: send only circuit id, not full circuit
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra(WaitingActivity.CIRCUIT_ID, circuit);
        startActivity(intent);
    }

    public void editClicked(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        // TODO: send only circuit id, not full circuit
        intent.putExtra(EditActivity.CIRCUIT_ID, circuit);
        startActivity(intent);
    }
}
