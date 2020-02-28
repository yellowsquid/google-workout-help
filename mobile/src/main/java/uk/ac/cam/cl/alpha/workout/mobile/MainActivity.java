package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.databinding.ActivityMainBinding;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.CircuitAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.drag.CircuitDetailsLookup;
import uk.ac.cam.cl.alpha.workout.mobile.drag.CircuitKeyProvider;
import uk.ac.cam.cl.alpha.workout.mobile.drag.SelectionObserver;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitModel;
import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;

public class MainActivity extends AppCompatActivity {
    private CircuitModel model;
    private SelectionTracker<Long> tracker;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Show the delete button in the menu there is a selection
        MenuItem addItem = menu.findItem(R.id.add_new_circuit);
        MenuItem deleteItem = menu.findItem(R.id.delete_selected_circuits);
        MenuItem editItem = menu.findItem(R.id.edit_selected_circuit);

        int selectedCount = tracker.getSelection().size();

        boolean showAdd = selectedCount == 0;
        boolean showDelete = selectedCount != 0;
        boolean showEdit = selectedCount == 1;

        addItem.setEnabled(showAdd);
        addItem.setVisible(showAdd);

        deleteItem.setEnabled(showDelete);
        deleteItem.setVisible(showDelete);

        editItem.setEnabled(showEdit);
        editItem.setVisible(showEdit);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_circuit:
                // Create new circuit and start editor
                Runnable task = model.createCircuit(BareCircuit.create(0, "New Circuit", 1))
                        .andThen(this::editClicked);
                model.dispatch(task);
                return true;
            case R.id.delete_selected_circuits:
                model.deleteCircuits(tracker.getSelection());
                return true;
            case R.id.edit_selected_circuit:
                Selection<Long> selection = tracker.getSelection();

                if (selection.size() == 1) {
                    editClicked(selection.iterator().next());
                    return true;
                } else {
                    Log.e("MainActivity", "Someone clicked edit button when nothing's selected");
                    return false;
                }
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(CircuitModel.class);

        RecyclerView recyclerView = binding.circuitList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        CircuitAdapter circuitSelectAdapter =
                new CircuitAdapter(this::isCircuitSelected, this::circuitClicked);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(circuitSelectAdapter);

        model.getCircuits().observe(this, circuitSelectAdapter::submitList);

        tracker = new SelectionTracker.Builder<>("Circuit selection tracker", recyclerView,
                                                 new CircuitKeyProvider(recyclerView),
                                                 new CircuitDetailsLookup(recyclerView),
                                                 StorageStrategy.createLongStorage())
                .build();

        tracker.addObserver(new SelectionObserver(this::onCircuitStateChanged));
    }

    public void editClicked(long id) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.CIRCUIT_ID, id);
        startActivity(intent);
    }

    public void circuitClicked(long id) {
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra(WaitingActivity.CIRCUIT_ID, id);
        startActivity(intent);
    }

    public void onCircuitStateChanged() {
        // Force the options menu to close and reopen, with the delete button in the correct state
        invalidateOptionsMenu();
    }

    private boolean isCircuitSelected(long position) {
        return tracker.isSelected(position);
    }
}
