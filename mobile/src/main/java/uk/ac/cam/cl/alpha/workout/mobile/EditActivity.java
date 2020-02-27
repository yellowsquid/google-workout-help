package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.NameWatcher;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.NumberWatcher;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseDetailsLookup;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseDragEventListener;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseDragInitiatedListener;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseSelectedActionMode;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseSelectionObserver;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitEditModel;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class EditActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    static final int ADD_EXERCISE_REQUEST = 1;

    private CircuitEditModel model;
    private ActionMode actionMode;
    private SelectionTracker<Long> tracker;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Retrieve circuit and token.
        // TODO: something sensible when get id 0
        long circuit = getIntent().getLongExtra(CIRCUIT_ID, 0);
        model = new ViewModelProvider(this).get(CircuitEditModel.class);
        model.setCircuitId(circuit);

        // Show the circuit laps and change value in store if user changes text
        Resources resources = getResources();
        EditText lapsText = findViewById(R.id.numLapsEditText);
        model.getLaps().observe(this, laps -> lapsText
                .setText(resources.getString(R.string.pure_laps, laps)));
        lapsText.addTextChangedListener((NumberWatcher) number -> model.updateLaps(number));

        // Show the circuit laps and change value in store if user changes text
        EditText nameText = findViewById(R.id.circuitNameEditText);
        model.getName().observe(this, name -> nameText
                .setText(resources.getString(R.string.pure_name, name)));
        nameText.addTextChangedListener((NameWatcher) name -> model.updateName(name));

        // Create the RecyclerView that contains the circuit's exercises
        recyclerView = findViewById(R.id.circuitEditRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ExerciseAdapter adapter = new ExerciseAdapter(model::updateItemDuration);
        model.getExercises().observe(this, adapter::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new ExerciseDragEventListener(model));

        // TODO: SELECTION STUFF HERE, MAYBE EXTRACT TO PRIVATE METHOD?
        ItemKeyProvider<Long> itemKeyProvider = new ExerciseKeyProvider(recyclerView);
        ExerciseSelectionObserver observer = new ExerciseSelectionObserver(this);
        ExerciseDragInitiatedListener exerciseDragListener = new ExerciseDragInitiatedListener();

        tracker = new SelectionTracker.Builder<>(
                "exercise selection tracker",
                recyclerView,
                itemKeyProvider,
                new ExerciseDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage())
                .withOnDragInitiatedListener(exerciseDragListener)
                .build();

        adapter.setTracker(tracker);
        exerciseDragListener.setFields(tracker, recyclerView);
        tracker.addObserver(observer);
    }

    @Override
    // Get the selected exercise type and create a new circuit containing an instance of that
    // exercise
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            ExerciseType type =
                    (ExerciseType) data.getSerializableExtra(AddExerciseActivity.EXERCISE_ID);
            model.appendExercise(type);
        }
    }

    public void addExerciseClicked(View v) {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        startActivityForResult(intent, ADD_EXERCISE_REQUEST);
    }

    // Used to start the contextual action mode when an exercise is selected
    public void startExerciseActionMode() {
        if (actionMode == null) {
            actionMode = startActionMode(new ExerciseSelectedActionMode(this));
        }
        if (tracker.getSelection().isEmpty()) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.format("Selected: %d", tracker.getSelection().size()));
        }
    }

    // Called when the contextual action mode is ended
    public void finishExerciseActionMode() {
        if (tracker != null) {
            tracker.clearSelection();
        }

        actionMode = null;
    }

    public SelectionTracker<Long> getTracker() {
        return tracker;
    }

    public CircuitEditModel getModel() {
        return model;
    }

    public void deleteSelected() {
        Selection<Long> selection = tracker.getSelection();
        int initialSize = recyclerView.getAdapter().getItemCount();
        model.deleteExercises(selection);
        for (long key : selection) {
            recyclerView.getAdapter().notifyItemRemoved(Math.toIntExact(key));
            recyclerView.getAdapter().notifyItemRangeChanged(Math.toIntExact(key), initialSize);
            initialSize -= 1;
        }
    }
}
