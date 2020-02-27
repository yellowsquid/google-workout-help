package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.CircuitAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.database.Task;
import uk.ac.cam.cl.alpha.workout.mobile.drag.CircuitDetailsLookup;
import uk.ac.cam.cl.alpha.workout.mobile.drag.CircuitSelectionObserver;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitModel;
import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;
import uk.ac.cam.cl.alpha.workout.shared.PureCircuit;

public class MainActivity extends AppCompatActivity {
    private CircuitModel model;
    private View startButton;
    private View editButton;
    private SelectionTracker<Long> tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.circuitSelectRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        CircuitAdapter circuitSelectAdapter = new CircuitAdapter();
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

        ItemKeyProvider<Long> itemKeyProvider = new CircuitKeyProvider(recyclerView);
        CircuitDetailsLookup circuitDetailsLookup = new CircuitDetailsLookup(recyclerView);
        CircuitSelectionObserver observer = new CircuitSelectionObserver(this);
        CircuitItemActivatedListener activatedListener = new CircuitItemActivatedListener();

        tracker = new SelectionTracker.Builder<>(
                "Circuit selection tracker",
                recyclerView,
                itemKeyProvider,
                circuitDetailsLookup,
                StorageStrategy.createLongStorage())
                .withOnItemActivatedListener(activatedListener)
                .build();

        circuitSelectAdapter.setTracker(tracker);
        activatedListener.setTracker(tracker);
        tracker.addObserver(observer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Show the delete button if the menu there is a selection
        if (null != tracker){
            if (tracker.getSelection().isEmpty()) {
                menu.getItem(1).setEnabled(false);
                menu.getItem(1).setVisible(false);
            } else {
                menu.getItem(1).setEnabled(true);
                menu.getItem(1).setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_new_circuit:
                // Create new circuit and start editor
                Task<Long> task = model.createCircuit(BareCircuit.create(0, "New Circuit", 1)).chain(id-> {
                    model.setCircuitId(id);
                    editClicked(null);
                    return null;});
                model.dispatch(task);
                return true;

            case R.id.delete_selected_circuits:
                // Delete selected circuits
                if (null == tracker) {
                    return false;
                } else {
                    model.deleteCircuits(tracker.getSelection());
                    return true;
                }

            default:
                return false;
        }
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

    public void onCircuitStateChanged(){
        // Force the options menu to close and reopen, with the delete button in the correct state
        invalidateOptionsMenu();

        // Set the appropriate button state
        if (1 == tracker.getSelection().size()) {
            model.setCircuitId(tracker.getSelection().iterator().next());
            startButton.setEnabled(true);
            editButton.setEnabled(true);
        }
        else {
            model.setCircuitId(-1);
            startButton.setEnabled(false);
            editButton.setEnabled(false);
        }
    }
}
