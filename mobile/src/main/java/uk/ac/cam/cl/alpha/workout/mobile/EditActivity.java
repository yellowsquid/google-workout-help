package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitModel;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;
import uk.ac.cam.cl.alpha.workout.shared.PureCircuit;

public class EditActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    public static final String EXERCISE_ID = "uk.ac.cam.cl.alpha.workout.mobile.EXERCISE_ID";
    static final int ADD_EXERCISE_REQUEST = 1;

    private PureCircuit circuit;
    private CircuitModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Retrieve circuit and token.
        circuit = (PureCircuit) getIntent().getSerializableExtra(CIRCUIT_ID);
        model = new ViewModelProvider(this).get(CircuitModel.class);

        EditText numLapsEditText = findViewById(R.id.numLapsEditText);
        numLapsEditText.setText(String.format("%d", circuit.getLaps()));

        RecyclerView recyclerView = findViewById(R.id.circuitEditRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ExerciseAdapter adapter = new ExerciseAdapter();
        model.getExercises(circuit).observe(this, list -> {
            List<Exercise> myList = new ArrayList<>(list);
            myList.sort(null);
            adapter.submitList(myList);
        });

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
            ExerciseType type = (ExerciseType) data.getSerializableExtra(EXERCISE_ID);
            model.appendExercise(circuit, type, 0);
        }
    }
}
