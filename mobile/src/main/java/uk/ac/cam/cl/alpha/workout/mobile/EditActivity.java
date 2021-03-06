package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.MutableSelection;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.databinding.ActivityEditBinding;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseViewHolder;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.NumberWatcher;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseDetailsLookup;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseDragEventListener;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseDragInitiatedListener;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseKeyProvider;
import uk.ac.cam.cl.alpha.workout.mobile.drag.ExerciseSelectedActionMode;
import uk.ac.cam.cl.alpha.workout.mobile.drag.SelectionObserver;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitEditModel;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class EditActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    static final int ADD_EXERCISE_REQUEST = 1;

    private CircuitEditModel model;
    private ActionMode actionMode;
    private SelectionTracker<Long> tracker;
    private ActivityEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve circuit and token.
        // TODO: something sensible when get id 0
        long circuit = getIntent().getLongExtra(CIRCUIT_ID, 0);
        model = new ViewModelProvider(this).get(CircuitEditModel.class);
        model.setCircuitId(circuit);

        // Show the circuit laps and change value in store if user changes text
        Resources resources = getResources();
        EditText lapsText = binding.lapCount;
        model.getLaps().observe(this, laps -> lapsText
                .setText(resources.getString(R.string.pure_laps, laps)));

        // Show the circuit laps and change value in store if user changes text
        EditText nameText = binding.circuitName;
        model.getName().observe(this, name -> nameText
                .setText(resources.getString(R.string.pure_name, name)));

        // Create the RecyclerView that contains the circuit's exercises
        RecyclerView recyclerView = binding.exerciseList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ExerciseAdapter adapter =
                new ExerciseAdapter(model::updateItemDuration, this::isExerciseSelected);
        model.getExercises().observe(this, adapter::submitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener(new ExerciseDragEventListener(model));

        tracker = new SelectionTracker.Builder<>("exercise selection tracker", recyclerView,
                                                 new ExerciseKeyProvider(recyclerView),
                                                 new ExerciseDetailsLookup(recyclerView),
                                                 StorageStrategy.createLongStorage())
                .withOnDragInitiatedListener(
                        new ExerciseDragInitiatedListener(recyclerView, this::copySelection))
                .build();

        tracker.addObserver(new SelectionObserver(this::startExerciseActionMode));
    }

    private boolean isExerciseSelected(long position) {
        return tracker.isSelected(position);
    }

    private void copySelection(MutableSelection<Long> selection) {
        tracker.copySelection(selection);
    }

    // Used to start the contextual action mode when an exercise is selected
    public void startExerciseActionMode() {
        if (actionMode == null) {
            actionMode = startActionMode(new ExerciseSelectedActionMode(this));
        }
        if (tracker.getSelection().isEmpty()) {
            actionMode.finish();
        } else {
            String title = getResources()
                    .getString(R.string.selected_title, tracker.getSelection().size());
            actionMode.setTitle(title);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Get the selected exercise type and create a new circuit containing an instance of that
        // exercise
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            ExerciseType type =
                    (ExerciseType) data.getSerializableExtra(AddExerciseActivity.EXERCISE_ID);
            model.appendExercise(type);
        }
    }

    public void createExercise(View v) {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        startActivityForResult(intent, ADD_EXERCISE_REQUEST);
    }

    // Called when the contextual action mode is ended
    public void finishExerciseActionMode() {
        if (tracker != null) {
            tracker.clearSelection();
        }

        actionMode = null;
    }

    public void deleteSelected() {
        Selection<Long> selection = tracker.getSelection();
        List<Integer> selected = new ArrayList<>(selection.size());

        for (Long id : selection) {
            selected.add(Math.toIntExact(id));
        }

        model.deleteExercises(selected);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Update the circuit laps
        String lapsString = binding.lapCount.getText().toString();
        if (lapsString.isEmpty()) {
            lapsString = "0";
        }
        try {
            model.updateLaps(Integer.parseInt(lapsString));
        } catch (NumberFormatException e) {
            Log.e("EditActivity", "input not a number", e);
        }

        // Update the circuit name
        String nameString = binding.circuitName.getText().toString();
        if (nameString.isEmpty()) {
            nameString = "Circuit";
        }
        model.updateName(nameString);

        // Update the individual exercise durations
        int size = binding.exerciseList.getAdapter().getItemCount();
        for (int i = 0; i < size; i++) {
            model.updateItemDuration(i, ((ExerciseViewHolder) binding.exerciseList.findViewHolderForAdapterPosition(i)).getTime());
        }
    }
}
