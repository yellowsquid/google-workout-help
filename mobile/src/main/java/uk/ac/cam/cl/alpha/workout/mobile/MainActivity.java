package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.alpha.workout.mobile.adapter.CircuitAdapter;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class MainActivity extends AppCompatActivity {
    private Circuit circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Exercise> exercises = new ArrayList<>(5);
        exercises.add(new Exercise(ExerciseType.BURPEES, 30));
        exercises.add(new Exercise(ExerciseType.PUSHUPS, 30));
        exercises.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 20));
        exercises.add(new Exercise(ExerciseType.SQUATS, 30));
        exercises.add(new Exercise(ExerciseType.STAR_JUMPS, 30));
        circuit = new Circuit("Circuit 1", exercises, 3);
        Circuit circuit2 = new Circuit("Circuit 2", exercises, 5);

        List<Circuit> testCircuits = new ArrayList<>();
        testCircuits.add(circuit);
        testCircuits.add(circuit2);

        RecyclerView recyclerView = findViewById(R.id.circuitSelectRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        CircuitAdapter circuitSelectAdapter = new CircuitAdapter(testCircuits);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(circuitSelectAdapter);

    }

    public void waitClicked(View v) {
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra(WaitingActivity.CIRCUIT_ID, circuit);
        startActivity(intent);
    }

    public void editClicked(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.CIRCUIT_ID, circuit);
        startActivity(intent);
    }
}
