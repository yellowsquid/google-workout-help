package uk.ac.cam.cl.alpha.workout.mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseAdapter;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;

public class EditActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";

    private Circuit circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Retrieve circuit and token.
        circuit = (Circuit) getIntent().getSerializableExtra(CIRCUIT_ID);

        EditText numLapsEditText = findViewById(R.id.numLapsEditText);
        numLapsEditText.setText(String.format("%d", circuit.getLaps()));

        RecyclerView recyclerView = findViewById(R.id.circuitEditRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new ExerciseAdapter(circuit);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void addExerciseClicked(View v) {
        circuit.getExercises();
    }
}
