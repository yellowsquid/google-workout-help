package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitEditModel;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class EditActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    static final int ADD_EXERCISE_REQUEST = 1;

    private CircuitEditModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Retrieve circuit and token.
        // TODO: something sensible when get id 0
        long circuit = getIntent().getLongExtra(CIRCUIT_ID, 0);
        model = new ViewModelProvider(this).get(CircuitEditModel.class);
        model.setCircuitId(circuit);

        EditText numLapsEditText = findViewById(R.id.numLapsEditText);
        Resources resources = getResources();
        model.getLaps().observe(this, laps -> numLapsEditText
                .setText(resources.getString(R.string.pure_laps, laps)));

        RecyclerView recyclerView = findViewById(R.id.circuitEditRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ExerciseAdapter adapter = new ExerciseAdapter();
        model.getExercises().observe(this, adapter::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    // Get the selected exercise type and create a new circuit containing an instance of that
    // exercise
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            ExerciseType type =
                    (ExerciseType) data.getSerializableExtra(AddExerciseActivity.EXERCISE_ID);
            model.dispatch(model.appendExercise(type));
        }
    }

    public void addExerciseClicked(View v) {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        startActivityForResult(intent, ADD_EXERCISE_REQUEST);
    }
}
