package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseAdapter;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class EditActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    public static final String EXERCISE_ID = "uk.ac.cam.cl.alpha.workout.mobile.EXERCISE_ID";
    static final int ADD_EXERCISE_REQUEST = 1;

    private Circuit circuit;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Retrieve circuit and token.
        circuit = (Circuit) getIntent().getSerializableExtra(CIRCUIT_ID);

        EditText numLapsEditText = findViewById(R.id.numLapsEditText);
        numLapsEditText.setText(String.format("%d", circuit.getLaps()));

        recyclerView = findViewById(R.id.circuitEditRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ExerciseAdapter adapter = new ExerciseAdapter(circuit);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void addExerciseClicked(View v) {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        startActivityForResult(intent, ADD_EXERCISE_REQUEST);
    }

    @Override
    // Get the selected exercise type and create a new circuit containing an instance of that
    // exercise
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            ExerciseType newExerciseType = (ExerciseType) data.getSerializableExtra(EXERCISE_ID);

            // Cannot add exercise to circuit so create new circuit with extra exercise
            ArrayList<Exercise> updatedExercises = new ArrayList<>();
            updatedExercises.addAll(circuit.getExercises());
            updatedExercises.add(new Exercise(newExerciseType, 0));
            circuit = new Circuit(circuit.getName(), updatedExercises, circuit.getLaps());

            // Update recyclerView to use the new circuit object
            recyclerView.setAdapter(new ExerciseAdapter(circuit));
        }
    }
}
